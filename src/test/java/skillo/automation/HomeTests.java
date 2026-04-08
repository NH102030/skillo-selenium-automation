package skillo.automation;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomeTests extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupHomeTest() {
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

        // Pre-condition: User must be logged in to interact with posts
        loginPage.navigateToPage();
        loginPage.login("123@mail.bg", "123@mail.bg");

        // Verify successful login
        Assert.assertTrue(homePage.isUrlLoaded(), "Setup failed: User not logged in!");
    }

    @Test(priority = 1, description = "Compare modal likes directly with feed likes using explicit wait")
    public void testLikeSynchronizationVisualProof() {
        // 1. Wait for feed to load
        homePage.waitForPostToAppear();

        // 2. Capture initial feed likes
        int feedLikes = homePage.getFeedLikesCount();
        System.out.println("DEBUG: [FEED] Initial likes: " + feedLikes);

        // 3. Open post and wait for modal
        homePage.openFirstPost();
        homePage.waitForModalImageToLoad();

        // 4. Click like and wait for the UI to update
        homePage.clickLikeAndVerifyChange(feedLikes);

        // 5. Capture what the modal shows
        int modalLikes = homePage.getModalLikesCount();
        System.out.println("DEBUG: [MODAL] Current likes after click: " + modalLikes);

        // 6. Compare directly.
        Assert.assertEquals(modalLikes, feedLikes, "Data mismatch: Modal likes do not match Feed likes!");

        homePage.closeModal();
    }

}