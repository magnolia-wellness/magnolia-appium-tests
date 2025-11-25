# 📋 Test Configuration Guide

## 🎯 Quick Configuration

All configuration properties are easily editable in:
**`src/main/java/com/magnoliacollectivewellness/appium/config/TestConfig.java`**

## 🔧 Configuration Options

### 1. Platform Selection

Set the platform flag in `TestConfig.java`:

```java
public static final String PLATFORM = "android";  // or "ios"
```

**Or via command line:**
```bash
./gradlew test -Dplatform=android
./gradlew test -Dplatform=ios
```

### 2. Run Target Selection

Set the target flag in `TestConfig.java`:

```java
public static final String RUN_TARGET = "device";  // or "emulator"
```

**Or via command line:**
```bash
./gradlew test -DrunTarget=device
./gradlew test -DrunTarget=emulator
```

## 📱 Android Configuration

### Android Device Properties

Edit these values in `TestConfig.java`:

```java
// Android Device
public static final String ANDROID_DEVICE_NAME = "Priya's M34";
public static final String ANDROID_DEVICE_UDID = "RZCW82C6BCK";
public static final String ANDROID_DEVICE_PLATFORM_VERSION = "15";
```

### Android Emulator Properties

```java
// Android Emulator
public static final String ANDROID_EMULATOR_NAME = "Android Emulator";
public static final String ANDROID_EMULATOR_UDID = "emulator-5554";
public static final String ANDROID_EMULATOR_PLATFORM_VERSION = "12";
```

### Android App Properties

```java
public static final String ANDROID_APP_PACKAGE = "com.magnoliacollectivewellness.app";
public static final String ANDROID_APP_ACTIVITY = "com.magnoliacollectivewellness.app.MainActivity";
public static final String ANDROID_APP_PATH = "./app/app-debug.apk";
```

## 🍎 iOS Configuration

### iOS Device Properties

```java
// iOS Device
public static final String IOS_DEVICE_NAME = "iPhone";
public static final String IOS_DEVICE_UDID = "auto";
public static final String IOS_DEVICE_PLATFORM_VERSION = "16.0";
```

### iOS Simulator Properties

```java
// iOS Simulator
public static final String IOS_SIMULATOR_NAME = "iPhone 14";
public static final String IOS_SIMULATOR_UDID = "auto";
public static final String IOS_SIMULATOR_PLATFORM_VERSION = "16.0";
```

### iOS App Properties

```java
public static final String IOS_BUNDLE_ID = "com.magnoliacollectivewellness.app";
public static final String IOS_APP_PATH = "./app/Magnolia.app";
```

## 🔧 Common Settings

```java
public static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723";
public static final int COMMAND_TIMEOUT_SECONDS = 60;
public static final boolean NO_RESET = false;
public static final boolean AUTO_GRANT_PERMISSIONS = true;
```

## 🚀 Usage Examples

### Run on Android Device

```bash
./gradlew test -Dplatform=android -DrunTarget=device
```

### Run on Android Emulator

```bash
./gradlew test -Dplatform=android -DrunTarget=emulator
```

### Run on iOS Device

```bash
./gradlew test -Dplatform=ios -DrunTarget=device
```

### Run on iOS Simulator

```bash
./gradlew test -Dplatform=ios -DrunTarget=emulator
```

### Override Device Properties

```bash
./gradlew test -Dplatform=android -DrunTarget=device \
  -Dandroid.deviceName="My Device" \
  -Dandroid.deviceUdid="ABC123" \
  -Dandroid.platformVersion="14"
```

## 📊 Configuration Display

When tests run, all configuration values are automatically displayed:

```
══════════════════════════════════════════════════════════════════════════════
📋 TEST CONFIGURATION
══════════════════════════════════════════════════════════════════════════════
🎯 Platform: ANDROID
🎯 Run Target: DEVICE
──────────────────────────────────────────────────────────────────────────────
🤖 ANDROID CONFIGURATION:
   Device Name: Priya's M34
   UDID: RZCW82C6BCK
   Platform Version: 15
   App Package: com.magnoliacollectivewellness.app
   App Activity: com.magnoliacollectivewellness.app.MainActivity
   App Path: ./app/app-debug.apk
──────────────────────────────────────────────────────────────────────────────
🔧 COMMON SETTINGS:
   Appium Server URL: http://127.0.0.1:4723
   Command Timeout: 60 seconds
   No Reset: false
   Auto Grant Permissions: true
══════════════════════════════════════════════════════════════════════════════
```

## 💡 Tips

1. **Edit TestConfig.java** - All properties are clearly marked and easy to find
2. **Use System Properties** - Override values via command line without editing code
3. **Check Configuration Output** - Always displayed at test start for verification
4. **Platform-Specific Settings** - Android and iOS settings are clearly separated

