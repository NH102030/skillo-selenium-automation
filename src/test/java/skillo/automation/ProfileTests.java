package skillo.automation;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ProfileTests extends BaseTest{
    private LoginPage loginPage;
    private HomePage homePage;
    private ProfilePage profilePage;

    @BeforeClass // Runs only once before all tests in this class
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

    @Test(priority = 2, dependsOnMethods = "testNavigateToProfile", description = "Verify that the profile shows the correct post count")
    public void testProfilePostCountVisibility() {

        int postCount = profilePage.getPostCount();
        System.out.println("Step: User has " + postCount + " posts.");
        Assert.assertTrue(postCount >= 0, "ERROR: Could not retrieve post count!");
    }

    @Test(priority = 3, dependsOnMethods = "testProfilePostCountVisibility", description = "Check if displayed post count matches the actual items in gallery")
    public void testProfilePostCountIntegrity() {
        System.out.println("Step: Checking data integrity on Profile Page.");
        int displayedCount = profilePage.getDisplayedPostCount();
        System.out.println("Step: UI says there are " + displayedCount + " posts.");

        int actualCount = profilePage.getActualGalleryPostCount();
        System.out.println("Step: Selenium found " + actualCount + " post elements in the gallery.");

        // If they don't match, the test fails and shows the exact difference
        Assert.assertEquals(actualCount, displayedCount,
                "DATA INTEGRITY ERROR: The header says " + displayedCount +
                        " posts, but the gallery actually contains " + actualCount + " items!");
    }
}
