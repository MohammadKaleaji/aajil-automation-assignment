package aajil.pages;

import aajil.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class PurchasePage extends BasePage {

    // ===== Locators for form fields =====
    private final By nameInput          = By.id("inputName");
    private final By addressInput       = By.id("address");
    private final By cityInput          = By.id("city");
    private final By stateInput         = By.id("state");
    private final By zipCodeInput       = By.id("zipCode");
    private final By cardTypeSelect     = By.id("cardType");
    private final By cardNumberInput    = By.id("creditCardNumber");
    private final By cardMonthInput     = By.id("creditCardMonth");
    private final By cardYearInput      = By.id("creditCardYear");
    private final By nameOnCardInput    = By.id("nameOnCard");
    private final By rememberMeCheckbox = By.id("rememberMe");
    private final By purchaseButton     = By.cssSelector("input[type='submit'][value='Purchase Flight']");

    // ===== Constructor =====
    public PurchasePage(WebDriver driver) {
        super(driver);
        // تأكد بسيط أننا على صفحة الشراء
        waitForElementVisible(nameInput);
    }

    /**
     * يكمّل عملية الشراء بالبيانات الممرّرة من الـ test.
     * يرجع لنا صفحة التأكيد.
     */
    public ConfirmationPage completePurchase(
            String fullName,
            String address,
            String city,
            String state,
            String zipCode,
            String cardTypeValue,   // "visa" أو "amex" أو "dinersclub"
            String cardNumber,
            String month,
            String year,
            String nameOnCard,
            boolean rememberMe
    ) {

        // تعبئة الحقول النصية
        type(nameInput, fullName);
        type(addressInput, address);
        type(cityInput, city);
        type(stateInput, state);
        type(zipCodeInput, zipCode);

        // اختيار نوع البطاقة من الـ <select>
        Select cardType = new Select(driver.findElement(cardTypeSelect));
        cardType.selectByValue(cardTypeValue);

        // بيانات البطاقة
        type(cardNumberInput, cardNumber);
        type(cardMonthInput, month);
        type(cardYearInput, year);
        type(nameOnCardInput, nameOnCard);

        // اختيار "Remember me" إذا مطلوب
        if (rememberMe) {
            click(rememberMeCheckbox);
        }

        // Submit
        click(purchaseButton);

        // ننتقل لصفحة التأكيد
        return new ConfirmationPage(driver);
    }

    /**
     * نسخة مختصرة: تشتري ببيانات ثابتة سريعة.
     * مفيدة للاختبار السريع في الـ tests.
     */
    public ConfirmationPage completePurchaseWithDefaultData() {
        return completePurchase(
                "John Test",              // fullName
                "123 Main St.",           // address
                "Anytown",                // city
                "CA",                     // state
                "12345",                  // zipCode
                "visa",                   // cardTypeValue (exactly like option value)
                "4111111111111111",       // cardNumber
                "11",                     // month
                "2028",                   // year
                "John Test",              // nameOnCard
                false                     // rememberMe
        );
    }
}
