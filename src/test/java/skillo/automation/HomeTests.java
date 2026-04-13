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

    @Test(priority = 1, description = "Verify that like count is persisted after page refresh")
    public void testLikePersistsAfterRefresh() {
        homePage.waitForPostToAppear();

        int initialFeedLikes = homePage.getFeedLikesCount();
        System.out.println("DEBUG: [FEED] Initial likes: " + initialFeedLikes);

        homePage.openFirstPost();
        homePage.waitForModalImageToLoad();

        int initialModalLikes = homePage.getModalLikesCount();
        System.out.println("DEBUG: [MODAL] Initial likes: " + initialModalLikes);

        Assert.assertEquals(initialModalLikes, initialFeedLikes,
                "Initial mismatch: modal likes do not match feed likes.");

        int updatedModalLikes = homePage.clickLikeAndWaitForCountChange(initialModalLikes);
        System.out.println("DEBUG: [MODAL] Updated likes: " + updatedModalLikes);

        Assert.assertNotEquals(updatedModalLikes, initialModalLikes,
                "The modal like count did not change after clicking the like button.");

        homePage.closeModal();

        driver.navigate().refresh();
        homePage.waitForPostToAppear();

        int refreshedFeedLikes = homePage.getFeedLikesCount();
        System.out.println("DEBUG: [FEED] Likes after refresh: " + refreshedFeedLikes);

        Assert.assertEquals(refreshedFeedLikes, updatedModalLikes,
                "The like count was not persisted correctly after page refresh.");
    }
}