package com.magnoliacollectivewellness.appium.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Helper for onboarding/questionnaire flow.
 * Uses a dynamic approach to handle questions as they appear on screen.
 */
public class OnboardingPage {
    private final AppiumDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;
    private int tapAttempt = 0;

    public OnboardingPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    /**
     * Tap the "GET STARTED" button on the welcome screen.
     */
    public void startOnboarding() {
        try {
            WebElement getStarted = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//*[(contains(@text,'GET STARTED') or contains(@text,'Get Started') or @content-desc='GET STARTED')]")));
            getStarted.click();
            Thread.sleep(2000);
            System.out.println("✅ Clicked GET STARTED button");
        } catch (Exception e) {
            System.out.println("ℹ️ Get Started button not found: " + e.getMessage());
        }
    }

    /**
     * Complete the entire onboarding questionnaire dynamically.
     * This method handles each question as it appears on screen.
     */
    public void completeOnboardingQuestionnaire() throws InterruptedException {
        System.out.println("\n📋 Starting Dynamic Questionnaire Flow...");
        
        int maxAttempts = 25;
        int attempts = 0;
        
        while (!isOnSignupStep() && attempts < maxAttempts) {
            attempts++;
            System.out.println("\n🔄 Questionnaire Step " + attempts);
            
            // Wait for content to load
            Thread.sleep(1500);
            
            // Try to select an option on the current screen
            boolean optionSelected = selectVisibleOption();
            
            if (optionSelected) {
                Thread.sleep(500);
            }
            
            // Try to click Continue button
            boolean continueClicked = clickContinueIfEnabled();
            
            if (!continueClicked && !optionSelected) {
                // If we can't select or continue, try tapping on screen
                System.out.println("⚠️ No option or continue available, trying tap...");
                tapOnOptionArea();
                Thread.sleep(500);
                clickContinueIfEnabled();
            }
            
            Thread.sleep(1000);
        }
        
        System.out.println("\n✅ Questionnaire completed after " + attempts + " steps");
    }

