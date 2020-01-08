package com.apriori.pageobjects.reports.pages.view.reports;

import com.apriori.pageobjects.reports.header.ReportsPageHeader;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericReportPage extends ReportsPageHeader {

    private static Logger logger = LoggerFactory.getLogger(GenericReportPage.class);

    @FindBy(xpath = "//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='top-level']/div/a")
    private WebElement topLevelExportSet;

    @FindBy(xpath = "//label[@title='Currency Code']/div/div/div/a")
    private WebElement currentCurrencyElement;

    @FindBy(css = "li[title='USD'] > div > a")
    private WebElement usdCurrencyOption;

    @FindBy(css = "li[title='GBP'] > div > a")
    private WebElement gbpCurrencyOption;

    @FindBy(id = "apply")
    private WebElement applyButton;

    @FindBy(id = "ok")
    private WebElement okButton;

    @FindBy(id = "reset")
    private WebElement resetButton;

    @FindBy(id = "cancelButton")
    private WebElement cancelButton;

    @FindBy(id = "save")
    private WebElement saveButton;

    @FindBy(id = "loading")
    private WebElement loadingPopup;

    private WebDriver driver;
    private PageUtils pageUtils;


    public GenericReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Selects top level export set
     * @return current page object
     */
    public GenericReportPage selectTopLevelExportSet() {
        pageUtils.isPageLoaded(topLevelExportSet);
        pageUtils.waitForElementAndClick(topLevelExportSet);
        return this;
    }

    /**
     * Checks current currency selection, fixes if necessary
     * @param currency
     * @return current page object
     */
    public GenericReportPage checkCurrencySelected(String currency) {
        pageUtils.waitForElementToAppear(currentCurrencyElement);
        pageUtils.scrollWithJavaScript(currentCurrencyElement, true);
        if (!currentCurrencyElement.getAttribute("title").equals(currency)) {
            currentCurrencyElement.click();
            switch (currency) {
                case "USD":
                    usdCurrencyOption.click();
                    break;
                case "GBP":
                    gbpCurrencyOption.click();
                    break;
            }
        }
        return this;
    }

    /**
     * Clicks apply and ok
     * @return Assembly Details Report page object
     */
    public AssemblyDetailsReportPage clickApplyAndOk() {
        pageUtils.waitForElementToAppear(applyButton);
        applyButton.click();
        pageUtils.waitForElementToAppear(okButton);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        okButton.click();
        return new AssemblyDetailsReportPage(driver);
    }
}
