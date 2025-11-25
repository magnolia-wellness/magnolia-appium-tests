package com.magnoliacollectivewellness.appium.config;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.time.Duration;

/**
 * Appium Configuration
 * Creates AppiumDriver instances based on TestConfig settings
 */
public class AppiumConfig {
    
    /**
     * Create and return AppiumDriver based on platform and target configuration
     */
    public static AppiumDriver createDriver() throws Exception {
        // Print configuration for visibility
        TestConfig.printConfiguration();
        
        URL appiumServerUrl = new URL(TestConfig.APPIUM_SERVER_URL);
        
        if (TestConfig.isAndroid()) {
            return createAndroidDriver(appiumServerUrl);
        } else {
            return createIOSDriver(appiumServerUrl);
        }
    }
    
    /**
     * Create Android driver
     */
    private static AndroidDriver createAndroidDriver(URL serverUrl) {
        System.out.println("\n🤖 === ANDROID DRIVER SETUP ===");
        System.out.println("🎯 Target: " + (TestConfig.isDevice() ? "REAL DEVICE" : "EMULATOR"));
        
        UiAutomator2Options options = new UiAutomator2Options()
            .setPlatformName("Android")
            .setAutomationName("UiAutomator2")
            .setDeviceName(TestConfig.getDeviceName())
            .setUdid(TestConfig.getUdid())
            .setPlatformVersion(TestConfig.getPlatformVersion())
            .setAppPackage(TestConfig.ANDROID_APP_PACKAGE)
            .setAppActivity(TestConfig.ANDROID_APP_ACTIVITY)
            .setNewCommandTimeout(Duration.ofSeconds(TestConfig.COMMAND_TIMEOUT_SECONDS))
            .setNoReset(TestConfig.NO_RESET)
            .setAutoGrantPermissions(TestConfig.AUTO_GRANT_PERMISSIONS);
        
        System.out.println("✅ Android capabilities configured:");
        System.out.println("   Device: " + TestConfig.getDeviceName());
        System.out.println("   UDID: " + TestConfig.getUdid());
        System.out.println("   Platform Version: " + TestConfig.getPlatformVersion());
        
        return new AndroidDriver(serverUrl, options);
    }
    
    /**
     * Create iOS driver
     */
    private static IOSDriver createIOSDriver(URL serverUrl) {
        System.out.println("\n🍎 === iOS DRIVER SETUP ===");
        System.out.println("🎯 Target: " + (TestConfig.isDevice() ? "REAL DEVICE" : "SIMULATOR"));
        
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("automationName", "XCUITest");
        capabilities.setCapability("deviceName", TestConfig.getDeviceName());
        
        // Set UDID only if not "auto"
        if (!"auto".equalsIgnoreCase(TestConfig.getUdid())) {
            capabilities.setCapability("udid", TestConfig.getUdid());
        }
        
        capabilities.setCapability("platformVersion", TestConfig.getPlatformVersion());
        capabilities.setCapability("bundleId", TestConfig.IOS_BUNDLE_ID);
        capabilities.setCapability("newCommandTimeout", TestConfig.COMMAND_TIMEOUT_SECONDS);
        capabilities.setCapability("noReset", TestConfig.NO_RESET);
        
        System.out.println("✅ iOS capabilities configured:");
        System.out.println("   Device: " + TestConfig.getDeviceName());
        System.out.println("   UDID: " + TestConfig.getUdid());
        System.out.println("   Platform Version: " + TestConfig.getPlatformVersion());
        
        return new IOSDriver(serverUrl, capabilities);
    }
}
