# 📱 Building the native Android app (Capacitor)

This folder contains the **Capacitor 6** shell that wraps the same `index.html` into a real
installable Android app — including a **foreground sentry service** so the camera keeps
monitoring with the screen off (Android only; iOS does not permit background camera).

## Prerequisites
- **Node.js** 18+ and **npm**
- **Android Studio** (with Android SDK 34+ / API 34) for building the APK
- A phone with Developer mode + USB debugging, or an emulator

## Build steps
```bash
cd native
npm install                 # install Capacitor (already in package-lock)
./sync-web.sh               # copies ../index.html etc. into www/ and syncs android/
npx cap open android        # opens the project in Android Studio
```
Then in Android Studio: **Build → Build APK(s)**. The APK lands in
`android/app/build/outputs/apk/debug/app-debug.apk`.

Or build from the CLI once the SDK is configured:
```bash
cd native/android && ./gradlew assembleDebug
```

## What the native layer adds
| File | Purpose |
|---|---|
| `SentryService.java` | Foreground service + persistent notification + PARTIAL_WAKE_LOCK |
| `SentryPlugin.java` | JS bridge: `Sentry.start() / stop() / keepAwake() / ignoreBatteryOptimizations()` |
| `AndroidManifest.xml` | CAMERA, RECORD_AUDIO, location, FOREGROUND_SERVICE_CAMERA, POST_NOTIFICATIONS, WAKE_LOCK |
| `MainActivity.java` | Registers `SentryPlugin` |

The web app already calls this bridge: toggling **Low-power mode** (🌙) starts/stops the
foreground sentry service when running inside the app.

## Caveats (honest)
- **iOS**: background camera is not allowed by Apple — parking sentry is screen-on only on iOS.
- Android 14+ requires the `FOREGROUND_SERVICE_CAMERA` permission (declared) and may ask the
  user to grant it at runtime.
- Battery optimization can pause the WebView; call
  `Native.ignoreBattery()` (or add the app to the battery whitelist) for long sentry sessions.
- The same web app runs on GitHub Pages — the native shell is optional but unlocks background
  monitoring and a store-installable app.

## Keeping in sync
Edit the app at the **repo root** (`../index.html`), then re-run `./sync-web.sh`. Only the
repo-root files are the source of truth; `www/` is a generated copy.
