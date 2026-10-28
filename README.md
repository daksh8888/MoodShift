# MoodShift

A personal Android TV launcher with a five-app dock, remote-first app grid, time-aware wallpapers and greetings, and a custom visual style.

The idea started after I saw an online trend of people customizing their TV home screens. The design direction and feature choices are mine, including the rounded screen treatment and Apple-inspired Liquid Glass focus style. I used AI, including OpenAI Codex, to help with coding and debugging, then tested and refined the details on the TV. MoodShift is an independent project and is not affiliated with Apple, Google, or any streaming service. Streaming-service names and icons belong to their respective owners.

## Features

- Bottom dock for YouTube, Spotify, Prime Video, Netflix, and Willow, when those apps are installed.
- Press **Down** from the dock to reveal the app grid; press **Up** from its first row to return.
- Rounded app artwork, centered focus treatment, and a moving translucent selection surface.
- Clock, date, time-based greeting, Friday greeting, and an offline Hindu festival greeting calendar.
- Darker color palette from 8 PM to 8 AM.
- Bright white, vector-sharp dock artwork during the morning session (6:00 AM–5:00 PM); the dark icon set returns outside that window.
- Three 4K wallpapers change with local time: mountain morning (6:00 AM–5:00 PM), Toronto evening (5:00–8:00 PM), and bokeh night (8:00 PM–6:00 AM).
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
