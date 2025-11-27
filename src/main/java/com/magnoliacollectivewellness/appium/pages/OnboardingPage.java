package com.magnoliacollectivewellness.appium.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
/**
 * Helper for onboarding/questionnaire flow.
 */
public class OnboardingPage {
    private final AppiumDriver driver;
    private final WebDriverWait wait;

    public OnboardingPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Tap the “GET STARTED” button on the welcome screen.
     */
    public void startOnboarding() {
        try {
            WebElement getStarted = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//*[(contains(@text,'GET STARTED') or contains(@text,'Get Started') or @content-desc='GET STARTED')]")));
            getStarted.click();
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("ℹ️ Get Started button not found: " + e.getMessage());
        }
    }

    /**
     * Complete onboarding questionnaire by selecting a valid option and tapping continue until Step 1 appears.
     */
    private int tapAttempt = 0;

    public void completeOnboardingQuestionnaire() throws InterruptedException {
        int attempts = 0;
        while (!isOnSignupStep() && attempts < 20) {
            selectQuestionOption();
            if (!isContinueEnabled()) {
                Thread.sleep(500);
                selectQuestionOption();
            }
            clickContinue();
            Thread.sleep(1500);
            attempts++;
        }
    }

    private void selectQuestionOption() {
        try {
            List<WebElement> optionTexts = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                AppiumBy.xpath("//android.widget.TextView[string-length(normalize-space(@text)) > 0 and not(contains(@text,'Step')) and not(contains(@text,'Continue')) and not(contains(@text,'Welcome'))]")));

            for (WebElement text : optionTexts) {
                if (!text.isDisplayed()) continue;
                text.click();
                System.out.println("✅ Selected onboarding option: " + text.getText());
                Thread.sleep(500);
                if (isContinueEnabled()) {
                    return;
                }
            }
            tapOnOptionArea();
        } catch (Exception e) {
            System.out.println("⚠️ Could not select onboarding option via element; tapping coordinates. " + e.getMessage());
            tapOnOptionArea();
        }
    }

    private void tapOnOptionArea() {
        try {
            int width = driver.manage().window().getSize().width;
            int height = driver.manage().window().getSize().height;
            int x = width / 2;
            int baseY = (int) (height * 0.45);
            int offset = (tapAttempt % 3) * (height / 10);
            int y = baseY + offset;

            java.util.Map<String, Object> args = new java.util.HashMap<>();
            args.put("x", x);
            args.put("y", y);
            driver.executeScript("mobile: tap", args);
            tapAttempt++;
            Thread.sleep(500);
            System.out.println("✅ Tapped screen at coordinates (" + x + "," + y + ")");
        } catch (Exception e) {
            System.out.println("⚠️ Coordinate tap failed: " + e.getMessage());
        }
    }

    private void clickContinue() {
        try {
            WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//*[(contains(@text,'Continue') or contains(@text,'CONTINUE')) and @clickable='true']")));
            continueBtn.click();
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("⚠️ Continue button not found: " + e.getMessage());
        }
    }

    private boolean isOnSignupStep() {
        try {
            WebElement title = driver.findElement(AppiumBy.xpath("//*[contains(@text,'Let\\'s tailor this for you') or contains(@text,'This is your space')]"));
            return title != null && title.isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }
}

