# XMR Lab

**XMR Lab** is a tablet-first, landscape-oriented, MUX-inspired community derivative of
[StrykerOSS](https://github.com/zalexdev/strykerapp).

It keeps the upstream application and engine architecture (Root/CHROOT, User Mode Linux,
and rootless QEMU) while applying XMR Lab branding, a dark cyan/orange visual system,
and a permanent tablet navigation rail.

## Project status

This is an early rebrand/integration branch. The first pass covers application identity,
landscape orientation, tablet navigation shell, MUX palette, asset endpoints, OTA manifest,
and CI/local build configuration. Full visual QA and on-device engine validation are still
required before a release is considered stable.

## Runtime assets

Large Linux payloads are downloaded on first use from the XMR Lab GitHub Release tagged
`xmr-assets-651` and verified against SHA-256 and expected size values in
[`stryker_manifest.json`](stryker_manifest.json).

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

## Upstream and licensing

XMR Lab is a derivative work of StrykerOSS. Preserve the upstream copyright notices,
`LICENSE`, and `THIRD-PARTY-NOTICES.md`. This project is distributed under the applicable
GNU GPL v3.0 terms. The corresponding source for this derivative must remain available.
Upstream: https://github.com/zalexdev/strykerapp

## Device scope

The upstream build configuration is arm64-v8a only and requires Android API 24 or newer.
Engine availability remains device-dependent: Root requires a rooted device, UML requires
the kernel/runtime support already implemented upstream, and QEMU runs rootlessly with
its own CPU/RAM overhead.
