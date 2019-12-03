package com.apriori.pageobjects.reports.pages.view.reports;

import com.apriori.pageobjects.reports.header.ReportsHeader;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputControls extends ReportsHeader {
    private final Logger logger = LoggerFactory.getLogger(InputControls.class);

    @FindBy(css = "label[title='Currency Code'] > div > div > div > a")
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

    private PageUtils pageUtils;
    private WebDriver driver;

    public InputControls(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    public InputControls checkUsdSelected() {
        if (!currentCurrencyElement.getAttribute("title").equals("USD")) {
            currentCurrencyElement.click();
            usdCurrencyOption.click();
        }
        return this;
    }

    public InputControls checkGbpSelected() {
        if (!currentCurrencyElement.getAttribute("title").equals("GBP")) {
            currentCurrencyElement.click();
            gbpCurrencyOption.click();
        }
        return this;
    }

    public AssemblyDetailsReport clickApplyAndOk() {
        pageUtils.waitForElementToAppear(applyButton);
        applyButton.click();
        pageUtils.waitForElementToAppear(okButton);
        okButton.click();
        return new AssemblyDetailsReport(driver);
    }
}
