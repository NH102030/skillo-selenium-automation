package skillo.automation;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ProfileTests extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;
    private ProfilePage profilePage;
    private boolean postCreated;

    @BeforeClass
    public void setupProfileTest() {
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        profilePage = new ProfilePage(driver);

        loginPage.navigateToPage();
        loginPage.login("123@mail.bg", "123@mail.bg");

        Assert.assertTrue(homePage.isUrlLoaded(), "Setup failed: Login was not successful!");
    }

    @Test(priority = 1, description = "Verify that the user can navigate to their profile page")
    public void testNavigateToProfile() {
        homePage.clickProfile();

        System.out.println("Step: Verifying Profile URL...");
        boolean isProfileUrlLoaded = profilePage.isUrlLoaded();
        Assert.assertTrue(isProfileUrlLoaded, "ERROR: Profile URL path '/users/' was not found!");
    }

    @Test(priority = 2, description = "Verify that the profile header count and visible gallery count increase after creating a new post")
    public void testProfilePostCountIncreasesAfterCreatingPost() {
        postCreated = false;
        homePage.clickProfile();
        Assert.assertTrue(profilePage.isUrlLoaded(), "ERROR: Could not open profile page.");

        int initialDisplayedCount = profilePage.getDisplayedPostCount();
        int initialGalleryCount = profilePage.getActualGalleryPostCount();

        System.out.println("Step: Initial header count is " + initialDisplayedCount);
        System.out.println("Step: Initial gallery count is " + initialGalleryCount);

        homePage.clickNewPost();
        Assert.assertTrue(profilePage.isPostFormLoaded(), "ERROR: Post form did not load.");

        String imagePath = "src/test/resources/laleta.jpg";
        profilePage.createPost(imagePath, "Profile count test");

        boolean isRedirected = homePage.isRedirectedAfterPost();
        Assert.assertTrue(isRedirected, "ERROR: The post was not created.");
        postCreated = true;

        homePage.clickProfile();
        Assert.assertTrue(profilePage.isUrlLoaded(), "ERROR: Could not return to profile page.");

        int updatedDisplayedCount = profilePage.getDisplayedPostCount();
        int updatedGalleryCount = profilePage.getActualGalleryPostCount();

        System.out.println("Step: Updated header count is " + updatedDisplayedCount);
        System.out.println("Step: Updated gallery count is " + updatedGalleryCount);

        Assert.assertEquals(updatedDisplayedCount, initialDisplayedCount + 1,
                "ERROR: The profile header post count did not increase by 1 after creating a post.");

        Assert.assertEquals(updatedGalleryCount, initialGalleryCount + 1,
                "ERROR: The profile gallery count did not increase by 1 after creating a post.");
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUpCreatedPost() {
        if (postCreated) {
            homePage.clickProfile();
            profilePage.deleteLastPost();
            postCreated = false;
        }
    }
}