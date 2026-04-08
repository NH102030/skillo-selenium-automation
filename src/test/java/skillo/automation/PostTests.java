package skillo.automation;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PostTests extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;
    private ProfilePage profilePage;

    @BeforeMethod
    public void setupPostTest() {

        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        profilePage = new ProfilePage(driver);

        loginPage.navigateToPage();
        loginPage.login("123@mail.bg", "123@mail.bg");

        Assert.assertTrue(homePage.isUrlLoaded(), "Setup failed: Could not login!");
    }

    @Test(priority = 1, description = "Verify user can upload a new post")
    public void testCreatePost() {

        homePage.clickNewPost();
        Assert.assertTrue(profilePage.isPostFormLoaded(), "Post form did not load.");

        String imagePath = "src/test/resources/laleta.jpg";
        profilePage.uploadImage(imagePath);

        System.out.println("Step: Writing a description...");
        profilePage.typeCaption("Automation Test Flowers");
        profilePage.clickSubmit();

        boolean isRedirected = homePage.isRedirectedAfterPost();
        Assert.assertTrue(isRedirected, "ERROR: The post was not sent!");
    }

    @Test(priority = 2, dependsOnMethods = "testCreatePost")
    public void testCleanupLastPost() {
        homePage.clickProfile();
        profilePage.deleteLastPost();
    }

}
