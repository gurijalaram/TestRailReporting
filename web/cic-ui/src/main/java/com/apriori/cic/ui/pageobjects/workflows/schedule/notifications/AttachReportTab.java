package com.apriori.cic.ui.pageobjects.workflows.schedule.notifications;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Attach Report tab in Notification part during workflow creation or modification
 */
public class AttachReportTab extends NotificationsPart {

    @FindBy(xpath = "//div[@tab-number='4']//div[@tab-number='2']//div[contains(@class, 'layout-vert-fluid-center ')]//div[@class='widget-content widget-dropdown']")
    private WebElement reportNameDropDownElement;

    @FindBy(css = "#CIC_EmptyReport_MU-1_label-3 > span")
    private WebElement emptyReportLbl;

    @FindBy(xpath = "//div//span[@class='arrow-up']")
    private WebElement ddlArrowUp;

    @FindBy(xpath = "//div[contains(@class, 'ss-open')]//div[.='Single Part Reports']")
    private WebElement reportNameDdlGroup;

    @FindBy(xpath = "//div[@tab-number='4']//div[@tab-number='2']//span[.='Report Configuration']")
    private WebElement reportConfigurationLbl;

    public AttachReportTab(WebDriver driver) {
        super(driver);
    }

    /**
     * Select Report Name
     *
     * @return current class object
     */
    public AttachReportTab selectReportName() {
        pageUtils.waitUntilDropdownOptionsLoaded(getReportNameDropdownElement().findElement(By.tagName("select")));
        pageUtils.waitForElementAndClick(getReportNameDropdownElement());
        pageUtils.waitForElementAndClick(By.xpath(String.format(OPTIONS_CONTAINS_TEXT, workFlowData.getNotificationsData().getReportName())));
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return this;
    }

    /**
     * Select currency code
     *
     * @return AttachReport object
     */
    public AttachReportTab selectCurrencyCode() {
        if (!getCurrencyCodeDdl().getText().equals("USD")) {
            pageUtils.waitForElementToAppear(getCurrencyCodeDdl());
            pageUtils.waitForElementAndClick(getCurrencyCodeDdl());
            pageUtils.waitForElementAndClick(By.xpath(String.format(OPTIONS_CONTAINS_TEXT, workFlowData.getNotificationsData().getReportCurrencyCode())));
            pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        }
        return this;
    }

    /**
     * select cost rounding
     *
     * @return AttachReport object
     */
    public AttachReportTab selectCostRounding() {
        pageUtils.waitForElementAppear(reportConfigurationLbl);
        pageUtils.waitForElementAndClick(getCostRoundingDdl());
        pageUtils.waitForElementAndClick(By.xpath(String.format(OPTIONS_CONTAINS_TEXT, workFlowData.getNotificationsData().getReportCostRounding())));
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return this;
    }

    /**
     * Getter for Empty report not defined label element
     *
     * @return WebElement
     */
    public WebElement getEmptyReportLbl() {
        return emptyReportLbl;
    }


    /**
     * Getter for Cost metric element
     *
     * @return WebElement
     */
    public WebElement getCostMetricDdl() {
        WebElement costMetricElement = null;
        if (workFlowData.getNotificationsData().getReportName().equals("DFM Multiple Components Summary [CIR]")) {
            pageUtils.waitForElementAppear(reportConfigurationLbl);
            WebElement currencyTextBoxElement = getAttachReportTextFields().stream()
                .filter(webElement -> webElement.getAttribute("value").equals("Cost Metric"))
                .findFirst()
                .get();
            costMetricElement = driver.findElement(with(By.xpath("//div[@tab-number='4']//div[@tab-number='2']//div[contains(@class, 'tw-flex-row')]//div[@class='widget-content widget-dropdown']"))
                .toRightOf(currencyTextBoxElement));
        }
        return costMetricElement;
    }

    /**
     * Getter for Cost rounding element
     *
     * @return WebElement
     */
    public WebElement getCostRoundingDdl() {
        WebElement currencyTextBoxElement = getAttachReportTextFields().stream()
            .filter(webElement -> webElement.getAttribute("value").equals("Cost Rounding"))
            .findFirst()
            .get();
        return driver.findElement(with(By.xpath("//div[@tab-number='4']//div[@tab-number='2']//div[contains(@class, 'tw-flex-row')]//div[@class='widget-content widget-dropdown']"))
            .toRightOf(currencyTextBoxElement));
    }

    /**
     * Getter for Currency Code element
     *
     * @return WebElement
     */
    public WebElement getCurrencyCodeDdl() {
        WebElement currencyTextBoxElement = getAttachReportTextFields().stream()
            .filter(webElement -> webElement.getText().equals("Curreny Code"))
            .findFirst()
            .get();

        return driver.findElement(with(By.xpath("//div[@tab-number='4']//div[@tab-number='2']//div[contains(@class, 'tw-flex-row')]//div[@class='widget-content widget-dropdown']"))
            .toLeftOf(currencyTextBoxElement));
    }

    /**
     * get the list of standard mappings rows
     *
     * @return list of standard mappings rows
     */
    public List<WebElement> getAttachReportDropdownFields() {
        return driver.findElements(By.xpath("//div[@tab-number='4']//div[@tab-number='2']//div[contains(@class, 'tw-flex-row')]//div[@class='widget-content widget-dropdown']"));
    }

    /**
     * get all the text fields from notitifaction - attach report tab
     *
     * @return - list of webelements
     */
    public List<WebElement> getAttachReportTextFields() {
        String xpathLocator = "//div[@tab-number='4']//div[@tab-number='2']//div[contains(@class, 'tw-flex-row')]//div[@class='widget-content widget-textbox']//input[@disabled='disabled']";
        pageUtils.waitForElementsToAppear(By.xpath(xpathLocator));
        return driver.findElements(By.xpath(xpathLocator));
    }

    /**
     * get report name dropdown element
     *
     * @return WebElement
     */
    private WebElement getReportNameDropdownElement() {
        pageUtils.waitForElementAppear(driver.findElement(By.xpath("//div[@tab-number='4']//div[@tab-number='2']//div//span[.='Report Name']")));
        return driver.findElement(with(By.xpath("//div[@tab-number='4']//div[@tab-number='2']//div[@class='widget-content widget-dropdown']"))
            .below(By.xpath("//div[@tab-number='4']//div[@tab-number='2']//div//span[.='Report Name']")));
    }
}
