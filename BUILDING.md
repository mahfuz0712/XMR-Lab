# Building XMR Lab from source

Most people don't need this — see the main [README](README.md) for how to just install
the app. This is for building the APK yourself.

## Requirements

- JDK 17
- Android SDK Platform 37
- Android Build Tools 37.0.0
- Android NDK 25.1.8937393
- CMake 3.22.1
- Gradle wrapper (included, no need to install Gradle separately)

Set `ANDROID_HOME` / `ANDROID_SDK_ROOT` and install the packages above with
`sdkmanager`, or follow the OS-specific steps below.

## Ubuntu 24.04 / WSL

```bash
sudo apt-get update
sudo apt-get install -y openjdk-17-jdk unzip wget
export ANDROID_HOME="$HOME/Android/Sdk"
export ANDROID_SDK_ROOT="$ANDROID_HOME"
mkdir -p "$ANDROID_HOME/cmdline-tools"
# Install Android command-line tools from the official Android developer site,
# then ensure sdkmanager is at $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager.
yes | "$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager" --licenses
"$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager" \
  "platform-tools" "platforms;android-37" "build-tools;37.0.0" \
  "ndk;25.1.8937393" "cmake;3.22.1"
chmod +x ./gradlew
./gradlew --no-daemon assembleDebug
```

## Windows

Install JDK 17 and Android command-line tools, set `ANDROID_HOME`, then:

```powershell
.\gradlew.bat assembleDebug
```

The project does not require opening Android Studio; Gradle CLI is sufficient.

APK output lands in:

```text
app/build/outputs/apk/debug/
```

## Release builds

To build a signed release APK locally, set the `XMRLAB_RELEASE_*` properties/
environment variables referenced in `app/build.gradle`, then run:

```bash
./gradlew --no-daemon assembleRelease
```

**Never commit signing keystores or passwords to the repository.**

## CI (GitHub Actions)

`.github/workflows/android.yml` builds a debug APK on every push/PR to `main`. Use
**Actions → XMR Lab Android CI → Run workflow** to build and choose `debug` or
`release`; the APK is uploaded as a downloadable Actions artifact either way.

A release build in CI is signed only if these repository secrets are set (otherwise
it builds unsigned):

- `XMRLAB_KEYSTORE_BASE64`
- `XMRLAB_KEYSTORE_PASSWORD`
- `XMRLAB_KEY_ALIAS`
- `XMRLAB_KEY_PASSWORD`

## Guest Linux images

The Debian rootfs and kernel that the app downloads at runtime (see "Runtime assets"
below) aren't part of the APK build — they're a separate, much heavier build. See
`images/build-all.sh` and `.github/workflows/build-images.yml` if you need to rebuild
and republish those.

## Runtime assets

Large Linux payloads are downloaded on first use from a GitHub Release and verified
against the SHA-256 and size values in [`xmr_manifest.json`](xmr_manifest.json):

- `chroot64-debian.tar.gz` — root/chroot engine
- `Image` — rootless VM guest kernel
- `initrd.img` — rootless VM initramfs
- `rootfs.imgz` — shared Debian root filesystem for UML and the rootless VM

QEMU, libslirp, the UML kernel, and their support libraries are bundled in the APK
under `app/src/main/jniLibs/arm64-v8a/` instead — the remote manifest only needs to
host the pieces above.

## Device scope

The build is `arm64-v8a` only and targets Android API 24 or newer. Engine
availability is device-dependent: the chroot engine requires a rooted device, UML
requires kernel/runtime support, and the rootless VM (QEMU) works on stock devices
with its own CPU/RAM overhead.
