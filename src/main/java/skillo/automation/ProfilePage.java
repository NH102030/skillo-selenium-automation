package skillo.automation;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;
import java.io.File;

public class ProfilePage extends BasePage {

     @FindBy(name = "caption")
    private WebElement captionField;

    @FindBy(id = "create-post")
    private WebElement submitButton;

    @FindBy(css = ".image-preview")
    private WebElement imagePreview;

    @FindBy(css = ".gallery-item")
    private List<WebElement> myPosts;

    @FindBy(css = ".post-img img")
    private List<WebElement> profilePostsElements;

    @FindBy(xpath = "//input[@type='file']")
    private WebElement postFileInput;

    @FindBy(xpath = "//div[contains(@class, 'post-modal')]")
    private WebElement postModallElement;

    // "Delete post"
    @FindBy(xpath = "//*[contains(text(), 'Delete post')]")
    private WebElement profileDeleteButton;

    // When "Delete" button is clicked, a CONFIRMATION appears
    @FindBy(xpath = "//button[contains(@class, 'btn-primary') and normalize-space()='Yes']")
    private WebElement confirmYesButtonLocator;

    // Locate the <li> element that contains the word "posts"
    @FindBy(xpath = "//li[contains(text(),'posts')]")
    private WebElement postsCountText;

    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Verifies if the Profile page is loaded by checking the URL path.
     */
    public boolean isUrlLoaded() {
        System.out.println("Step: Verifying if Profile URL is loaded...");
        try {
            // This waits up to the default timeout for the URL to contain '/users/'
            return wait.until(ExpectedConditions.urlContains("/users/"));
        } catch (TimeoutException e) {
            System.out.println("Error: The URL does not contain '/users/'. Current URL is: " + driver.getCurrentUrl());
            return false;
        }
    }

    public void uploadImage(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("File not found: " + file.getAbsolutePath());
        }
        postFileInput.sendKeys(file.getAbsolutePath());

        wait.until(ExpectedConditions.visibilityOf(imagePreview));
        wait.until(ExpectedConditions.visibilityOf(captionField));

        System.out.println("Success: Photo and caption field are now visible!");
    }

    public boolean isPostFormLoaded() {
        try {
            return wait.until(driver -> postFileInput.isEnabled());
        } catch (Exception e) {
            return false;
        }
    }

    public void typeCaption(String text) {
        typeText(captionField, text);
    }

    public void clickSubmit() {
        click(submitButton);
    }

    public int getPostCount() {
        System.out.println("Step: Checking posts count in profile...");
        return myPosts.size();
    }

    public void deleteLastPost() {
        System.out.println("Step: Deleting the last (oldest) post from profile...");

        try {
            // 1. Wait for the post list to be populated and not empty
            wait.until(driver -> !profilePostsElements.isEmpty());

            // 2. Get the total count of all posts
            int totalPosts = profilePostsElements.size();

            // 3. The index of the last element is always (count - 1)
            WebElement oldestPost = profilePostsElements.get(totalPosts - 1);

            // Before clicking, scroll to the element in case it is located further down the page
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", oldestPost);

            // 4. Wait for the oldest post to be clickable, then click it
            wait.until(ExpectedConditions.elementToBeClickable(oldestPost)).click();

            // 5. Deletion procedure
            // Wait for the post modal to appear
            wait.until(ExpectedConditions.visibilityOf(postModallElement));

            // Wait for the Delete button and click it
            wait.until(ExpectedConditions.elementToBeClickable(profileDeleteButton)).click();

            // Wait for the confirmation "Yes" button and click it
            wait.until(ExpectedConditions.elementToBeClickable(confirmYesButtonLocator)).click();

            // Wait for the modal to be fully hidden from the DOM
            wait.until(ExpectedConditions.invisibilityOf(postModallElement));

            System.out.println("Success: The oldest post (at index " + (totalPosts - 1) + ") was deleted.");

        } catch (Exception e) {
            System.out.println("Error while deleting the last post: " + e.getMessage());
        }
    }

    /**
     * Extracts the numerical value from the 'X posts' text.
     * Example: "27 posts" -> returns 27 as an integer.
     */
    public int getDisplayedPostCount() {
        // Wait until the text is visible to ensure page is loaded
        wait.until(ExpectedConditions.visibilityOf(postsCountText));
        String text = postsCountText.getText();

        // Regular expression to strip everything except digits
        String numericText = text.replaceAll("[^0-9]", "");

        if (numericText.isEmpty()) return 0;
        return Integer.parseInt(numericText);
    }

    /**
     * Returns the count of actual image elements rendered in the profile gallery.
     * Includes a wait to ensure the gallery has time to load.
     */
    public int getActualGalleryPostCount() {
        try {
            // Wait for at least one image to be present before counting
            // This prevents counting 0 if the page is still rendering images
            wait.until(driver -> !profilePostsElements.isEmpty());
        } catch (TimeoutException e) {
            // If no posts appear within the timeout, the count is effectively 0
            return 0;
        }
        return profilePostsElements.size();
    }

    public WebElement getPostsCountElement() {
        return postsCountText;
    }

}