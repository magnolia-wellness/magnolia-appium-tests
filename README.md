# Magnolia Appium Test Cases

This repository contains Appium test cases for the Magnolia Android application.

## 📋 Overview

This test suite uses **Java** with Appium to perform end-to-end testing on the Magnolia Android and iOS applications. The tests follow the Page Object Model (POM) pattern for maintainability and reusability.

### ✨ Key Features

- 🎯 **Easy Configuration** - All properties in one file (`TestConfig.java`)
- 🔄 **Reusable Setup** - Single `BaseTest` class for all tests
- 📱 **Cross-Platform** - Support for Android and iOS
- 🎮 **Flexible Targets** - Run on device or emulator/simulator
- 📊 **Auto-Display Config** - Configuration values shown at test start
- 🚀 **Zero Duplication** - No repeated setup/teardown code

## 🏗️ Project Structure

```
appium-tests/
├── src/
│   ├── main/java/com/magnoliacollectivewellness/appium/
│   │   ├── config/              # Configuration classes
│   │   │   ├── TestConfig.java      # ⭐ Easily editable configuration
│   │   │   └── AppiumConfig.java    # Driver creation based on config
│   │   ├── pages/               # Page Object Models
│   │   │   ├── LoginPage.java
│   │   │   └── HomePage.java
│   │   └── utils/               # Helper utilities
│   │       └── TestHelpers.java
│   └── test/java/com/magnoliacollectivewellness/appium/
│       ├── base/                # Base test class
│       │   └── BaseTest.java        # ⭐ Reusable setup/teardown
│       └── tests/               # Test files
│           ├── AppLaunchTest.java
│           └── LoginTest.java
├── app/                         # Place APK/IPA files here
├── screenshots/                 # Test screenshots (gitignored)
├── build.gradle                 # Gradle build configuration
├── CONFIGURATION.md             # ⭐ Configuration guide
└── README.md                    # This file
```

## 🚀 Setup

### Prerequisites

1. **Java JDK 11** or higher
2. **Gradle** (7.0 or higher) - or use Gradle Wrapper
3. **Appium** (v2.x)
4. **Android SDK** (for Android testing)
5. **Xcode** (for iOS testing, macOS only)

### Installation

1. **Install Appium globally:**
   ```bash
   npm install -g appium
   ```

2. **Install Appium drivers:**
   ```bash
   # For Android
   appium driver install uiautomator2
   
   # For iOS (macOS only)
   appium driver install xcuitest
   ```

3. **Verify Appium installation:**
   ```bash
   appium --version
   ```

4. **Build the project:**
   ```bash
   ./gradlew build
   ```

## ⚙️ Quick Configuration

### 🎯 Setting Up Your Test Environment

**All configuration is in one place: `TestConfig.java`**

1. **Set Platform Flag:**
   ```java
   public static final String PLATFORM = "android";  // or "ios"
   ```

2. **Set Run Target:**
   ```java
   public static final String RUN_TARGET = "device";  // or "emulator"
   ```

3. **Edit Device Properties:**
   - Android device: `ANDROID_DEVICE_NAME`, `ANDROID_DEVICE_UDID`, etc.
   - Android emulator: `ANDROID_EMULATOR_NAME`, `ANDROID_EMULATOR_UDID`, etc.
   - iOS device: `IOS_DEVICE_NAME`, `IOS_DEVICE_UDID`, etc.
   - iOS simulator: `IOS_SIMULATOR_NAME`, `IOS_SIMULATOR_UDID`, etc.

**📖 For detailed configuration guide, see [CONFIGURATION.md](CONFIGURATION.md)**

### Example Configuration Output

When you run tests, you'll see:
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
   ...
══════════════════════════════════════════════════════════════════════════════
```

## 📱 Preparing the App

### Android

1. Build the APK from the main project:
   ```bash
   cd ../magnolia-android
   ./gradlew assembleDebug
   ```

2. Copy the APK to the appium-tests directory:
   ```bash
   cp app/build/outputs/apk/debug/app-debug.apk ../appium-tests/app/
   ```

### iOS

1. Build the IPA from the main project
2. Copy the app bundle to `appium-tests/app/`

## 🧪 Running Tests

### Start Appium Server

**Important:** Start Appium server before running tests!

In a separate terminal:
```bash
appium --allow-cors
```

### Run All Tests

Uses configuration from `TestConfig.java`:
```bash
./gradlew test
```

### Run Specific Test Class

```bash
./gradlew test --tests "com.magnoliacollectivewellness.appium.tests.LoginTest"
```

### Run with Platform and Target Flags

```bash
# Android device
./gradlew test -Dplatform=android -DrunTarget=device