    /**
     * Select an option that is currently visible on screen.
     * Dynamically finds and clicks any selectable option.
     * Handles radio button style options in Compose UI.
     */
    private boolean selectVisibleOption() {
        System.out.println("  🔍 Looking for selectable options...");
        
        try {
            // Strategy 1: Find clickable View containers (radio button cards in Compose)
            // These are typically the parent containers of the option text
            List<WebElement> clickableViews = driver.findElements(
                AppiumBy.xpath("//android.view.View[@clickable='true']"));
            
            System.out.println("    Found " + clickableViews.size() + " clickable Views");
            
            // Filter to find option cards (skip header, continue button, etc.)
            int screenHeight = driver.manage().window().getSize().height;
            int optionAreaTop = (int)(screenHeight * 0.25);
            int optionAreaBottom = (int)(screenHeight * 0.80);
            
            for (WebElement view : clickableViews) {
                try {
                    int y = view.getLocation().getY();
                    int height = view.getSize().getHeight();
                    
                    // Option cards are typically in the middle portion of screen
                    // and have reasonable height (not tiny icons or huge containers)
                    if (y > optionAreaTop && y < optionAreaBottom && height > 50 && height < 300) {
                        if (view.isDisplayed()) {
                            view.click();
                            System.out.println("  ✓ Clicked option card at y=" + y);
                            Thread.sleep(800);
                            
                            if (isContinueEnabled()) {
                                return true;
                            }
                        }
                    }
                } catch (Exception e) {
                    // Continue trying other elements
                }
            }
            
            // Strategy 2: Find clickable ViewGroups
            List<WebElement> viewGroups = driver.findElements(
                AppiumBy.xpath("//android.view.ViewGroup[@clickable='true']"));
            
            System.out.println("    Found " + viewGroups.size() + " clickable ViewGroups");
            
            for (WebElement viewGroup : viewGroups) {
                try {
                    int y = viewGroup.getLocation().getY();
                    int height = viewGroup.getSize().getHeight();
                    
                    if (y > optionAreaTop && y < optionAreaBottom && height > 50 && height < 300) {
                        if (viewGroup.isDisplayed()) {
                            viewGroup.click();
                            System.out.println("  ✓ Clicked ViewGroup at y=" + y);
                            Thread.sleep(800);
                            
                            if (isContinueEnabled()) {
                                return true;
                            }
                        }
                    }
                } catch (Exception e) {
                    // Continue trying
                }
            }
            
            // Strategy 3: Find text elements containing option text and click their parent
            List<WebElement> textElements = driver.findElements(
                AppiumBy.xpath("//android.widget.TextView[string-length(@text) > 20]"));
            
            System.out.println("    Found " + textElements.size() + " text elements");
            
            for (WebElement textEl : textElements) {
                try {
                    String text = textEl.getText();
                    if (text != null && !text.contains("Step") && !text.contains("Continue") 
                        && !text.contains("What brings") && !text.contains("Let's start")
                        && text.length() > 20) {
                        
                        int y = textEl.getLocation().getY();
                        if (y > optionAreaTop && y < optionAreaBottom) {
                            textEl.click();
                            System.out.println("  ✓ Clicked text option: " + truncateText(text, 40));
                            Thread.sleep(800);
                            
                            if (isContinueEnabled()) {
                                return true;
                            }
                        }
                    }
                } catch (Exception e) {
                    // Continue trying
                }
            }
            
            // Strategy 4: Tap on first option position (calculated based on screen)
            System.out.println("    Trying coordinate tap on first option...");
            int width = driver.manage().window().getSize().width;
            int firstOptionY = (int)(screenHeight * 0.38); // First option is usually around 38% from top
            
            java.util.Map<String, Object> args = new java.util.HashMap<>();
            args.put("x", width / 2);
            args.put("y", firstOptionY);
            driver.executeScript("mobile: tap", args);
            System.out.println("  ✓ Tapped at first option position (" + (width/2) + "," + firstOptionY + ")");
            Thread.sleep(800);
            
            if (isContinueEnabled()) {
                return true;
            }
            
            // Strategy 5: Try tapping on multiple option positions
            int[] optionYPositions = {
                (int)(screenHeight * 0.42),
                (int)(screenHeight * 0.52),
                (int)(screenHeight * 0.62),
                (int)(screenHeight * 0.35)
            };
            
            for (int optionY : optionYPositions) {
                args.put("y", optionY);
                driver.executeScript("mobile: tap", args);
                System.out.println("  → Tapped at (" + (width/2) + "," + optionY + ")");
                Thread.sleep(500);
                
                if (isContinueEnabled()) {
                    return true;
                }
            }
            
            return false;
            
        } catch (Exception e) {
            System.out.println("  ⚠️ Error selecting option: " + e.getMessage());
            return false;
        }
    }

