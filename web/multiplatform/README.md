# Discount Detective Multiplatform

This is a fork of the [Android App](https://github.com/SheaSmith/discount-detective) designed to use
Kotlin Multiplatform + JetBrains Compose in order to run on more platforms. Inevitably, it is much
less feature rich and polished than the Android app.

## Run on Android:

- connect device or emulator
- `./gradlew installDebug`
- open app

## Run on Desktop jvm

`./gradlew run`

## Run native on MacOS

`./gradlew runDebugExecutableMacosX64` (Works on Intel processors)

## Run web assembly in browser

`./gradlew jsBrowserDevelopmentRun`

## Run on iOS simulator

`./gradlew iosDeployIPhone8Debug`
`./gradlew iosDeployIPadDebug`

## Run on iOS device

- `./gradlew iosDeployDeviceRelease`
