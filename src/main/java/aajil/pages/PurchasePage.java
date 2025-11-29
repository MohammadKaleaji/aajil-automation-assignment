package aajil.pages;

import aajil.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.Random;

/**
 * Page Object لصفحة شراء التذكرة (purchase.php) في موقع BlazeDemo.
 */
public class PurchasePage extends BasePage {

    // ===== Locators for form fields =====
    private final By nameInput            = By.id("inputName");
    private final By addressInput         = By.id("address");
    private final By cityInput            = By.id("city");
    private final By stateInput           = By.id("state");
    private final By zipCodeInput         = By.id("zipCode");

    private final By cardTypeSelect       = By.id("cardType");
    private final By cardNumberInput      = By.id("creditCardNumber");
    private final By cardMonthInput       = By.id("creditCardMonth");
    private final By cardYearInput        = By.id("creditCardYear");
    private final By nameOnCardInput      = By.id("nameOnCard");
    private final By rememberMeCheckbox   = By.id("rememberMe");

    private final By purchaseButton       =
            By.cssSelector("input[type='submit'][value='Purchase Flight']");

    // ===== Constructor =====
    public PurchasePage(WebDriver driver) {
        super(driver);
        // تأكيد بسيط أننا فعلاً على صفحة الشراء
        waitForElementVisible(nameInput);
    }

    // ===== Helper class لتمثيل بيانات مستخدم وهمي =====
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

            this.fullName   = fullName;
            this.address    = address;
            this.city       = city;
            this.state      = state;
            this.zipCode    = zipCode;
            this.cardNumber = cardNumber;
            this.month      = month;
            this.year       = year;
        }
    }

    /**
     * ينشئ بيانات مستخدم وهمية (dummy) عشوائية في كل مرة.
     */
    private DummyUser generateRandomUser() {
        Random rnd = new Random();
        int id = rnd.nextInt(9999);

        String fullName   = "User " + id;
        String address    = "Street " + id;
        String city       = "City " + id;
        String state      = "ST";
        String zipCode    = String.valueOf(10000 + id);
        String cardNumber = "4111111111111111";       // Visa test number
        String month      = String.valueOf(1 + rnd.nextInt(12)); // 1..12
        String year       = String.valueOf(2025 + rnd.nextInt(5)); // 2025..2029

        return new DummyUser(fullName, address, city, state, zipCode,
                             cardNumber, month, year);
    }

    // ==================================================================
    // 1) نسخة مرنة: نمرّر البيانات من التست (تستخدمها لو حابب تتحكم يدويًا)
    // ==================================================================
    /**
     * يكمّل عملية الشراء بالبيانات المعطاة من التست.
     * يعود بنا إلى صفحة التأكيد ConfirmationPage.
     */
    public ConfirmationPage completePurchase(String fullName,
                                             String address,
                                             String city,
                                             String state,
                                             String zipCode,
                                             String cardTypeValue,   // "visa" / "amex" / "dinersclub"
                                             String cardNumber,
                                             String month,
                                             String year,
                                             String nameOnCard,
                                             boolean rememberMe) {

        // تعبئة الحقول النصية
        type(nameInput, fullName);
        type(addressInput, address);
        type(cityInput, city);
        type(stateInput, state);
        type(zipCodeInput, zipCode);

        // اختيار نوع البطاقة من <select>
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

        // ننتقل لصفحة التأكيد
        return new ConfirmationPage(driver);
    }

    // ==================================================================
    // 2) نسخة تعتمد على random dummy user في كل Run
    // ==================================================================
    /**
     * يملأ فورم الشراء ببيانات وهمية عشوائية في كل مرة
     * (مطلوبة في الـ assessment: dummy data generated randomly each time).
     */
    public ConfirmationPage completePurchaseWithRandomData() {
        DummyUser user = generateRandomUser();

        // نستخدم Visa دائمًا هنا (ممكن نخليها random لو حاب)
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
                false   // rememberMe
        );
    }

    // ==================================================================
    // 3) نسخة ثابتة (مفيدة أحياناً للتجارب السريعة / Debug)
    // ==================================================================
    /**
     * نسخة مختصرة ببيانات ثابتة – تستخدم للاختبارات السريعة.
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
                false
        );
    }
}