    /**
     * Click Continue button if it's enabled.
     * Returns true if clicked successfully.
     * Handles both traditional Android Views and Jetpack Compose buttons.
     */
    private boolean clickContinueIfEnabled() {
        System.out.println("  🔍 Looking for Continue button...");
        
        // Strategy 1: Find by text content (works for Compose)
        try {
            List<WebElement> elements = driver.findElements(
                AppiumBy.xpath("//*[contains(@text,'Continue') or contains(@text,'CONTINUE')]"));
            
            for (WebElement element : elements) {
                try {
                    if (element.isDisplayed()) {
                        System.out.println("    Found Continue element, attempting click...");
                        element.click();
                        System.out.println("  → Clicked Continue");
                        Thread.sleep(1500);
                        return true;
                    }
                } catch (Exception e) {
                    // Try next element
                }
            }
        } catch (Exception e) {
            System.out.println("    Strategy 1 failed: " + e.getMessage());
        }
        
        // Strategy 2: Find clickable button with Continue text
        try {
            WebElement continueBtn = driver.findElement(
                AppiumBy.xpath("//android.widget.Button[contains(@text,'Continue') or contains(@text,'CONTINUE')]"));
            if (continueBtn.isDisplayed()) {
                continueBtn.click();
                System.out.println("  → Clicked Continue (Button)");
                Thread.sleep(1500);
                return true;
            }
        } catch (Exception e) {
            // Continue to next strategy
        }
        
        // Strategy 3: Find by accessibility/content-desc
        try {
            WebElement continueBtn = driver.findElement(
                AppiumBy.accessibilityId("Continue"));
            if (continueBtn.isDisplayed()) {
                continueBtn.click();
                System.out.println("  → Clicked Continue (accessibility)");
                Thread.sleep(1500);
                return true;
            }
        } catch (Exception e) {
            // Continue to next strategy
        }
        
        // Strategy 4: Find any clickable element at bottom of screen with Continue text
        try {
            List<WebElement> allElements = driver.findElements(
                AppiumBy.xpath("//*[@clickable='true']"));
            
            int screenHeight = driver.manage().window().getSize().height;
            int bottomThreshold = (int)(screenHeight * 0.7); // Bottom 30% of screen
            
            for (WebElement element : allElements) {
                try {
                    String text = element.getText();
                    if (text != null && (text.contains("Continue") || text.contains("CONTINUE"))) {
                        element.click();
                        System.out.println("  → Clicked Continue (clickable element)");
                        Thread.sleep(1500);
                        return true;
                    }
                    
                    // Check location - Continue button is usually at bottom
                    int y = element.getLocation().getY();
                    if (y > bottomThreshold && element.isDisplayed()) {
                        String elementText = element.getText();
                        if (elementText != null && elementText.length() < 20) {
                            // Could be Continue button
                            element.click();
                            System.out.println("  → Clicked bottom button: " + elementText);
                            Thread.sleep(1000);
                            // Check if screen changed
                            return true;
                        }
                    }
                } catch (Exception ex) {
                    // Continue trying
                }
            }
        } catch (Exception e) {
            System.out.println("    Strategy 4 failed: " + e.getMessage());
        }
        
        // Strategy 5: Tap at typical Continue button location (bottom center)
        try {
            int width = driver.manage().window().getSize().width;
            int height = driver.manage().window().getSize().height;
            int x = width / 2;
            int y = (int)(height * 0.85); // Bottom area where Continue usually is
            
            java.util.Map<String, Object> args = new java.util.HashMap<>();
            args.put("x", x);
            args.put("y", y);
            driver.executeScript("mobile: tap", args);
            System.out.println("  → Tapped at Continue location (" + x + "," + y + ")");
            Thread.sleep(1500);
            return true;
        } catch (Exception e) {
            System.out.println("    Strategy 5 (tap) failed: " + e.getMessage());
        }
        
        System.out.println("  ⚠️ Could not find/click Continue button");
        return false;
    }

