package aajil.pages;

import aajil.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class HomePage extends BasePage {

    private final By fromSelect = By.name("fromPort");
    private final By toSelect   = By.name("toPort");
    private final By findFlightsButton = By.cssSelector("input[type='submit']");

    public HomePage(WebDriver driver) {
        super(driver);
        waitForElementVisible(fromSelect);   // make sure page is loading
    }

    public FlightsPage searchFlights(String fromCity, String toCity) {

        wait.until(ExpectedConditions.presenceOfElementLocated(fromSelect));
        wait.until(ExpectedConditions.presenceOfElementLocated(toSelect));

        Select from = new Select(driver.findElement(fromSelect));
        from.selectByVisibleText(fromCity);

        Select to = new Select(driver.findElement(toSelect));
        to.selectByVisibleText(toCity);

        click(findFlightsButton);

        return new FlightsPage(driver);
    }
}
