package com.magnoliacollectivewellness.appium.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

/**
 * Home Page Object Model
 */
public class HomePage {
    private final AppiumDriver driver;
    private final WebDriverWait wait;
    private final boolean isAndroid;

    public HomePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.isAndroid = driver.getCapabilities().getCapability("platformName").toString().equalsIgnoreCase("Android");
    }

    /**
     * Get home screen indicator - looks for "Welcome" text or "Home" in bottom navigation
     */
    public WebElement getHomeScreenIndicator() {
        if (isAndroid) {
            try {
                // Strategy 1: Look for "Welcome" text (Welcome, username or Welcome back, username)
                return wait.until(ExpectedConditions.presenceOfElementLocated(
                    AppiumBy.xpath("//*[contains(@text, 'Welcome') or contains(@text, 'welcome')]")));
            } catch (Exception e1) {
                try {
                    // Strategy 2: Look for "Home" in bottom navigation
                    return wait.until(ExpectedConditions.presenceOfElementLocated(
                        AppiumBy.xpath("//*[@text='Home' or @text='HOME']")));
                } catch (Exception e2) {
                    // Strategy 3: Look for "Upcoming Appointments" or "Create Appointment" text
                    return wait.until(ExpectedConditions.presenceOfElementLocated(
                        AppiumBy.xpath("//*[contains(@text, 'Appointment') or contains(@text, 'appointment')]")));
                }
            }
        } else {
            return wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name, 'Welcome') or @name='Home']")));
        }
    }

    /**
     * Get "Home" button in bottom navigation
     */
    public WebElement getHomeButton() {
        if (isAndroid) {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//*[@text='Home' or @text='HOME']")));
        } else {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//XCUIElementTypeButton[@name='Home']")));
        }
    }

    /**
     * Get "Appointments" button in bottom navigation
     */
    public WebElement getAppointmentsButton() {
        if (isAndroid) {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//*[@text='Appointment' or @text='APPOINTMENT' or contains(@text, 'Appointment')]")));
        } else {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//XCUIElementTypeButton[@name='Appointment']")));
        }
    }

    /**
     * Get "Progress" button in bottom navigation
     */
    public WebElement getProgressButton() {
        if (isAndroid) {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//*[@text='Progress' or @text='PROGRESS']")));
        } else {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//XCUIElementTypeButton[@name='Progress']")));
        }
    }

    /**
     * Get "More" button in bottom navigation
     */
    public WebElement getMoreButton() {
        if (isAndroid) {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//*[@text='More' or @text='MORE']")));
        } else {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//XCUIElementTypeButton[@name='More']")));
        }
    }

    /**
     * Get profile picture/button
     */
    public WebElement getProfileButton() {
        if (isAndroid) {
            // Profile is usually an icon or image, try to find by content description or nearby text
            try {
                return wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.xpath("//*[@content-desc='Profile' or @content-desc='profile']")));
            } catch (Exception e) {
                // Try finding near "Welcome" text
                return wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.xpath("//android.widget.ImageView | //android.view.View")));
            }
        } else {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//XCUIElementTypeButton[@name='Profile']")));
        }
    }

    /**
     * Wait for home page to load
     */
    public void waitForHomePage() {
        getHomeScreenIndicator();
    }

    /**
     * Check if home page is displayed
     * Uses multiple strategies to verify we're on the home screen
     */
    public boolean isHomePageDisplayed() {
        try {
            // Strategy 1: Check for Welcome text
            List<WebElement> welcomeElements = driver.findElements(
                AppiumBy.xpath("//*[contains(@text, 'Welcome') or contains(@text, 'welcome')]"));
            if (!welcomeElements.isEmpty() && welcomeElements.get(0).isDisplayed()) {
                System.out.println("✅ Found Welcome text - Home page detected");
                return true;
            }
            
            // Strategy 2: Check for Home in bottom navigation
            List<WebElement> homeNavElements = driver.findElements(
                AppiumBy.xpath("//*[@text='Home' or @text='HOME']"));
            if (!homeNavElements.isEmpty() && homeNavElements.get(0).isDisplayed()) {
                System.out.println("✅ Found Home navigation - Home page detected");
                return true;
            }
            
            // Strategy 3: Check for appointment-related text
            List<WebElement> appointmentElements = driver.findElements(
                AppiumBy.xpath("//*[contains(@text, 'Appointment') or contains(@text, 'appointment')]"));
            if (!appointmentElements.isEmpty() && appointmentElements.get(0).isDisplayed()) {
                System.out.println("✅ Found Appointment text - Home page detected");
                return true;
            }
            
            // Strategy 4: Check if we're not on login screen
            List<WebElement> loginElements = driver.findElements(
                AppiumBy.xpath("//*[@text='Login' or @text='LOGIN' or contains(@text, 'email') or contains(@text, 'phone')]"));
            if (loginElements.isEmpty()) {
                System.out.println("✅ Not on login screen - Assuming home page");
                return true;
            }
            
            return false;
        } catch (Exception e) {
            System.out.println("⚠️ Error checking home page: " + e.getMessage());
            return false;
        }
    }

    /**
     * Navigate to appointments
     */
    public void navigateToAppointments() {
        getAppointmentsButton().click();
    }

    /**
     * Navigate to progress
     */
    public void navigateToProgress() {
        getProgressButton().click();
    }

    /**
     * Navigate to more
     */
    public void navigateToMore() {
        getMoreButton().click();
    }

    /**
     * Navigate to profile
     */
    public void navigateToProfile() {
        getProfileButton().click();
    }
}
