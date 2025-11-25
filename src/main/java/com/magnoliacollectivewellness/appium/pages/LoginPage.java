package com.magnoliacollectivewellness.appium.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

/**
 * Login Page Object Model
 * Handles welcome screen and login flow
 */
public class LoginPage {
    private final AppiumDriver driver;
    private final WebDriverWait wait;
    private final boolean isAndroid;

    public LoginPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.isAndroid = driver.getCapabilities().getCapability("platformName").toString().equalsIgnoreCase("Android");
    }

    /**
     * Handle welcome screen - click "ALREADY A MEMBER" button
     * Works for both Android and iOS
     */
    public void handleWelcomeScreen() {
        try {
            // Wait a bit for app to load
            Thread.sleep(2000);
            
            WebElement alreadyMemberButton;
            if (isAndroid) {
                // Android: Find by text attribute
                alreadyMemberButton = wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.xpath("//*[@text='ALREADY A MEMBER' or @text='Already a member' or contains(@text, 'ALREADY')]")));
            } else {
                // iOS: Find by name attribute
                alreadyMemberButton = wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.xpath("//XCUIElementTypeButton[@name='ALREADY A MEMBER' or @name='Already a member' or contains(@name, 'ALREADY')]")));
            }
            
            if (alreadyMemberButton != null && alreadyMemberButton.isDisplayed()) {
                System.out.println("✅ Found 'ALREADY A MEMBER' button - clicking...");
                alreadyMemberButton.click();
                Thread.sleep(2000); // Wait for navigation to login screen
                System.out.println("✅ Navigated to login screen");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("ℹ️ Interrupted while handling welcome screen");
        } catch (Exception e) {
            System.out.println("ℹ️ Welcome screen not found or already on login screen: " + e.getMessage());
        }
    }

    /**
     * Get email/phone input field (first screen of login)
     * Works for both Android and iOS
     */
    public WebElement getEmailOrPhoneInput() {
        if (isAndroid) {
            // Android: Find EditText - Compose OutlinedTextField renders as EditText
            // Try multiple strategies
            try {
                // Strategy 1: Find by placeholder text
                return wait.until(ExpectedConditions.presenceOfElementLocated(
                    AppiumBy.xpath("//android.widget.EditText[contains(@hint, 'email') or contains(@hint, 'phone') or contains(@hint, 'Email') or contains(@hint, 'Phone')]")));
            } catch (Exception e1) {
                try {
                    // Strategy 2: Find first EditText on screen
                    List<WebElement> editTexts = driver.findElements(AppiumBy.xpath("//android.widget.EditText"));
                    if (!editTexts.isEmpty()) {
                        return editTexts.get(0);
                    }
                } catch (Exception e2) {
                    // Strategy 3: Find by class name
                    return wait.until(ExpectedConditions.presenceOfElementLocated(
                        AppiumBy.className("android.widget.EditText")));
                }
            }
        } else {
            // iOS: Find text field
            try {
                // Strategy 1: Find by placeholder or value
                return wait.until(ExpectedConditions.presenceOfElementLocated(
                    AppiumBy.xpath("//XCUIElementTypeTextField[@placeholder='Email' or @placeholder='Phone' or contains(@value, '@')]")));
            } catch (Exception e1) {
                try {
                    // Strategy 2: Find first text field
                    List<WebElement> textFields = driver.findElements(AppiumBy.xpath("//XCUIElementTypeTextField"));
                    if (!textFields.isEmpty()) {
                        return textFields.get(0);
                    }
                } catch (Exception e2) {
                    // Strategy 3: Find by class name
                    return wait.until(ExpectedConditions.presenceOfElementLocated(
                        AppiumBy.className("XCUIElementTypeTextField")));
                }
            }
        }
        return null;
    }

    /**
     * Get "Enter Password" button (first screen of login)
     */
    public WebElement getEnterPasswordButton() {
        if (isAndroid) {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//*[@text='Enter Password' or @text='ENTER PASSWORD' or contains(@text, 'Password')]")));
        } else {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//XCUIElementTypeButton[@name='Enter Password']")));
        }
    }

    /**
     * Get password input field (second screen of login)
     * Works for both Android and iOS
     */
    public WebElement getPasswordInput() {
        if (isAndroid) {
            // Android: Find password field - Compose renders secure text field
            try {
                // Strategy 1: Find by placeholder
                return wait.until(ExpectedConditions.presenceOfElementLocated(
                    AppiumBy.xpath("//android.widget.EditText[contains(@hint, 'password') or contains(@hint, 'Password')]")));
            } catch (Exception e1) {
                try {
                    // Strategy 2: Find EditText (password field is usually the second one or has password input type)
                    List<WebElement> editTexts = driver.findElements(AppiumBy.xpath("//android.widget.EditText"));
                    // Password field is usually the last EditText or one with password input type
                    for (WebElement element : editTexts) {
                        String inputType = element.getAttribute("password");
                        if (inputType != null || element.getAttribute("class").contains("Password")) {
                            return element;
                        }
                    }
                    // If no password-specific found, return the last EditText
                    if (!editTexts.isEmpty()) {
                        return editTexts.get(editTexts.size() - 1);
                    }
                } catch (Exception e2) {
                    // Strategy 3: Find by class
                    return wait.until(ExpectedConditions.presenceOfElementLocated(
                        AppiumBy.className("android.widget.EditText")));
                }
            }
        } else {
            // iOS: Find secure text field
            try {
                // Strategy 1: Find by placeholder
                return wait.until(ExpectedConditions.presenceOfElementLocated(
                    AppiumBy.xpath("//XCUIElementTypeSecureTextField[@placeholder='Password' or @placeholder='password']")));
            } catch (Exception e1) {
                try {
                    // Strategy 2: Find first secure text field
                    List<WebElement> secureFields = driver.findElements(AppiumBy.xpath("//XCUIElementTypeSecureTextField"));
                    if (!secureFields.isEmpty()) {
                        return secureFields.get(0);
                    }
                } catch (Exception e2) {
                    // Strategy 3: Find by class name
                    return wait.until(ExpectedConditions.presenceOfElementLocated(
                        AppiumBy.className("XCUIElementTypeSecureTextField")));
                }
            }
        }
        return null;
    }

    /**
     * Get "Login" button (second screen of login)
     */
    public WebElement getLoginButton() {
        if (isAndroid) {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//*[@text='Login' or @text='LOGIN' or contains(@text, 'Login')]")));
        } else {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//XCUIElementTypeButton[@name='Login']")));
        }
    }

    /**
     * Get "Forgot Password?" link
     */
    public WebElement getForgotPasswordLink() {
        if (isAndroid) {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//*[@text='Forgot Password?' or @text='Forgot your password?' or contains(@text, 'Forgot')]")));
        } else {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Forgot Password?']")));
        }
    }

    /**
     * Get error message element
     * Error messages in Compose can be in various formats
     */
    public WebElement getErrorMessage() {
        try {
            if (isAndroid) {
                // Strategy 1: Look for error text with various patterns
                List<WebElement> errorElements = driver.findElements(
                    AppiumBy.xpath("//*[contains(@text, 'error') or contains(@text, 'Error') or contains(@text, '⚠') or contains(@text, 'invalid') or contains(@text, 'Invalid') or contains(@text, 'incorrect') or contains(@text, 'Incorrect') or contains(@text, 'failed') or contains(@text, 'Failed')]"));
                
                for (WebElement element : errorElements) {
                    if (element.isDisplayed()) {
                        return element;
                    }
                }
                
                // Strategy 2: Look for error container/card
                List<WebElement> errorContainers = driver.findElements(
                    AppiumBy.xpath("//android.view.View[contains(@content-desc, 'error')] | //android.widget.CardView"));
                
                for (WebElement container : errorContainers) {
                    if (container.isDisplayed()) {
                        // Try to find text inside the container
                        try {
                            WebElement errorText = container.findElement(
                                AppiumBy.xpath(".//android.widget.TextView"));
                            if (errorText != null && errorText.isDisplayed()) {
                                return errorText;
                            }
                        } catch (Exception e) {
                            return container;
                        }
                    }
                }
            } else {
                return driver.findElement(AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name, 'error')]"));
            }
        } catch (Exception e) {
            // No error message found
        }
        return null;
    }

    // Actions
    /**
     * Enter email or phone number (first step of login)
     */
    public void enterEmailOrPhone(String emailOrPhone) {
        try {
            WebElement inputField = getEmailOrPhoneInput();
            inputField.click();
            Thread.sleep(500);
            inputField.clear();
            inputField.sendKeys(emailOrPhone);
            System.out.println("✅ Entered email/phone: " + emailOrPhone);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Click "Enter Password" button to proceed to password screen
     */
    public void clickEnterPasswordButton() {
        try {
            WebElement enterPasswordBtn = getEnterPasswordButton();
            enterPasswordBtn.click();
            Thread.sleep(2000); // Wait for password screen to load
            System.out.println("✅ Clicked 'Enter Password' button");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Enter password (second step of login)
     */
    public void enterPassword(String password) {
        try {
            WebElement passwordField = getPasswordInput();
            passwordField.click();
            Thread.sleep(500);
            passwordField.clear();
            passwordField.sendKeys(password);
            System.out.println("✅ Entered password");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Hide/dismiss the keyboard
     */
    public void hideKeyboard() {
        try {
            if (isAndroid) {
                // Method 1: Try using hideKeyboard() method
                try {
                    ((AndroidDriver) driver).hideKeyboard();
                    System.out.println("✅ Keyboard hidden using hideKeyboard()");
                    Thread.sleep(500); // Wait for keyboard to hide
                    return;
                } catch (Exception e1) {
                    // Method 2: Tap on a safe area (logo/title area) to dismiss keyboard
                    try {
                        // Tap on the app logo or title area at the top
                        List<WebElement> topElements = driver.findElements(
                            AppiumBy.xpath("//android.widget.TextView[contains(@text, 'MAGNOLIA') or contains(@text, 'Welcome')]"));
                        if (!topElements.isEmpty() && topElements.get(0).isDisplayed()) {
                            topElements.get(0).click();
                            System.out.println("✅ Keyboard hidden by tapping on title area");
                            Thread.sleep(500);
                            return;
                        }
                    } catch (Exception e2) {
                        // Method 3: Tap on a non-interactive view
                        try {
                            // Find a safe view to tap (like the background card)
                            List<WebElement> views = driver.findElements(
                                AppiumBy.xpath("//android.view.View[@clickable='false']"));
                            if (!views.isEmpty()) {
                                views.get(0).click();
                                System.out.println("✅ Keyboard hidden by tapping on background");
                                Thread.sleep(500);
                            } else {
                                System.out.println("ℹ️ Could not find safe area to tap, continuing anyway");
                            }
                        } catch (Exception e3) {
                            System.out.println("ℹ️ Could not hide keyboard, continuing anyway");
                        }
                    }
                }
            } else {
                // iOS: Hide keyboard using multiple strategies
                try {
                    // Method 1: Use hideKeyboard() method
                    ((IOSDriver) driver).hideKeyboard();
                    System.out.println("✅ Keyboard hidden using hideKeyboard() (iOS)");
                    Thread.sleep(500);
                    return;
                } catch (Exception e1) {
                    // Method 2: Tap on a safe area (title/logo area)
                    try {
                        List<WebElement> topElements = driver.findElements(
                            AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name, 'MAGNOLIA') or contains(@name, 'Welcome')]"));
                        if (!topElements.isEmpty() && topElements.get(0).isDisplayed()) {
                            topElements.get(0).click();
                            System.out.println("✅ Keyboard hidden by tapping on title area (iOS)");
                            Thread.sleep(500);
                            return;
                        }
                    } catch (Exception e2) {
                        // Method 3: Tap outside the input field
                        try {
                            // Tap on a non-interactive element
                            List<WebElement> views = driver.findElements(
                                AppiumBy.xpath("//XCUIElementTypeOther[@enabled='true']"));
                            if (!views.isEmpty()) {
                                views.get(0).click();
                                System.out.println("✅ Keyboard hidden by tapping outside (iOS)");
                                Thread.sleep(500);
                            } else {
                                System.out.println("ℹ️ Could not find safe area to tap on iOS, continuing anyway");
                            }
                        } catch (Exception e3) {
                            System.out.println("ℹ️ Could not hide keyboard on iOS, continuing anyway");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("ℹ️ Error hiding keyboard: " + e.getMessage());
        }
    }

    /**
     * Tap login button
     */
    public void tapLoginButton() {
        // Hide keyboard first before trying to find and click Login button
        hideKeyboard();
        
        // Wait a bit for keyboard to fully hide
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        WebElement loginBtn = getLoginButton();
        loginBtn.click();
        System.out.println("✅ Clicked 'Login' button");
    }

    /**
     * Complete login flow: handle welcome screen, enter credentials, and login
     */
    public void login(String emailOrPhone, String password) {
        // Step 1: Handle welcome screen if present
        handleWelcomeScreen();
        
        // Step 2: Enter email/phone and click "Enter Password"
        enterEmailOrPhone(emailOrPhone);
        clickEnterPasswordButton();
        
        // Step 3: Enter password and click "Login"
        enterPassword(password);
        tapLoginButton();
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            WebElement errorElement = getErrorMessage();
            return errorElement != null && errorElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get error message text
     */
    public String getErrorMessageText() {
        if (isErrorMessageDisplayed()) {
            WebElement errorElement = getErrorMessage();
            return errorElement != null ? errorElement.getText() : "";
        }
        return "";
    }
}
