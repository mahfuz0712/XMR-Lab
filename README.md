# XMR Lab

**XMR Lab** is a tablet-first, cyber security and privacy research application for Android.

It keeps the upstream application and engine architecture (Root/CHROOT, User Mode Linux,
and rootless QEMU) while applying XMR Lab branding, a dark cyan/orange visual system,
and a permanent tablet navigation rail.

## Project status

The first pass covers application identity,
adaptive (portrait and landscape) layouts, tablet navigation shell, HUD palette, asset endpoints, OTA manifest,
and CI/local build configuration. Full visual QA and on-device engine validation are still
required before a release is considered stable.

## Runtime assets

Large Linux payloads are downloaded on first use from the XMR Lab GitHub Release tagged
`xmr-assets-651` and verified against SHA-256 and expected size values in
[`xmr_manifest.json`](xmr_manifest.json).

Expected release assets:

- `chroot64-debian.tar.gz` — Root/CHROOT
- `Image` — QEMU guest kernel
- `initrd.img` — QEMU initramfs
- `rootfs.imgz` — shared Debian root filesystem for UML and QEMU

QEMU, libslirp, UML kernel and support libraries are bundled in the APK under
`app/src/main/jniLibs/arm64-v8a/`. The remote manifest therefore does not need to host
those native libraries.

## Build locally

### Requirements

- JDK 17
- Android SDK Platform 37
- Android Build Tools 37.0.0
- Android NDK 25.1.8937393
- CMake 3.22.1
- Gradle wrapper (included)

Set `ANDROID_HOME` / `ANDROID_SDK_ROOT` and install the packages above with `sdkmanager`.

### Windows (PowerShell)

```powershell
.\gradlew.bat assembleDebug
```

### Linux / Ubuntu / WSL

```bash
chmod +x ./gradlew
./gradlew --no-daemon assembleDebug
```

APK output:

```text
app/build/outputs/apk/debug/
```

To build a release APK locally, configure the `XMRLAB_RELEASE_*` properties/environment
variables in `app/build.gradle` and run:

```bash
./gradlew --no-daemon assembleRelease
```

Do not commit signing keystores or passwords.

## GitHub Actions

`.github/workflows/android.yml` builds a debug APK on pushes and pull requests to `main`.
Use **Actions → XMR Lab Android CI → Run workflow** to choose `debug` or `release`.
The workflow uploads the APK as a downloadable Actions artifact. Release signing is optional
and uses repository secrets:

- `XMRLAB_KEYSTORE_BASE64`
- `XMRLAB_KEYSTORE_PASSWORD`
- `XMRLAB_KEY_ALIAS`
- `XMRLAB_KEY_PASSWORD`

Without these secrets, the release build is unsigned.


## Device scope

The upstream build configuration is arm64-v8a only and requires Android API 24 or newer.
Engine availability remains device-dependent: Root requires a rooted device, UML requires
the kernel/runtime support already implemented upstream, and QEMU runs rootlessly with
its own CPU/RAM overhead.
