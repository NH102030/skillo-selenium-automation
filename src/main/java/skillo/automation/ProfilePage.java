package skillo.automation;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.util.List;

public class ProfilePage extends BasePage {

    @FindBy(name = "caption")
    private WebElement captionField;

    @FindBy(id = "create-post")
    private WebElement submitButton;

    @FindBy(css = ".image-preview")
    private WebElement imagePreview;

    @FindBy(css = ".post-img img")
    private List<WebElement> profilePostsElements;

    @FindBy(xpath = "//input[@type='file']")
    private WebElement postFileInput;

    @FindBy(xpath = "//div[contains(@class, 'post-modal')]")
    private WebElement postModallElement;

    // "Delete post"
    @FindBy(xpath = "//*[contains(text(), 'Delete post')]")
    private WebElement profileDeleteButton;

    // When "Delete" button is clicked, a confirmation appears
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

    public void deleteLastPost() {
        System.out.println("Step: Deleting the last (oldest) post from profile...");

        wait.until(driver -> !profilePostsElements.isEmpty());

        int totalPosts = profilePostsElements.size();
        WebElement oldestPost = profilePostsElements.get(totalPosts - 1);

        click(oldestPost);

        wait.until(ExpectedConditions.visibilityOf(postModallElement));

        click(profileDeleteButton);
        click(confirmYesButtonLocator);

        wait.until(ExpectedConditions.invisibilityOf(postModallElement));

        System.out.println("Success: The oldest post (at index " + (totalPosts - 1) + ") was deleted.");
    }

    /**
     * Extracts the numerical value from the "X posts" text.
     * Example: "27 posts" -> returns 27 as an integer.
     */
    public int getDisplayedPostCount() {
        wait.until(ExpectedConditions.visibilityOf(postsCountText));
        String text = postsCountText.getText();
        String numericText = text.replaceAll("[^0-9]", "");

        if (numericText.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(numericText);
    }

    /**
     * Returns the count of actual image elements rendered in the profile gallery.
     */
    public int getActualGalleryPostCount() {
        try {
            wait.until(driver -> !profilePostsElements.isEmpty());
        } catch (TimeoutException e) {
            return 0;
        }

        return profilePostsElements.size();
    }

    public WebElement getPostsCountElement() {
        return postsCountText;
    }

    /**
     * Uploads an image, fills the caption and submits the post form.
     */
    public void createPost(String imagePath, String caption) {
        uploadImage(imagePath);
        typeCaption(caption);
        clickSubmit();
    }
}