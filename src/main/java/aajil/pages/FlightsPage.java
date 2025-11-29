package aajil.pages;

import aajil.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FlightsPage extends BasePage {

    // Rows from the flights table
    private final By flightRows = By.cssSelector("table.table tbody tr");
    private final By chooseButtons = By.cssSelector("table.table tbody tr td input[type='submit']");

    public FlightsPage(WebDriver driver) {
        super(driver);
        // Make sure the flights table actually showed up
        waitForElementVisible(flightRows);
    }

    // How many flights are available now
    public int getNumberOfFlights() {
        List<WebElement> rows = driver.findElements(flightRows);
        return rows.size();
    }

    // Pick a flight by its order (1 = first flight)
    public PurchasePage chooseFlightByIndex(int flightSeq) {
        List<WebElement> buttons = driver.findElements(chooseButtons);

        if (buttons.isEmpty()) {
            throw new RuntimeException("No flights found for the selected route.");
        }

        if (flightSeq < 1 || flightSeq > buttons.size()) {
            throw new IllegalArgumentException(
                    "Invalid flight index: " + flightSeq + ". Available flights: " + buttons.size());
        }

        buttons.get(flightSeq - 1).click();
        return new PurchasePage(driver);
    }
}
