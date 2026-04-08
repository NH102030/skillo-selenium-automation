package skillo.automation;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

/**
 * Home page represented with the Page Factory pattern.
 */
public class HomePage extends BasePage {
    // Relative URL for the home page
    private static final String HOME_URL = "/posts/all";
    /**
     * Navigation links collected with @FindAll.
     */
    @FindAll({
            @FindBy(css = "a.nav-link"),
            @FindBy(css = ".navbar a")
    })
    private List<WebElement> navigationLinks;

    @FindBy(css = "#nav-link-profile")
    private WebElement profileLink;

    // The Logout icon
    @FindBy(css = ".fas.fa-sign-out-alt")
    private WebElement logoutButton;

    /**
     * STABLE SELECTOR for "New Post" button. Locating by href attribute instead of ID.
     */
    @FindBy(css = "a[href='/posts/create']")
    private WebElement newPostLink;

    // Post images on the home feed
    @FindBy(css = ".post-feed-img")
    private List<WebElement> postImages;

    // Heart icon (like button) inside the modal view
    @FindBy(xpath = "//app-post-modal//i[contains(@class, 'fa-heart')]")
    private WebElement modalLikeButton;

    // Numeric likes count element inside the modal
    @FindBy(xpath = "//app-post-modal//strong")
    private WebElement modalLikesCount;

    // List of likes count elements on the home feed
    @FindBy(xpath = "//app-post-detail//strong")
    private List<WebElement> feedLikesCount;

    // The main container of the opened modal (useful for waiting for the popup to exist)
    @FindBy(css = ".post-modal")
    private WebElement postModalContainer;

    // The first image in the feed (to ensure the feed is not empty/loaded)
    @FindBy(css = "app-post-detail:nth-child(1) .post-feed-img")
    private WebElement firstPostImage;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isUrlLoaded() {
        return isUrlPartLoaded(HOME_URL);
    }
    /**
     * Clicks on the 'New Post' button.
     */
    public void clickNewPost() {
        click(newPostLink);
    }
    /**
     * Clicks on the profile link after ensuring it is clickable.
     */
    public void clickProfile() {
        click(profileLink);
    }
    /**
     * Clicks on the Logout button.
     */
    public void clickLogout() {
        click(logoutButton);
    }

    /**
     * Checks if the profile link is visible (proof of login).
     */
    public boolean isProfileLinkVisible() {
        return isElementVisible(profileLink);
    }
    /**
     * Returns the number of navigation links found via @FindAll.
     * @return navigation link count
     */
    public int getNavigationLinksCount() {
        System.out.println("Checking navigation links count...");
        try {
            wait.until(driver -> !navigationLinks.isEmpty());
        } catch (Exception e) {
            return 0;
        }
        return navigationLinks.size();
    }
    /**
     * Checks if the user is correctly redirected after creating a post.
     */
    public boolean isRedirectedAfterPost() {
        System.out.println("Verifying redirection after post...");
        // Wait until the URL contains either the home feed or the profile path
        return wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/posts/all"),
                ExpectedConditions.urlContains("/users/")
        ));
    }

// --- Action Methods ---
    /**
     * Opens the first post and waits for the heart icon to appear.
     */
    public void openFirstPost() {
        // Wait for images to be present
        wait.until(driver -> !postImages.isEmpty());

        // Click the first available image
        wait.until(ExpectedConditions.elementToBeClickable(postImages.get(0))).click();

        // Wait for the like button to ensure modal is loaded
        wait.until(ExpectedConditions.elementToBeClickable(modalLikeButton));
    }
    /**
     * Closes the modal and waits for the heart icon to disappear.
     */
    public void closeModal() {
        Actions action = new Actions(driver);
        action.sendKeys(Keys.ESCAPE).perform();

        // Wait until modal content is gone
        wait.until(ExpectedConditions.invisibilityOf(modalLikeButton));
    }

// --- Data Extraction Methods ---

    /**
     * Wait for the first post on the feed to be visible.
     */
    public void waitForPostToAppear() {
        wait.until(ExpectedConditions.visibilityOf(firstPostImage));
    }

    /**
     * Wait for the modal image and the like count to be visible.
     */
    public void waitForModalImageToLoad() {
        // Wait for the modal container to appear
        wait.until(ExpectedConditions.visibilityOf(postModalContainer));
        // Wait for the like count element to be visible inside the modal
        wait.until(ExpectedConditions.visibilityOf(modalLikesCount));
    }
    /**
     * Extracts the number from the modal (e.g., from "7 likes" -> 7)
     */
    public int getModalLikesCount() {
        // Ensure the text is not empty before parsing
        wait.until(driver -> !modalLikesCount.getText().isEmpty());
        return Integer.parseInt(modalLikesCount.getText().replaceAll("[^0-9]", ""));
    }
    /**
     * Gets numeric likes from the background feed.
     */
    public int getFeedLikesCount() {
        // Wait for the list of feed likes to be present
        wait.until(ExpectedConditions.visibilityOfAllElements(feedLikesCount));
        String text = feedLikesCount.get(0).getText();
        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }
    /**
     * Clicks the heart and waits for the like count to change from its initial value.
     */
    public void clickLikeAndVerifyChange(int initialValue) {
        // 1. Click the heart icon
        wait.until(ExpectedConditions.elementToBeClickable(modalLikeButton)).click();
        System.out.println("DEBUG: Clicked the like button in modal.");

        // 2. Wait until the text is NO LONGER the initial value
        wait.until(ExpectedConditions.not(
                ExpectedConditions.textToBePresentInElement(modalLikesCount, String.valueOf(initialValue))
        ));
        System.out.println("DEBUG: Modal count changed from " + initialValue);
    }

}