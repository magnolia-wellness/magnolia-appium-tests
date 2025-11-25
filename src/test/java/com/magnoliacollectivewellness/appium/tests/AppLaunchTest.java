package com.magnoliacollectivewellness.appium.tests;

import com.magnoliacollectivewellness.appium.base.BaseTest;
import com.magnoliacollectivewellness.appium.config.TestConfig;
import io.appium.java_client.android.AndroidDriver;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * App Launch Test Cases
 * 
 * Extends BaseTest to get reusable setup/teardown and driver instance
 */
public class AppLaunchTest extends BaseTest {

    @Test
    public void test01_AppLaunchSuccessfully() throws InterruptedException {
        if (shouldSkipTest()) {
            return;
        }

        System.out.println("\n🔢 === TEST: App Launch ===");
        System.out.println("🎯 Platform: " + TestConfig.getPlatform().toUpperCase());
        System.out.println("🎯 Target: " + TestConfig.getRunTarget().toUpperCase());
        
        // Wait for app to launch
        Thread.sleep(3000);

        // Verify app is launched by checking package/bundle
        if (TestConfig.isAndroid()) {
            String currentPackage = ((AndroidDriver) driver).getCurrentPackage();
            assertEquals("App package should match", 
                TestConfig.ANDROID_APP_PACKAGE, currentPackage);
            System.out.println("✅ App launched successfully!");
            System.out.println("📱 Current package: " + currentPackage);
        } else {
            String bundleId = driver.getCapabilities().getCapability("bundleId").toString();
            assertEquals("App bundle ID should match", 
                TestConfig.IOS_BUNDLE_ID, bundleId);
            System.out.println("✅ App launched successfully!");
            System.out.println("📱 Current bundle ID: " + bundleId);
        }
    }

    @Test
    public void test02_VerifyDeviceCapabilities() {
        if (shouldSkipTest()) {
            return;
        }

        System.out.println("\n🔢 === TEST: Verify Device Capabilities ===");
        System.out.println("🎯 Platform: " + TestConfig.getPlatform().toUpperCase());
        System.out.println("🎯 Target: " + TestConfig.getRunTarget().toUpperCase());
        
        // Get device info
        String platformName = driver.getCapabilities().getCapability("platformName").toString();
        String deviceName = driver.getCapabilities().getCapability("deviceName").toString();
        String platformVersion = driver.getCapabilities().getCapability("platformVersion").toString();

        assertNotNull("Platform name should not be null", platformName);
        assertNotNull("Device name should not be null", deviceName);
        assertNotNull("Platform version should not be null", platformVersion);

        System.out.println("📱 Platform: " + platformName);
        System.out.println("📱 Device: " + deviceName);
        System.out.println("📱 Platform Version: " + platformVersion);
        System.out.println("✅ Device capabilities verified!");
    }
}

