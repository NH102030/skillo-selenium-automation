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
     * Stable selector for the "New Post" button.
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

    // The main container of the opened modal
    @FindBy(css = ".post-modal")
    private WebElement postModalContainer;

    // The first image in the feed
    @FindBy(css = "app-post-detail:nth-child(1) .post-feed-img")
    private WebElement firstPostImage;

    // The like count of the first post in the feed
    @FindBy(css = "app-post-detail:nth-child(1) strong")
    private WebElement firstPostLikesCount;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isUrlLoaded() {
        return isUrlPartLoaded(HOME_URL);
    }

    /**
     * Clicks on the "New Post" button.
     */
    public void clickNewPost() {
        click(newPostLink);
    }

    /**
     * Clicks on the profile link.
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
     * Checks if the profile link is visible.
     */
    public boolean isProfileLinkVisible() {
        return isElementVisible(profileLink);
    }

    /**
     * Returns the number of navigation links found via @FindAll.
     *
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
        return wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/posts/all"),
                ExpectedConditions.urlContains("/users/")
        ));
    }

    /**
     * Opens the first post and waits for the modal to appear.
     */
    public void openFirstPost() {
        wait.until(driver -> !postImages.isEmpty());
        click(postImages.get(0));
        wait.until(ExpectedConditions.visibilityOf(modalLikeButton));
    }

    /**
     * Waits for the first post on the feed to be visible.
     */
    public void waitForPostToAppear() {
        wait.until(ExpectedConditions.visibilityOf(firstPostImage));
    }

    /**
     * Waits for the modal image and the like count to be visible.
     */
    public void waitForModalImageToLoad() {
        wait.until(ExpectedConditions.visibilityOf(postModalContainer));
        wait.until(ExpectedConditions.visibilityOf(modalLikesCount));
    }

    /**
     * Closes the modal and waits for the heart icon to disappear.
     */
    public void closeModal() {
        Actions action = new Actions(driver);
        action.sendKeys(Keys.ESCAPE).perform();
        wait.until(ExpectedConditions.invisibilityOf(modalLikeButton));
    }

    /**
     * Extracts the number from the modal text.
     * Example: "7 likes" -> 7
     */
    public int getModalLikesCount() {
        wait.until(driver -> !modalLikesCount.getText().isEmpty());
        return Integer.parseInt(modalLikesCount.getText().replaceAll("[^0-9]", ""));
    }

    /**
     * Gets the numeric like count from the first post in the feed.
     */
    public int getFeedLikesCount() {
        wait.until(ExpectedConditions.visibilityOf(firstPostLikesCount));
        String text = firstPostLikesCount.getText();
        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }

    /**
     * Clicks the like button and waits for the modal like count to change.
     *
     * @param initialValue the like count before clicking
     * @return the updated modal like count
     */
    public int clickLikeAndWaitForCountChange(int initialValue) {
        click(modalLikeButton);
        wait.until(driver -> getModalLikesCount() != initialValue);

        int updatedLikes = getModalLikesCount();
        System.out.println("DEBUG: Modal likes changed from " + initialValue + " to " + updatedLikes);

        return updatedLikes;
    }
}