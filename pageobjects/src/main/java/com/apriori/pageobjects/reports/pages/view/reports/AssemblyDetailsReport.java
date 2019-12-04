package com.apriori.pageobjects.reports.pages.view.reports;

import com.apriori.pageobjects.reports.header.ReportsHeader;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssemblyDetailsReport extends ReportsHeader {

    private final Logger logger = LoggerFactory.getLogger(AssemblyDetailsReport.class);

    @FindBy(xpath = "//span[contains(text(), 'Currency:')]/../../td[4]/span")
    private WebElement currentCurrency;

    @FindBy(xpath = "//span[contains(text(), 'GRAND TOTAL')]/../../td[34]/span")
    private WebElement capitalInvGrandTotal;

    private float capitalInvGrandTotalPreviousCurrency;

    private PageUtils pageUtils;
    private WebDriver driver;

    public AssemblyDetailsReport(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets Capital Investments Grand Total
     * @return current page object
     */
    public float getCapitalInvGrandTotal() {
        pageUtils.waitForElementToAppear(capitalInvGrandTotal);
        return Float.parseFloat(capitalInvGrandTotal.getText());
    }

    /**
     * Gets current currency setting
     * @return String
     */
    public String getCurrentCurrency() {
        return pageUtils.getElementText(currentCurrency);
    }

    public AssemblyDetailsReport waitForReportToAppear() {
        pageUtils.checkElementAttribute(currentCurrency, "innerText", "GBP");
        return this;
    }
}
