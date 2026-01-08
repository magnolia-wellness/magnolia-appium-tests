package com.magnoliacollectivewellness.appium.config;

/**
 * Test Configuration
 * 
 * ═══════════════════════════════════════════════════════════════════════════
 * 🎯 EASILY EDITABLE CONFIGURATION PROPERTIES
 * ═══════════════════════════════════════════════════════════════════════════
 * 
 * Edit the values below to configure your test execution:
 * 
 * 1. PLATFORM: Set to "android" or "ios"
 * 2. TARGET: Set to "device" or "emulator"
 * 3. Device properties: Edit device-specific settings below
 * 
 * You can also override these via system properties:
 *   -Dplatform=android -DrunTarget=device
 * 
 * ═══════════════════════════════════════════════════════════════════════════
 */

public class TestConfig {
    
    // ═══════════════════════════════════════════════════════════════════════
    // 🎯 PLATFORM CONFIGURATION
    // ═══════════════════════════════════════════════════════════════════════
    
    /**
     * Platform selection: "android" or "ios"
     * Can be overridden via system property: -Dplatform=android
     */
    public static final String PLATFORM = getSystemProperty("platform", "android");
    
    /**
     * Run target: "device" or "emulator"
     * Can be overridden via system property: -DrunTarget=emulator
     */
    public static final String RUN_TARGET = getSystemProperty("runTarget", "emulator");
    
    // ═══════════════════════════════════════════════════════════════════════
    // 🤖 ANDROID CONFIGURATION
    // ═══════════════════════════════════════════════════════════════════════
    
    // Android Device Properties
    public static final String ANDROID_DEVICE_NAME = getSystemProperty("android.deviceName", "Priya's M34");
    public static final String ANDROID_DEVICE_UDID = getSystemProperty("android.deviceUdid", "RZCW82C6BCK");
    public static final String ANDROID_DEVICE_PLATFORM_VERSION = getSystemProperty("android.platformVersion", "15");
    
    // Android Emulator Properties
    // Based on emulator: sdk_gphone64_x86_64 (Android 15)
    public static final String ANDROID_EMULATOR_NAME = getSystemProperty("android.emulatorName", "sdk_gphone64_x86_64");
    public static final String ANDROID_EMULATOR_UDID = getSystemProperty("android.emulatorUdid", "emulator-5554");
    public static final String ANDROID_EMULATOR_PLATFORM_VERSION = getSystemProperty("android.emulatorPlatformVersion", "15");
    
    // Android App Properties
    // Note: Dev build uses .dev suffix, release uses base package
    public static final String ANDROID_APP_PACKAGE = getSystemProperty("android.appPackage", "com.magnoliacollectivewellness.app.dev");
    public static final String ANDROID_APP_ACTIVITY = "com.magnoliacollectivewellness.app.MainActivity";
    public static final String ANDROID_APP_PATH = getSystemProperty("android.appPath", "./app/app-debug.apk");
    
    // ═══════════════════════════════════════════════════════════════════════
    // 🍎 iOS CONFIGURATION
    // ═══════════════════════════════════════════════════════════════════════
    
    // iOS Device Properties
    public static final String IOS_DEVICE_NAME = getSystemProperty("ios.deviceName", "iPhone");
    public static final String IOS_DEVICE_UDID = getSystemProperty("ios.deviceUdid", "auto");
    public static final String IOS_DEVICE_PLATFORM_VERSION = getSystemProperty("ios.devicePlatformVersion", "16.0");
    
    // iOS Simulator Properties
    public static final String IOS_SIMULATOR_NAME = getSystemProperty("ios.simulatorName", "iPhone 14");
    public static final String IOS_SIMULATOR_UDID = getSystemProperty("ios.simulatorUdid", "auto");
    public static final String IOS_SIMULATOR_PLATFORM_VERSION = getSystemProperty("ios.simulatorPlatformVersion", "16.0");
    
    // iOS App Properties
    public static final String IOS_BUNDLE_ID = "com.magnoliacollectivewellness.app";
    public static final String IOS_APP_PATH = getSystemProperty("ios.appPath", "./app/Magnolia.app");
    
    // ═══════════════════════════════════════════════════════════════════════
    // 🔧 COMMON CONFIGURATION
    // ═══════════════════════════════════════════════════════════════════════
    
    // Appium 1.x uses /wd/hub endpoint, Appium 2.x uses root /
    public static final String APPIUM_SERVER_URL = getSystemProperty("appiumServerUrl", "http://127.0.0.1:4723/wd/hub");
    public static final int COMMAND_TIMEOUT_SECONDS = Integer.parseInt(getSystemProperty("commandTimeout", "60"));
    public static final boolean NO_RESET = Boolean.parseBoolean(getSystemProperty("noReset", "false"));
    public static final boolean AUTO_GRANT_PERMISSIONS = Boolean.parseBoolean(getSystemProperty("autoGrantPermissions", "true"));
    
