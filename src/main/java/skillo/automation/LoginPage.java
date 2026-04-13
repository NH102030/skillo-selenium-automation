package skillo.automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Login page class using the Page Factory pattern.
 */
public class LoginPage extends BasePage {

    // Relative path to the login page
    private static final String LOGIN_URL = "/users/login";

    // Username input field
    @FindBy(css = "#defaultLoginFormUsername")
    private WebElement usernameField;

    // Password input field
    @FindBy(css = "#defaultLoginFormPassword")
    private WebElement passwordField;

    // Sign-in button
    @FindBy(css = "#sign-in-button")
    private WebElement signInButton;

    // Error message element (toast notification)
    @FindBy(css = ".toast-message")
    private WebElement errorMessage;

    /**
     * Constructor - initializes the driver and Page Factory.
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates the browser to the login page.
     */
    public void navigateToPage() {
        navigateTo(LOGIN_URL);
    }

    /**
     * Types the given username into the username field.
     */
    public void enterUsername(String username) {
        typeText(usernameField, username);
    }

    /**
     * Types the given password into the password field.
     */
    public void enterPassword(String password) {
        typeText(passwordField, password);
    }

    /**
     * Clicks the Sign In button.
     */
    public void clickSignIn() {
        click(signInButton);
    }

    /**
     * Performs the complete login flow: enters credentials and clicks Sign In.
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSignIn();
    }

    /**
     * Returns the text of the error message.
     */
    public String getErrorMessageText() {
        wait.until(ExpectedConditions.visibilityOf(errorMessage));
        return errorMessage.getText();
    }

    /**
     * Verifies if the Login page is loaded.
     */
    public boolean isUrlLoaded() {
        return isUrlPartLoaded(LOGIN_URL);
    }
}