package aajil.pages;

import aajil.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ConfirmationPage extends BasePage {

    // Locators for summary table on confirmation page
    private final By statusCell  = By.xpath("//tr[td[text()='Status']]/td[2]");
    private final By amountCell  = By.xpath("//tr[td[text()='Amount']]/td[2]");

    public ConfirmationPage(WebDriver driver) {
        super(driver);
        // تأكد أن صفحة التأكيد ظهرت
        waitForElementVisible(statusCell);
    }

    // مثال: يرجع "PendingCapture"
    public String getStatus() {
        return getText(statusCell).trim();
    }

    // مثال: يرجع "555 USD"
    public String getAmountText() {
        return getText(amountCell).trim();
    }

    // يرجّع القيمة الرقمية فقط: 555.0
    public double getAmountValue() {
        String text = getAmountText();     // "555 USD"
        String numberPart = text.split(" ")[0];
        return Double.parseDouble(numberPart);
    }
}
