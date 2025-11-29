package aajil.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    // We keep a single WebDriver instance for the whole run
    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            // Basic Chrome setup (feel free to toss in more switches)
            ChromeOptions options = new ChromeOptions();
            // If you need headless mode:
            // options.addArguments("--headless=new");

            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