    // ═══════════════════════════════════════════════════════════════════════
    // 🎯 DYNAMIC PROPERTY GETTERS
    // ═══════════════════════════════════════════════════════════════════════
    
    /**
     * Get current platform (android or ios)
     */
    public static String getPlatform() {
        return PLATFORM.toLowerCase();
    }
    
    /**
     * Get current run target (device or emulator)
     */
    public static String getRunTarget() {
        return RUN_TARGET.toLowerCase();
    }
    
    /**
     * Check if platform is Android
     */
    public static boolean isAndroid() {
        return "android".equalsIgnoreCase(getPlatform());
    }
    
    /**
     * Check if platform is iOS
     */
    public static boolean isIOS() {
        return "ios".equalsIgnoreCase(getPlatform());
    }
    
    /**
     * Check if target is device
     */
    public static boolean isDevice() {
        return "device".equalsIgnoreCase(getRunTarget());
    }
    
    /**
     * Check if target is emulator/simulator
     */
    public static boolean isEmulator() {
        return "emulator".equalsIgnoreCase(getRunTarget());
    }
    
    /**
     * Get device name based on platform and target
     */
    public static String getDeviceName() {
        if (isAndroid()) {
            return isDevice() ? ANDROID_DEVICE_NAME : ANDROID_EMULATOR_NAME;
        } else {
            return isDevice() ? IOS_DEVICE_NAME : IOS_SIMULATOR_NAME;
        }
    }
    
    /**
     * Get UDID based on platform and target
     */
    public static String getUdid() {
        if (isAndroid()) {
            return isDevice() ? ANDROID_DEVICE_UDID : ANDROID_EMULATOR_UDID;
        } else {
            return isDevice() ? IOS_DEVICE_UDID : IOS_SIMULATOR_UDID;
        }
    }
    
    /**
     * Get platform version based on platform and target
     */
    public static String getPlatformVersion() {
        if (isAndroid()) {
            return isDevice() ? ANDROID_DEVICE_PLATFORM_VERSION : ANDROID_EMULATOR_PLATFORM_VERSION;
        } else {
            return isDevice() ? IOS_DEVICE_PLATFORM_VERSION : IOS_SIMULATOR_PLATFORM_VERSION;
        }
    }
    
    /**
     * Get app path based on platform
     */
    public static String getAppPath() {
        return isAndroid() ? ANDROID_APP_PATH : IOS_APP_PATH;
    }
    
    /**
     * Print all configuration values for easy review
     */
    public static void printConfiguration() {
        System.out.println("\n" + "═".repeat(80));
        System.out.println("📋 TEST CONFIGURATION");
        System.out.println("═".repeat(80));
        System.out.println("🎯 Platform: " + getPlatform().toUpperCase());
        System.out.println("🎯 Run Target: " + getRunTarget().toUpperCase());
        System.out.println("─".repeat(80));
        
        if (isAndroid()) {
            System.out.println("🤖 ANDROID CONFIGURATION:");
            System.out.println("   Device Name: " + getDeviceName());
            System.out.println("   UDID: " + getUdid());
            System.out.println("   Platform Version: " + getPlatformVersion());
            System.out.println("   App Package: " + ANDROID_APP_PACKAGE);
            System.out.println("   App Activity: " + ANDROID_APP_ACTIVITY);
            System.out.println("   App Path: " + getAppPath());
        } else {
            System.out.println("🍎 iOS CONFIGURATION:");
            System.out.println("   Device Name: " + getDeviceName());
            System.out.println("   UDID: " + getUdid());
            System.out.println("   Platform Version: " + getPlatformVersion());
            System.out.println("   Bundle ID: " + IOS_BUNDLE_ID);
            System.out.println("   App Path: " + getAppPath());
        }
        
        System.out.println("─".repeat(80));
        System.out.println("🔧 COMMON SETTINGS:");
        System.out.println("   Appium Server URL: " + APPIUM_SERVER_URL);
        System.out.println("   Command Timeout: " + COMMAND_TIMEOUT_SECONDS + " seconds");
        System.out.println("   No Reset: " + NO_RESET);
        System.out.println("   Auto Grant Permissions: " + AUTO_GRANT_PERMISSIONS);
        System.out.println("═".repeat(80) + "\n");
    }
    
    /**
     * Helper method to get system property with default value
     */
    private static String getSystemProperty(String key, String defaultValue) {
        return System.getProperty(key, defaultValue);
    }
}

