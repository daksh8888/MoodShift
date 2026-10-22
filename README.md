# EcoTV Home

A remote-first Android TV home screen with a focused app dock, app grid, and a colorful, time-aware visual design.

This project began with an original user concept and visual direction, with implementation assistance from OpenAI Codex. It is an independent project and is not affiliated with Apple, Google, or any streaming service. Streaming-service names and icons belong to their respective owners.

## Features

- Bottom dock for YouTube, Spotify, Prime Video, Netflix, and Willow, when those apps are installed.
- Press **Down** from the dock to reveal the app grid; press **Up** from its first row to return.
- Rounded app artwork, centered focus treatment, and a moving translucent selection surface.
- Clock, date, time-based greeting, Friday greeting, and an offline Hindu festival greeting calendar.
- Darker color palette from 8 PM to 8 AM.
- Android Settings shortcut in the app grid.
- TV remote and D-pad navigation.

App availability, artwork, and remote-key behavior depend on the Android TV device and installed apps. This app does not include the streaming services themselves.

## Build

Requirements:

- JDK 17
- Android SDK Platform 35 and Android Build Tools 35.0.0
- Gradle 8.11.1 (Android Gradle Plugin 8.9.2)

Open this repository's root directory in Android Studio, or build from PowerShell with Gradle on `PATH`:

```powershell
gradle --no-daemon assembleDebug
```

The debug APK is created at `app\build\outputs\apk\debug\app-debug.apk`. To install it on a connected test device with Android Debug Bridge:

```powershell
adb install -r .\app\build\outputs\apk\debug\app-debug.apk
```

This is a debug build intended for testing. Do not treat it as a production release. A production distribution should use a release build signed with a key that you control.

## Privacy

This repository contains no Wi-Fi password, TV IP address, device serial number, ADB logs, screenshots, or device backup. The app can request Android's location permission to display the connected Wi-Fi network name (SSID); this value is used locally by the launcher.

## License

No open-source license is included yet. Until a license is added, viewing this repository does not grant permission to reuse, modify, or redistribute the code.
