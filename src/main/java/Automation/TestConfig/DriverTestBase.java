package Automation.TestConfig;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.time.Duration;

public class DriverTestBase {

        public static WebDriver driver;
        public static WebDriverWait wait;

        @BeforeClass
        public void beforeExecution() {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("start-maximized");

            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            driver.get("https://www.msccrociere.it");
        }


        @AfterClass
        public static void quitDriver() {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }


}
