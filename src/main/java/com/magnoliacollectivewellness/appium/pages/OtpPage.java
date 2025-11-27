package com.magnoliacollectivewellness.appium.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * OTP verification helper.
 */
public class OtpPage {
    private final AppiumDriver driver;
    private final WebDriverWait wait;

    public OtpPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void enterOtp(String otp) {
        try {
            List<WebElement> otpFields = driver.findElements(AppiumBy.className("android.widget.EditText"));
            int index = 0;
            for (char digit : otp.toCharArray()) {
                if (index >= otpFields.size()) break;
                WebElement field = otpFields.get(index);
                field.click();
                Thread.sleep(200);
                field.clear();
                field.sendKeys(String.valueOf(digit));
                index++;
            }
        } catch (Exception e) {
            System.out.println("⚠️ Unable to enter OTP: " + e.getMessage());
        }
    }

    public void submitOtp() {
        try {
            WebElement verifyButton = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//*[(contains(@text,'Verify') or contains(@text,'VERIFY')) and @clickable='true']")));
            verifyButton.click();
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("⚠️ Could not click OTP verify button: " + e.getMessage());
        }
    }
}

