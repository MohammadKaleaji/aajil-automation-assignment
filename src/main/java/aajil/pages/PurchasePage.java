package aajil.pages;

import aajil.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.Random;

/**
 * Page Object purchase page
 */
public class PurchasePage extends BasePage {

    // ===== Locators for form fields =====
    private final By nameInput = By.id("inputName");
    private final By addressInput = By.id("address");
    private final By cityInput = By.id("city");
    private final By stateInput = By.id("state");
    private final By zipCodeInput = By.id("zipCode");

    private final By cardTypeSelect = By.id("cardType");
    private final By cardNumberInput = By.id("creditCardNumber");
    private final By cardMonthInput = By.id("creditCardMonth");
    private final By cardYearInput = By.id("creditCardYear");
    private final By nameOnCardInput = By.id("nameOnCard");
    private final By rememberMeCheckbox = By.id("rememberMe");

    private final By purchaseButton = By.cssSelector("input[type='submit'][value='Purchase Flight']");

    // ===== Constructor =====
    public PurchasePage(WebDriver driver) {
        super(driver);
        // confirmation we are in purchase page
        waitForElementVisible(nameInput);
    }

    // ===== Helper class used for dummy user data====
    private static class DummyUser {
        final String fullName;
        final String address;
        final String city;
        final String state;
        final String zipCode;
        final String cardNumber;
        final String month;
        final String year;

        DummyUser(String fullName,
                String address,
                String city,
                String state,
                String zipCode,
                String cardNumber,
                String month,
                String year) {

            this.fullName = fullName;
            this.address = address;
            this.city = city;
            this.state = state;
            this.zipCode = zipCode;
            this.cardNumber = cardNumber;
            this.month = month;
            this.year = year;
        }
    }

    /**
     * generating the dummy
     */
    private DummyUser generateRandomUser() {
        Random rnd = new Random();
        int id = rnd.nextInt(9999);

        String fullName = "User " + id;
        String address = "Street " + id;
        String city = "City " + id;
        String state = "ST";
        String zipCode = String.valueOf(10000 + id);
        String cardNumber = "4111111111111111"; // Visa test number
        String month = String.valueOf(1 + rnd.nextInt(12)); // 1..12
        String year = String.valueOf(2025 + rnd.nextInt(5)); // 2025..2029

        return new DummyUser(fullName, address, city, state, zipCode,
                cardNumber, month, year);
    }

    // ==================================================================
    // 1) Flexible flavor: data comes from the test when ya wanna control stuff
    // ==================================================================
    /**
     * Finishes the purchase using the test-provided data.
     * Brings us back to the ConfirmationPage.
     */
    public ConfirmationPage completePurchase(String fullName,
            String address,
            String city,
            String state,
            String zipCode,
            String cardTypeValue, // "visa" / "amex" / "dinersclub"
            String cardNumber,
            String month,
            String year,
            String nameOnCard,
            boolean rememberMe) {

        // Fill the text fields real quick
        type(nameInput, fullName);
        type(addressInput, address);
        type(cityInput, city);
        type(stateInput, state);
        type(zipCodeInput, zipCode);

        // Grab the card type from the <select>
        Select cardType = new Select(driver.findElement(cardTypeSelect));
        cardType.selectByValue(cardTypeValue);

        type(cardNumberInput, cardNumber);
        type(cardMonthInput, month);
        type(cardYearInput, year);
        type(nameOnCardInput, nameOnCard);

        if (rememberMe) {
            click(rememberMeCheckbox);
        }

        // Submit
        click(purchaseButton);

        // Bounce to the confirmation page
        return new ConfirmationPage(driver);
    }

    // ==================================================================
    // 2) Flavor that leans on a random dummy user every run
    // ==================================================================
    /**
     * Fills the purchase form with fresh dummy data each single time
     * (needed for the assessment: dummy data generated randomly each run).
     */
    public ConfirmationPage completePurchaseWithRandomData() {
        DummyUser user = generateRandomUser();

        // We stick with Visa here (could randomize it if ya feel like it)
        String cardTypeValue = "visa";

        return completePurchase(
                user.fullName,
                user.address,
                user.city,
                user.state,
                user.zipCode,
                cardTypeValue,
                user.cardNumber,
                user.month,
                user.year,
                user.fullName,
                false // rememberMe
        );
    }

    // ==================================================================
    // 3) Static flavor (handy for quick experiments / debug)
    // ==================================================================
    /**
     * Short version with fixed data – nice for quick smoke checks.
     */
    public ConfirmationPage completePurchaseWithDefaultData() {
        return completePurchase(
                "John Test",
                "123 Main St.",
                "Anytown",
                "CA",
                "12345",
                "visa",
                "4111111111111111",
                "11",
                "2028",
                "John Test",
                false);
    }
}
