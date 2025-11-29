package aajil.tests;

import aajil.core.DriverFactory;
import aajil.pages.HomePage;
import aajil.pages.FlightsPage;
import aajil.pages.PurchasePage;
import aajil.pages.ConfirmationPage;
import org.junit.jupiter.api.*;

import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PurchaseTest {

    private WebDriver driver;
    private final Random random = new Random();

    // cities as in the site
    private static final List<String> DEPARTURE_CITIES = Arrays.asList(
            "Paris", "Philadelphia", "Boston", "Portland", "San Diego", "Mexico City", "São Paolo"
    );

    private static final List<String> DESTINATION_CITIES = Arrays.asList(
            "Buenos Aires", "Rome", "London", "Berlin", "New York", "Dublin", "Cairo"
    );

    @BeforeEach
    public void setUp() {
        driver = DriverFactory.getDriver();
        driver.get("https://blazedemo.com");
    }

    @AfterEach
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    // ==============
    // purchaseEndToEnd
    // ==============
    /**
     * Main end-to-end flow required by the assessment.
     *
     * @param deptCity  departure city, if null → random valid value
     * @param desCity   destination city, if null → random valid value
     * @param flightSeq preferred flight sequence (1 = first flight).
     *                  if null → random valid value.
     *
     * @return ConfirmationPage to allow further checks if needed.
     *
     * Throws IllegalArgumentException for wrong inputs
     * and AssertionError if business validations fail.
     */
    private ConfirmationPage purchaseEndToEnd(String deptCity, String desCity, Integer flightSeq) {

        // ===== Resolve defaults (random values when parameter not provided) =====
        String from = (deptCity == null)
                ? DEPARTURE_CITIES.get(random.nextInt(DEPARTURE_CITIES.size()))
                : deptCity;

        String to = (desCity == null)
                ? DESTINATION_CITIES.get(random.nextInt(DESTINATION_CITIES.size()))
                : desCity;

        // ===== Input sanitization =====
        if (!DEPARTURE_CITIES.contains(from)) {
            throw new IllegalArgumentException("Invalid departure city: " + from);
        }
        if (!DESTINATION_CITIES.contains(to)) {
            throw new IllegalArgumentException("Invalid destination city: " + to);
        }
        if (from.equals(to)) {
            throw new IllegalArgumentException(
                    "Departure and destination cities cannot be the same: " + from);
        }

        // ===== Navigate: Home -> Flights =====
        HomePage home = new HomePage(driver);
        FlightsPage flightsPage = home.searchFlights(from, to);

        int flightsCount = flightsPage.getNumberOfFlights();
        if (flightsCount == 0) {
            throw new RuntimeException("No flights available from " + from + " to " + to);
        }

        // Flight sequence: if null → random, else validate
        int seq;
        if (flightSeq == null) {
            seq = 1 + random.nextInt(flightsCount);   // 1..flightsCount
        } else {
            if (flightSeq <= 0 || flightSeq > flightsCount) {
                throw new IllegalArgumentException(
                        "Invalid flight index: " + flightSeq +
                        ". Available flights: " + flightsCount);
            }
            seq = flightSeq;
        }

        // ===== Choose flight & purchase with random dummy data =====
        PurchasePage purchasePage = flightsPage.chooseFlightByIndex(seq);
        ConfirmationPage confirmationPage = purchasePage.completePurchaseWithRandomData();

        // ===== Validations on confirmation page =====
        String status = confirmationPage.getStatus();
        double amount = confirmationPage.getAmountValue();

        System.out.printf(
                "[purchaseEndToEnd] %s -> %s | flight #%d | status=%s | amount=%.2f%n",
                from, to, seq, status, amount
        );

        if (!"PendingCapture".equals(status)) {
            throw new AssertionError(
                    "Expected status 'PendingCapture' but found '" + status + "'");
        }

        if (amount <= 100.0) {
            throw new AssertionError(
                    "Expected amount > 100.0 but found " + amount);
        }

        return confirmationPage;
    }

    // ===========================
    // 2.3.2 – Required Test Calls
    // ===========================

    // 1) Boston, Berlin, 2
    @Test
    public void test_case1_Boston_Berlin_2() {
        purchaseEndToEnd("Boston", "Berlin", 2);
    }

    // 2) No inputs (all parameters random)
    @Test
    public void test_case2_allRandom() {
        purchaseEndToEnd(null, null, null);
    }

    // 3) Boston, Boston, 1  → should be invalid (same city)
    @Test
    public void test_case3_Boston_Boston_1_invalid() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> purchaseEndToEnd("Boston", "Boston", 1),
                "Same departure/destination should be rejected");
    }

    // 4) Paris, Berlin, 0 → should be invalid (flightSeq <= 0)
    @Test
    public void test_case4_Paris_Berlin_0_invalid() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> purchaseEndToEnd("Paris", "Berlin", 0),
                "Flight index 0 should be rejected");
    }

    // 5) Any inputs of your choice –  Mexico City → Dublin  1
    @Test
    public void test_case5_customRoute() {
        purchaseEndToEnd("Mexico City", "Dublin", 1);
    }
}
