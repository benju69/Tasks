# iOS App

This directory contains the iOS application entry point for the KMP Task Manager.

## Setup

1. Build the KMP shared framework:
   ```
   ./gradlew :data:embedAndSignAppleFrameworkForXcode
   ```

2. Open `iosApp.xcodeproj` in Xcode

3. The app uses the shared KMP framework built from the Kotlin modules

## Architecture

The iOS app uses Compose Multiplatform through a `UIViewController` bridge:
- Swift entry point: `iOSApp.swift`
- Bridge: `ContentView.swift` → `MainViewControllerKt.MainViewController()`
