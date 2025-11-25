package com.magnoliacollectivewellness.appium.tests;

import com.magnoliacollectivewellness.appium.base.BaseTest;
import com.magnoliacollectivewellness.appium.config.TestConfig;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

/**
 * Login Test Cases
 * 
 * Extends BaseTest to get reusable setup/teardown and driver instance
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginTest extends BaseTest {

    @Test
    public void test01_LoginWithValidCredentials() throws InterruptedException {
        if (shouldSkipTest()) {
            return;
        }

        System.out.println("\n🔢 === TEST: Login with Valid Credentials ===");
        System.out.println("🎯 Platform: " + TestConfig.getPlatform().toUpperCase());
        System.out.println("🎯 Target: " + TestConfig.getRunTarget().toUpperCase());
        
        // Test data
        String email = "priya.lalani@plenartech.com";
        String password = "Plenar@123";

        // Perform login
        loginPage.login(email, password);

        // Wait for navigation to home page
        Thread.sleep(5000); // Give more time for navigation and data loading

        // Verify home page is displayed
        boolean isHomePageDisplayed = homePage.isHomePageDisplayed();
        assertTrue("Home page should be displayed after successful login", isHomePageDisplayed);

        System.out.println("✅ Login test completed successfully!");
    }

    @Test
    public void test02_LoginWithInvalidCredentials() throws InterruptedException {
        if (shouldSkipTest()) {
            return;
        }

        System.out.println("\n🔢 === TEST: Login with Invalid Credentials ===");
        System.out.println("🎯 Platform: " + TestConfig.getPlatform().toUpperCase());
        System.out.println("🎯 Target: " + TestConfig.getRunTarget().toUpperCase());
        
        // Test data
        String email = "invalid@email.com";
        String password = "WrongPassword@123";

        // Perform login
        loginPage.login(email, password);

        // Wait for error message or navigation
        Thread.sleep(5000); // Give time for error to appear or navigation to complete

        // Verify error message is displayed OR we're still on login screen (both indicate failure)
        boolean isErrorDisplayed = loginPage.isErrorMessageDisplayed();
        boolean isStillOnLoginScreen = !homePage.isHomePageDisplayed();
        
        assertTrue("Error message should be displayed OR login should fail (still on login screen) for invalid credentials", 
            isErrorDisplayed || isStillOnLoginScreen);

        System.out.println("✅ Invalid credentials test completed!");
    }
}

