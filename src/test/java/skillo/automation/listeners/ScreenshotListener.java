package skillo.automation.listeners;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;
import skillo.automation.BaseTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotListener implements ITestListener {

    // Path to the folder where screenshots will be saved
    private static final String SCREENSHOTS_DIR = "src/test/resources/screenshots";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    @Override
    public void onTestFailure(ITestResult result) {
        // Get the test instance
        Object instance = result.getInstance();
        if (!(instance instanceof BaseTest)) {
            return;
        }

        // Access the driver from BaseTest
        WebDriver driver = ((BaseTest) instance).driver;
        if (driver == null) {
            return;
        }

        // Format the screenshot filename
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String fileName = result.getName() + "_" + timestamp + ".png";
        Path screenshotsDir = Paths.get(SCREENSHOTS_DIR);

        try {
            Files.createDirectories(screenshotsDir);

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path destination = screenshotsDir.resolve(fileName);
            Files.copy(srcFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Screenshot saved for failed test: " + destination.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
    }
}