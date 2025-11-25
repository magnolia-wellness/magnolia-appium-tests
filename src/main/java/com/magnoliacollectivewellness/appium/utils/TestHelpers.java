package com.magnoliacollectivewellness.appium.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

/**
 * Helper utilities for Appium tests
 */
public class TestHelpers {
    private final AppiumDriver driver;
    private final boolean isAndroid;

    public TestHelpers(AppiumDriver driver) {
        this.driver = driver;
        this.isAndroid = driver.getCapabilities().getCapability("platformName").toString().equalsIgnoreCase("Android");
    }

    /**
     * Wait for element to be visible
     */
    public void waitForElement(WebElement element, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for element to be clickable
     */
    public void waitForClickable(WebElement element, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Tap on element with retry
     */
    public void tapWithRetry(WebElement element, int retries) throws InterruptedException {
        for (int i = 0; i < retries; i++) {
            try {
                waitForClickable(element, 10);
                element.click();
                return;
            } catch (Exception e) {
                if (i == retries - 1) throw e;
                Thread.sleep(1000);
            }
        }
    }

    /**
     * Scroll to element
     */
    public void scrollToElement(WebElement element) {
        // Appium handles scrolling automatically when finding elements
        // This is a placeholder for custom scroll logic if needed
    }

    /**
     * Hide keyboard
     */
    public void hideKeyboard() {
        try {
            if (isAndroid) {
                ((AndroidDriver) driver).hideKeyboard();
            } else {
                // iOS - tap Done button or tap outside
                try {
                    driver.findElement(io.appium.java_client.AppiumBy.xpath("//XCUIElementTypeButton[@name='Done']")).click();
                } catch (Exception e) {
                    // Keyboard might not be visible
                }
            }
        } catch (Exception e) {
            System.out.println("Keyboard not visible or already hidden");
        }
    }

    /**
     * Take screenshot
     */
    public String takeScreenshot(String filename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String path = "./screenshots/" + filename + "-" + timestamp + ".png";
        // Screenshot functionality would be implemented here
        return path;
    }

    /**
     * Get platform-specific selector
     */
    public String getPlatformSelector(String androidSelector, String iosSelector) {
        return isAndroid ? androidSelector : iosSelector;
    }
}