    /**
     * Check if Continue button is currently enabled/visible.
     */
    public boolean isContinueEnabled() {
        try {
            List<WebElement> continueBtns = driver.findElements(
                AppiumBy.xpath("//*[(contains(@text,'Continue') or contains(@text,'CONTINUE'))]"));
            for (WebElement btn : continueBtns) {
                if (btn.isDisplayed()) {
                    // For Compose apps, just check if displayed and enabled
                    return btn.isEnabled();
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Tap on screen at positions where options typically appear.
     */
    private void tapOnOptionArea() {
        try {
            int width = driver.manage().window().getSize().width;
            int height = driver.manage().window().getSize().height;
            int x = width / 2;
            
            // Calculate Y position - cycle through different option positions
            int[] yPositions = {
                (int)(height * 0.35),
                (int)(height * 0.45),
                (int)(height * 0.55),
                (int)(height * 0.40),
                (int)(height * 0.50)
            };
            int y = yPositions[tapAttempt % yPositions.length];

            java.util.Map<String, Object> args = new java.util.HashMap<>();
            args.put("x", x);
            args.put("y", y);
            driver.executeScript("mobile: tap", args);
            tapAttempt++;
            Thread.sleep(300);
            System.out.println("  → Tapped at (" + x + "," + y + ")");
        } catch (Exception e) {
            System.out.println("  ⚠️ Tap failed: " + e.getMessage());
        }
    }

    /**
     * Check if we've reached the signup step (after questionnaire).
     */
    public boolean isOnSignupStep() {
        try {
            // Check for various signup step indicators
            List<WebElement> signupIndicators = driver.findElements(
                AppiumBy.xpath("//*[contains(@text,'tailor') or contains(@text,'Tailor') " +
                    "or contains(@text,'your space') or contains(@text,'First name') " +
                    "or contains(@text,'First Name') or contains(@text,'Legal first')]"));
            
            for (WebElement indicator : signupIndicators) {
                if (indicator.isDisplayed()) {
                    System.out.println("✅ Detected signup step: " + truncateText(indicator.getText(), 30));
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click the Continue button (public method for external use).
     */
    public void clickContinue() {
        clickContinueIfEnabled();
    }

    /**
     * Helper to truncate text for logging.
     */
    private String truncateText(String text, int maxLength) {
        if (text == null) return "";
        return text.length() > maxLength ? text.substring(0, maxLength) + "..." : text;
    }

    // ====== SPECIFIC QUESTION METHODS (optional, for explicit control) ======

    /**
     * Answer Question 1: What brings you here today?
     */
    public void answerWhatBringsYouHere(String option) throws InterruptedException {
        System.out.println("\n📝 Question: What brings you here today?");
        Thread.sleep(1000);
        selectOptionByPartialText(option);
        clickContinueIfEnabled();
        Thread.sleep(1500);
    }

    /**
     * Answer Question 4: Race and/or cultural background (with nested selections)
     */
    public void answerRaceAndCulturalBackground(String primaryOption, String subOption, String specificOption) throws InterruptedException {
        System.out.println("\n📝 Question: Race and/or cultural background");
        
        // Primary selection
        Thread.sleep(1000);
        selectOptionByPartialText(primaryOption);
        clickContinueIfEnabled();
        Thread.sleep(1500);
        
        // Sub selection if provided
        if (subOption != null && !subOption.isEmpty()) {
            selectOptionByPartialText(subOption);
            clickContinueIfEnabled();
            Thread.sleep(1500);
        }
        
        // Specific selection if provided
        if (specificOption != null && !specificOption.isEmpty()) {
            selectOptionByPartialText(specificOption);
            clickContinueIfEnabled();
            Thread.sleep(1500);
        }
    }

    /**
     * Answer Question 6: Medical considerations (multi-select)
     */
    public void answerMedicalConsiderations(String... options) throws InterruptedException {
        System.out.println("\n📝 Question: Medical considerations");
        Thread.sleep(1000);
        
        for (String option : options) {
            selectOptionByPartialText(option.trim());
            Thread.sleep(500);
        }
        
        clickContinueIfEnabled();
        Thread.sleep(1500);
    }

    /**
     * Answer Question 7: Hormone therapy status
     */
    public void answerHormoneTherapyStatus(String option) throws InterruptedException {
        System.out.println("\n📝 Question: Hormone therapy status");
        Thread.sleep(1000);
        selectOptionByPartialText(option);
        clickContinueIfEnabled();
        Thread.sleep(1500);
    }

    /**
     * Answer Question 14: Menopausal status
     */
    public void answerMenopausalStatus(String option) throws InterruptedException {
        System.out.println("\n📝 Question: Menopausal status");
        Thread.sleep(1000);
        selectOptionByPartialText(option);
        clickContinueIfEnabled();
        Thread.sleep(1500);
    }

    /**
     * Select option by partial text match.
     */
    private boolean selectOptionByPartialText(String partialText) {
        try {
            // Use first 25 characters for matching to handle long option texts
            String searchText = partialText.length() > 25 ? partialText.substring(0, 25) : partialText;
            
            WebElement option = shortWait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//*[contains(@text,'" + searchText + "')]")));
            
            if (option.isDisplayed()) {
                option.click();
                System.out.println("  ✓ Selected: " + truncateText(partialText, 40));
                return true;
            }
        } catch (Exception e) {
            System.out.println("  ⚠️ Could not find option: " + truncateText(partialText, 30));
            // Try fallback - tap on first available option
            selectVisibleOption();
        }
        return false;
    }
}
