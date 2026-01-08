package com.magnoliacollectivewellness.appium.tests;

import com.magnoliacollectivewellness.appium.base.BaseTest;
import com.magnoliacollectivewellness.appium.config.TestConfig;
import com.magnoliacollectivewellness.appium.pages.LoginPage;
import com.magnoliacollectivewellness.appium.pages.OnboardingPage;
import com.magnoliacollectivewellness.appium.pages.OtpPage;
import com.magnoliacollectivewellness.appium.pages.SignupFlowPage;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Covers the onboarding/signup flow, OTP verification, and landing on the home screen.
 * 
 * Uses a dynamic questionnaire approach that handles questions as they appear,
 * rather than assuming a fixed order.
 */
public class SignupFlowTest extends BaseTest {
    private OnboardingPage onboardingPage;
    private SignupFlowPage signupFlowPage;
    private OtpPage otpPage;
    private LoginPage loginPage;

    @Override
    protected void initializePageObjects() {
        super.initializePageObjects();
        onboardingPage = new OnboardingPage(driver);
        signupFlowPage = new SignupFlowPage(driver);
        otpPage = new OtpPage(driver);
        loginPage = new LoginPage(driver);
    }

    @Test
    public void testOnboardingSignupFlow() throws InterruptedException {
        if (shouldSkipTest()) {
            return;
        }

        System.out.println("\n" + "═".repeat(60));
        System.out.println("🔢 === TEST: Onboarding & Signup Flow ===");
        System.out.println("🎯 Platform: " + TestConfig.getPlatform().toUpperCase());
        System.out.println("🎯 Target: " + TestConfig.getRunTarget().toUpperCase());
        System.out.println("═".repeat(60));

        // Step 1: Start onboarding
        System.out.println("\n📌 Step 1: Starting onboarding...");
        onboardingPage.startOnboarding();

        // Step 2: Complete questionnaire dynamically
        // This handles all questions (What brings you here, Race/Culture, Medical, HRT, Menopausal status)
        System.out.println("\n📌 Step 2: Completing questionnaire...");
        onboardingPage.completeOnboardingQuestionnaire();

        // Step 3: Complete signup form
        System.out.println("\n📌 Step 3: Filling signup form...");
        signupFlowPage.fillStep1("Priya", "Lalani");
        signupFlowPage.selectPreferredPronoun("She/Her");
        signupFlowPage.selectCountry("United States");
        
        // Step 4: Complete remaining signup steps
        System.out.println("\n📌 Step 4: Completing additional steps...");
        signupFlowPage.completeStep2();
        signupFlowPage.completeStep3();

        // Step 5: Submit OTP verification
        System.out.println("\n📌 Step 5: Verifying OTP...");
        otpPage.enterOtp("1234");
        otpPage.submitOtp();

        // Wait for navigation to home screen
        Thread.sleep(5000);

        // Verify home screen is displayed
        System.out.println("\n📌 Step 6: Verifying home screen...");
        assertTrue("Home screen should be displayed at the end of signup flow", 
            homePage.isHomePageDisplayed());
        
        System.out.println("\n" + "═".repeat(60));
        System.out.println("✅ === TEST PASSED: Onboarding & Signup Flow Complete ===");
        System.out.println("═".repeat(60) + "\n");
    }
}
