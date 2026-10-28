# Earn Senses V1 — Android Studio source project

This is **source code, not an APK**. Android Studio must download dependencies and build an APK. The project has not been compiled or tested in this environment.

## Setup
1. Open the `EarnSensesV1` folder in Android Studio (recent version with Android Gradle Plugin 8.7 support).
2. Allow Gradle sync and install Android SDK 35 if prompted.
3. Connect an Android phone with USB debugging enabled; choose Run, or use Build > Build APK(s).
4. Launch Earn Senses and enable Android notification access for Earn Senses. This Android permission grants system-level visibility into notifications from all apps; the implementation deliberately filters to `com.doordash.driverapp` before reading text. If the Dasher app has a different package ID, update the filter.
5. Open Dasher and wait for a real Dasher notification. Return to Earn Senses and tap Refresh. Do not accept a poor offer solely for testing.
6. Test the manual hourly calculator with an example payment and estimated minutes.

## Implemented
- Dasher package-only notification content filter; last matching notification stored locally.
- Manual $35/hr gross earnings calculator and recommendation.
- No internet permission, screen recording, Accessibility Service, Google Maps integration, or automatic order actions.

## Not implemented / limitations
- Notification access cannot guarantee offer amounts, distance, locations, or order statuses are included.
- Android notification access is broad at the permission level even though the code filters immediately; only enable if comfortable.
- No automatic start/stop of screen capture. No screen capture at all in V1.
- No auto-detection of accepted, rejected, completed or cancelled orders.
- No compiled APK included because Android SDK/Gradle build tools are unavailable in this environment.
- No affiliation with DoorDash or Uber.