# Android emulator
./gradlew test -Dplatform=android -DrunTarget=emulator

# iOS device
./gradlew test -Dplatform=ios -DrunTarget=device

# iOS simulator
./gradlew test -Dplatform=ios -DrunTarget=emulator

# Skip tests during build
./gradlew build -DskipAppiumTests=true
```

## 📝 Writing Tests

### BaseTest - The Foundation

**All test classes extend `BaseTest` to get automatic setup/teardown:**

```java
// tests/LoginTest.java
public class LoginTest extends BaseTest {
    
    @Test
    public void test01_LoginWithValidCredentials() {
        // ✅ Driver is already initialized
        // ✅ loginPage and homePage are ready to use
        // ✅ Configuration is automatically applied
        
        loginPage.login("test@email.com", "password");
        assertTrue(homePage.isHomePageDisplayed());
    }
}
```

### What BaseTest Provides

| Feature | Description |
|---------|-------------|
| **Automatic Setup** | Driver created based on `TestConfig` flags |
| **Pre-initialized Pages** | `loginPage` and `homePage` ready to use |
| **Platform Detection** | Automatically handles Android/iOS differences |
| **Target Selection** | Automatically uses device or emulator settings |
| **Configuration Display** | Shows all settings at test start |
| **Consistent Teardown** | Automatic cleanup after each test |
| **Skip Support** | Respects `skipAppiumTests` flag for builds |

### Benefits

✅ **Zero Duplication** - One setup method for all tests  
✅ **Easy Maintenance** - Change config once, affects all tests  
✅ **Clear Structure** - Focus on test logic, not setup  
✅ **Flexible** - Override any setting via command line  

### Page Object Model Pattern

Tests use the Page Object Model (POM) pattern for maintainability:

```java
// pages/LoginPage.java
public class LoginPage {
    private final AppiumDriver driver;
    
    public LoginPage(AppiumDriver driver) {
        this.driver = driver;
    }
    
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        tapLoginButton();
    }
}
```

### Creating New Test Classes

**Step 1:** Extend `BaseTest`
```java
public class MyNewTest extends BaseTest {
    // That's it! Setup and teardown are automatic
}
```

**Step 2:** Write your tests
```java
@Test
public void testMyFeature() {
    // ✅ driver is available
    // ✅ loginPage and homePage are pre-initialized
    // ✅ All configuration is applied automatically
    
    loginPage.login("email", "password");
    // Your test logic here
}
```

**Step 3:** Add custom page objects (optional)
```java
private MyCustomPage myCustomPage;

@Override
protected void initializePageObjects() {
    super.initializePageObjects();  // Keep existing pages
    myCustomPage = new MyCustomPage(driver);  // Add your page
}
```

### Complete Example

```java
package com.magnoliacollectivewellness.appium.tests;

import com.magnoliacollectivewellness.appium.base.BaseTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class AppointmentTest extends BaseTest {
    
    @Test
    public void test01_BookAppointment() {
        // Login first
        loginPage.login("user@email.com", "password");
        
        // Navigate to appointments
        homePage.navigateToAppointments();
        
        // Your test logic here
        assertTrue("Appointment should be booked", true);
    }
}
```

## 🔧 Configuration Details

### TestConfig.java - Main Configuration File

**Location:** `src/main/java/com/magnoliacollectivewellness/appium/config/TestConfig.java`

This file contains **all** test configuration properties:

#### Platform & Target Flags
```java
public static final String PLATFORM = "android";      // "android" or "ios"
public static final String RUN_TARGET = "device";     // "device" or "emulator"
```

#### Android Properties
```java
// Device
ANDROID_DEVICE_NAME = "Priya's M34"
ANDROID_DEVICE_UDID = "RZCW82C6BCK"
ANDROID_DEVICE_PLATFORM_VERSION = "15"

// Emulator
ANDROID_EMULATOR_NAME = "Android Emulator"
ANDROID_EMULATOR_UDID = "emulator-5554"
ANDROID_EMULATOR_PLATFORM_VERSION = "12"
```

#### iOS Properties
```java
// Device
IOS_DEVICE_NAME = "iPhone"
IOS_DEVICE_UDID = "auto"
IOS_DEVICE_PLATFORM_VERSION = "16.0"

