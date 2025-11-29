package aajil.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    // نستخدم instance واحدة من WebDriver
    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            // إعدادات بسيطة للكروم (تقدر تضيف خيارات لو حابب)
            ChromeOptions options = new ChromeOptions();
            // لو حابب تشغيل بدون واجهة:
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
