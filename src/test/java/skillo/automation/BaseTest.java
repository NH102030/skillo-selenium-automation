package skillo.automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * Base test class that manages the browser lifecycle.
 * All test classes should extend this class.
 */
public class BaseTest {

    public WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void cleanScreenshotsDirectory() {
        Path screenshotsDir = Paths.get("src/test/resources/screenshots");

        try {
            Files.createDirectories(screenshotsDir);

            Files.list(screenshotsDir)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to delete screenshot: " + path, e);
                        }
                    });

            System.out.println(">>> Old screenshots deleted before test suite.");
        } catch (IOException e) {
            throw new RuntimeException("Could not clean screenshots directory.", e);
        }
    }

    @BeforeClass
    public void setUp() {

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));

        System.out.println(">>> Driver initialized. Ready for Explicit Wait testing.");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("<<< Driver closed and session ended.");
        }
    }
}