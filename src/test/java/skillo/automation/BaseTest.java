package skillo.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.time.Duration;

/**
 * Base test class that manages the browser lifecycle.
 * All test classes should extend this class.
 */
public class BaseTest {

    // CHANGE: Must be public so ScreenshotListener can access it
    public WebDriver driver;

    /**
     * Initial setup before running the tests in the class.
     * Uses @BeforeClass to open the browser only once for the entire test suite.
     */
    @BeforeClass
    public void setUp() {
         // 1. Automatic ChromeDriver setup according to your browser version
        WebDriverManager.chromedriver().setup();

        // 2. Initialize a new Chrome browser
        driver = new ChromeDriver();

        // 3. Maximize the browser window
        driver.manage().window().maximize();

        // 4. Set timeout for the page load (HTML itself)
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));

        System.out.println(">>> Driver initialized. Ready for Explicit Wait testing.");
    }

    /**
     * runs once after all tests in the class, regardless of success or failure.
     * Cleanup method to close the browser after all tests in the class are finished.
     */
    @AfterClass(alwaysRun = true)
    public void tearDown() {
        // 5. Close all windows and release resources
        if (driver != null) {
            driver.quit();
            System.out.println("<<< Driver closed and session ended.");
        }
    }

}