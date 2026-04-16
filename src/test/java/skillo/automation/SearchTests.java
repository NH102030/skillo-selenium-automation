package skillo.automation;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SearchTests extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;
    private ProfilePage profilePage;

    @BeforeMethod
    public void setupSearchTest() {
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        profilePage = new ProfilePage(driver);

        loginPage.navigateToPage();
        loginPage.login("123@mail.bg", "123@mail.bg");

        Assert.assertTrue(homePage.isUrlLoaded(), "Setup failed: User not logged in!");
    }

    @Test(description = "Search for a user and compare profile post count with loaded gallery posts")
    public void testSearchUserAndCompareHeaderCountWithLoadedPosts() {
        String searchedUsername = "petyamar";

        homePage.searchUser(searchedUsername);
        homePage.openSearchedUser(searchedUsername);

        Assert.assertTrue(profilePage.isUrlLoaded(), "The searched user profile did not open.");

        profilePage.clickAllPostsTab();

        int headerPostCount = profilePage.getDisplayedPostCount();
        int loadedPostCount = profilePage.loadVisiblePostsUntilCountStopsGrowing(10);

        System.out.println("Step: Header post count is " + headerPostCount);
        System.out.println("Step: Loaded gallery post count is " + loadedPostCount);

        Assert.assertEquals(loadedPostCount, headerPostCount,
                "Mismatch: the profile header count does not match the loaded gallery posts.");
    }
}