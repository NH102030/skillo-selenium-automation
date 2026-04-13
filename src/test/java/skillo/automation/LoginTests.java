package skillo.automation;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class to verify the login process.
 */
public class LoginTests extends BaseTest {

    /**
     * Verify successful login with valid credentials.
     */
    @Test(priority = 1, description = "Verify that a user can login with valid credentials")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);

        System.out.println("Step: Navigating to Login Page...");
        loginPage.navigateToPage();

        System.out.println("Step: Entering credentials and clicking Sign In...");
        loginPage.login("123@mail.bg", "123@mail.bg");

        System.out.println("Step: Verifying the Home Page URL...");
        boolean isUrlCorrect = homePage.isUrlLoaded();
        Assert.assertTrue(isUrlCorrect, "The Home Page URL was NOT loaded after login!");

        System.out.println("Step: Checking if the Profile link is visible...");
        boolean isProfileVisible = homePage.isProfileLinkVisible();
        Assert.assertTrue(isProfileVisible, "The Profile link is NOT visible! Login failed.");

        System.out.println("SUCCESS: User logged in successfully!");
    }

    /**
     * Verify navigation links using @FindAll.
     */
    @Test(priority = 2, dependsOnMethods = "testSuccessfulLogin", description = "Verify navigation links count")
    public void testNavigationLinks() {
        HomePage homePage = new HomePage(driver);

        int linksCount = homePage.getNavigationLinksCount();
        System.out.println("Step: Found " + linksCount + " navigation links.");

        Assert.assertTrue(linksCount > 0, "No navigation links found on the Home Page!");
    }

    /**
     * Verify login fails with an invalid password.
     */
    @Test(priority = 3, description = "Login with invalid password should show error")
    public void testLoginWithInvalidPassword() {
        LoginPage loginPage = new LoginPage(driver);

        System.out.println("Step: Navigating to Login Page for negative test...");
        loginPage.navigateToPage();

        System.out.println("Step: Entering valid username but WRONG password...");
        loginPage.login("123@mail.bg", "wrong_password_123");

        String currentUrl = loginPage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/users/login"),
                "The user was redirected, but should have stayed on the Login page!");

        String errorText = loginPage.getErrorMessageText();
        System.out.println("Step: Error message caught: " + errorText);

        Assert.assertTrue(errorText.contains("Wrong") || errorText.contains("failed"),
                "The error message text was not as expected!");
    }

    /**
     * Verify that the user can logout successfully.
     */
    @Test(priority = 4, description = "Logout", dependsOnMethods = "testSuccessfulLogin")
    public void testLogout() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        System.out.println("Step: Logout Execution...");
        homePage.clickLogout();

        boolean isLoginPageLoaded = loginPage.isUrlLoaded();
        Assert.assertTrue(isLoginPageLoaded, "ERROR: User is not on the login page after Logout!");

        System.out.println("SUCCESS: User logged out successfully.");
    }
}