package com.magnoliacollectivewellness.appium.base;

import com.magnoliacollectivewellness.appium.config.AppiumConfig;
import com.magnoliacollectivewellness.appium.config.TestConfig;
import com.magnoliacollectivewellness.appium.pages.HomePage;
import com.magnoliacollectivewellness.appium.pages.LoginPage;
import io.appium.java_client.AppiumDriver;
import org.junit.After;
import org.junit.Before;

/**
 * Base Test Class
 * 
 * All test classes should extend this class to get:
 * - Reusable setup and teardown methods
 * - Pre-configured driver instance
 * - Pre-initialized page objects
 * - Platform and target configuration
 */
public abstract class BaseTest {
    
    protected AppiumDriver driver;
    protected LoginPage loginPage;
    protected HomePage homePage;
    
    /**
     * Setup method - called before each test
     * This method is reusable across all test classes
     */
    @Before
    public void setup() throws Exception {
        // Skip setup during build if Appium server is not available
        if (System.getProperty("skipAppiumTests", "false").equals("true")) {
            System.out.println("⏭️ Skipping Appium setup (skipAppiumTests=true)");
            return;
        }
        
        System.out.println("\n" + "═".repeat(80));
        System.out.println("🚀 TEST SETUP STARTING");
        System.out.println("═".repeat(80));
        
        // Create driver based on configuration
        driver = AppiumConfig.createDriver();
        
        // Initialize page objects
        initializePageObjects();
        
        // Wait for app to be ready
        Thread.sleep(2000);
        
        System.out.println("✅ Setup completed successfully!");
        System.out.println("═".repeat(80) + "\n");
    }
    
    /**
     * Initialize page objects
     * Override this method if you need additional page objects
     */
    protected void initializePageObjects() {
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
    }
    
    /**
     * Teardown method - called after each test
     * This method is reusable across all test classes
     */
    @After
    public void tearDown() {
        if (System.getProperty("skipAppiumTests", "false").equals("true")) {
            return;
        }
        
        if (driver != null) {
            try {
                System.out.println("\n" + "═".repeat(80));
                System.out.println("🧹 TEST TEARDOWN");
                System.out.println("═".repeat(80));
                System.out.println("🎯 Platform: " + TestConfig.getPlatform().toUpperCase());
                System.out.println("🎯 Target: " + TestConfig.getRunTarget().toUpperCase());
                
                driver.quit();
                
                System.out.println("✅ Driver session closed successfully!");
                System.out.println("═".repeat(80) + "\n");
            } catch (Exception e) {
                System.err.println("❌ Error during teardown: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Check if tests should be skipped
     */
    protected boolean shouldSkipTest() {
        return System.getProperty("skipAppiumTests", "false").equals("true");
    }
    
    /**
     * Get driver instance
     */
    protected AppiumDriver getDriver() {
        return driver;
    }
}

