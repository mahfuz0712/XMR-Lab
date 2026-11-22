# Local build notes

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
