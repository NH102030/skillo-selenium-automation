package skillo.automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Base page class for all page objects in the project.
 */
public class BasePage {

    /** Shared WebDriver instance. */
    protected WebDriver driver;

    /** Explicit wait used for custom conditions. */
    protected WebDriverWait wait;

    // The base URL of the application
    protected static final String BASE_URL = "http://training.skillo-bg.com:4300";

    /**
     * Constructor that sets up the driver and initializes Page Factory.
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        int timeoutInSeconds = 10;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, timeoutInSeconds), this);
    }

    /**
     * Navigates the browser to a specific URL by appending a suffix to the base URL.
     */
    protected void navigateTo(String urlSuffix) {
        driver.get(BASE_URL + urlSuffix);
    }

    /**
     * Universal method for URL verification.
     */
    protected boolean isUrlPartLoaded(String urlPart) {
        try {
            return wait.until(ExpectedConditions.urlContains(urlPart));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if a specific WebElement is visible.
     */
    protected boolean isElementVisible(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (Exception e) {
            System.out.println("Element not found or not visible.");
            return false;
        }
    }

    /**
     * Waits for the element to be visible and clears it before typing text.
     */
    protected void typeText(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Waits for the element to be clickable and performs a click.
     */
    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    /**
     * Returns the current URL from the browser.
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}