// Simulator
IOS_SIMULATOR_NAME = "iPhone 14"
IOS_SIMULATOR_UDID = "auto"
IOS_SIMULATOR_PLATFORM_VERSION = "16.0"
```

### Override via Command Line

You can override any property without editing code:

```bash
# Override platform and target
./gradlew test -Dplatform=android -DrunTarget=device

# Override device properties
./gradlew test -Dplatform=android \
  -Dandroid.deviceName="My Device" \
  -Dandroid.deviceUdid="ABC123" \
  -Dandroid.platformVersion="14"
```

### How Configuration Works

1. **TestConfig.java** - Contains all default values
2. **System Properties** - Can override defaults via `-D` flags
3. **AppiumConfig.java** - Uses TestConfig to create driver
4. **BaseTest** - Automatically uses AppiumConfig for setup

**Result:** One setup method, works for all platforms and targets! 🎯

## 🛠️ Utilities

Helper functions are available in `src/main/java/com/magnoliacollectivewellness/appium/utils/TestHelpers.java`:
- `waitForElement()` - Wait for element to be visible
- `waitForClickable()` - Wait for element to be clickable
- `tapWithRetry()` - Tap with retry logic
- `hideKeyboard()` - Hide keyboard (cross-platform)
- `takeScreenshot()` - Take screenshot with timestamp
- `getPlatformSelector()` - Get platform-specific selector

## 📊 Test Reports

Test results are displayed in the console. Screenshots are saved to `screenshots/` directory on test failures.

## 🔍 Troubleshooting

### Configuration Issues

**Problem:** Tests not using expected device/emulator

**Solution:** 
1. Check `TestConfig.java` - verify `PLATFORM` and `RUN_TARGET` flags
2. Check test output - configuration is displayed at start
3. Override via command line: `-Dplatform=android -DrunTarget=device`

### Appium Connection Issues

**Problem:** Cannot connect to Appium server

**Solution:**
1. Verify Appium server is running:
   ```bash
   appium --allow-cors
   ```
2. Check server URL in `TestConfig.java` (default: `http://127.0.0.1:4723`)
3. Verify port 4723 is not blocked

### Device Connection Issues

**Problem:** Device/emulator not detected

**Solution:**
1. Check device connection:
   ```bash
   # Android
   adb devices
   
   # iOS
   xcrun devicectl list devices
   ```
2. Verify UDID in `TestConfig.java` matches your device
3. For Android emulator, ensure it's running before tests

### Element Not Found

**Problem:** Test fails with "element not found"

**Solution:**
- Use Appium Inspector to find correct selectors
- Verify element IDs in the app match page objects
- Check if element is in a different screen/context
- Add explicit waits in page objects

### App Installation Issues

**Problem:** App not launching

**Solution:**
- Ensure APK/IPA path is correct in `TestConfig.java`
- Verify app package/bundle ID matches configuration
- Check app is installed on device/emulator
- Verify app permissions are granted

## 📚 Resources

### Documentation
- **[CONFIGURATION.md](CONFIGURATION.md)** - Detailed configuration guide
- [Appium Java Client Documentation](https://github.com/appium/java-client)
- [Appium Documentation](http://appium.io/docs/en/latest/)
- [JUnit 4 Documentation](https://junit.org/junit4/)
- [Selenium WebDriver Documentation](https://www.selenium.dev/documentation/)

### Key Files to Know

| File | Purpose |
|------|---------|
| `TestConfig.java` | ⭐ **Edit this** to change all test settings |
| `BaseTest.java` | Reusable setup/teardown for all tests |
| `AppiumConfig.java` | Creates driver based on TestConfig |
| `CONFIGURATION.md` | Detailed configuration guide |

## 🤝 Contributing

### Guidelines

1. **Extend BaseTest** - All new test classes should extend `BaseTest`
2. **Use Page Objects** - Follow Page Object Model pattern
3. **Edit TestConfig** - Add new properties to `TestConfig.java` for easy editing
4. **Descriptive Names** - Use clear test method names (e.g., `test01_LoginWithValidCredentials`)
5. **Documentation** - Update README and CONFIGURATION.md if adding features

### Adding New Configuration Properties

1. Add property to `TestConfig.java`:
   ```java
   public static final String MY_NEW_PROPERTY = getSystemProperty("myNewProperty", "defaultValue");
   ```

2. Use in `AppiumConfig.java` if needed for driver setup

3. Document in `CONFIGURATION.md`

## 📄 License

ISC
