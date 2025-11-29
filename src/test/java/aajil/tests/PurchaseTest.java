package aajil.tests;

import aajil.core.DriverFactory;
import aajil.pages.HomePage;
import aajil.pages.FlightsPage;
import aajil.pages.PurchasePage;
import aajil.pages.ConfirmationPage;
import org.openqa.selenium.WebDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class PurchaseTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = DriverFactory.getDriver();
        driver.get("https://blazedemo.com");
    }

    @AfterEach
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    // Test 1 – Fixed Inputs
    @Test
    public void test_Boston_Berlin_Flight2() {
        HomePage home = new HomePage(driver);
        FlightsPage flights = home.searchFlights("Boston", "Berlin");

        PurchasePage purchase = flights.chooseFlightByIndex(2);
        ConfirmationPage confirm = purchase.completePurchaseWithDefaultData();

        Assertions.assertEquals("PendingCapture", confirm.getStatus());
        Assertions.assertTrue(confirm.getAmountValue() > 100.0);
    }

    // Test 2 – Random Flight (أو أي مدخلات أخرى)
    @Test
    public void test_randomFlight() {
        HomePage home = new HomePage(driver);

        // استدعاء واحد فقط
        FlightsPage flights = home.searchFlights("Paris", "Berlin");

        PurchasePage purchase = flights.chooseFlightByIndex(2);
        ConfirmationPage confirm = purchase.completePurchaseWithDefaultData();

        Assertions.assertEquals("PendingCapture", confirm.getStatus());
        Assertions.assertTrue(confirm.getAmountValue() > 100.0);
    }

    // Test 3 – Invalid flight index
    @Test
    public void test_invalidFlightSequence() {
        HomePage home = new HomePage(driver);
        FlightsPage flights = home.searchFlights("Paris", "Berlin");

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> flights.chooseFlightByIndex(0),
                "Invalid flight index should throw IllegalArgumentException");
    }
}
