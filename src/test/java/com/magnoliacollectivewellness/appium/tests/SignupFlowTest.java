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

        System.out.println("\n🔢 === TEST: Onboarding & Signup Flow ===");
        System.out.println("🎯 Platform: " + TestConfig.getPlatform().toUpperCase());
        System.out.println("🎯 Target: " + TestConfig.getRunTarget().toUpperCase());

        onboardingPage.startOnboarding();
        onboardingPage.completeOnboardingQuestionnaire();

        signupFlowPage.fillStep1("Priya", "Lalani");
        signupFlowPage.selectPreferredPronoun("She/Her");
        signupFlowPage.selectCountry("United States");
        signupFlowPage.completeStep2();
        signupFlowPage.completeStep3();

        // Submit OTP
        otpPage.enterOtp("1234");
        otpPage.submitOtp();

        // Wait for navigation to home
        Thread.sleep(5000);

        assertTrue("Home screen should be displayed at the end of signup flow", 
            homePage.isHomePageDisplayed());
    }
}

