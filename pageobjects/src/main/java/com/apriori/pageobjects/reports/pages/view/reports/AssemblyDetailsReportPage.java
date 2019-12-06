package com.apriori.pageobjects.reports.pages.view.reports;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssemblyDetailsReportPage extends GenericReportPage {

    private final Logger logger = LoggerFactory.getLogger(AssemblyDetailsReportPage.class);

    @FindBy(xpath = "//span[contains(text(), 'Currency:')]/../../td[4]/span")
    private WebElement currentCurrency;

    @FindBy(xpath = "//tr[5]/td[10]/span")
    private WebElement reportTableCellElement;

    @FindBy(xpath = "//span[contains(text(), 'GRAND TOTAL')]/../../td[25]/span")
    private WebElement cycleTimeGrandTotalElement;

    @FindBy(xpath = "//span[contains(text(), 'GRAND TOTAL')]/../../td[28]/span")
    private WebElement piecePartCostGrandTotalElement;

    @FindBy(xpath = "//span[contains(text(), 'GRAND TOTAL')]/../../td[31]/span")
    private WebElement fullyBurdenedCostGrandTotalElement;

    @FindBy(xpath = "//span[contains(text(), 'GRAND TOTAL')]/../../td[34]/span")
    private WebElement capitalInvGrandTotalElement;

    private float capitalInvGrandTotalPreviousCurrency;

    private PageUtils pageUtils;
    private WebDriver driver;

    public AssemblyDetailsReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Generic method to get text from specified cell
     * @return String text of cell
     */
    public String getTableCellText(String rowIndex, String columnIndex) {
        By cellLocator = By.xpath(String.format("//tr[%s]/td[%s]/span", rowIndex, columnIndex));
        return pageUtils.getElementText(driver.findElement(cellLocator));
    }

    /**
     *
     * @return
     */
    public float getFullyBurdenedCostGrandTotal() {
        pageUtils.waitForElementToAppear(fullyBurdenedCostGrandTotalElement);
        return Float.parseFloat(pageUtils.getElementText(fullyBurdenedCostGrandTotalElement));
    }

    /**
     *
     * @return
     */
    public float getPiecePartCostGrandTotal() {
        pageUtils.waitForElementToAppear(piecePartCostGrandTotalElement);
        return Float.parseFloat(pageUtils.getElementText(piecePartCostGrandTotalElement));
    }

    /**
     *
     * @return
     */
    public float getCycleTimeGrandTotal() {
        pageUtils.waitForElementToAppear(cycleTimeGrandTotalElement);
        return Float.parseFloat(pageUtils.getElementText(cycleTimeGrandTotalElement));
    }

    /**
     * Gets Capital Investments Grand Total
     * @return current page object
     */
    public float getCapitalInvGrandTotal() {
        pageUtils.waitForElementToAppear(capitalInvGrandTotalElement);
        return Float.parseFloat(pageUtils.getElementText(capitalInvGrandTotalElement));
    }

    /**
     * Gets current currency setting
     * @return String
     */
    public String getCurrentCurrency() {
        return pageUtils.getElementText(currentCurrency);
    }

    public AssemblyDetailsReportPage waitForReportToAppear() {
        pageUtils.checkElementAttribute(currentCurrency, "innerText", "GBP");
        return this;
    }
}
