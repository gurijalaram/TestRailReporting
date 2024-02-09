package com.apriori.cic.ui.pageobjects.workflows.schedule.publishresults;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.cic.ui.utils.Constants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Attach Report tab in Publish Results part during workflow creation or modification
 */
public class PRAttachReportTab extends PublishResultsPart {

    @FindBy(css = "#CIC_EmptyReport_MU-2_label-3 > span")
    private WebElement emptyPRReportLbl;

    @FindBy(xpath = " //div[@tab-number='5']//div/span[.='Report Configuration']")
    private WebElement reportConfigurationLbl;

    public PRAttachReportTab(WebDriver driver) {
        super(driver);
    }

    /**
     * Select report name in AttachReport tab in publish results part
     *
     * @return AttachReportTab
     */
    public PRAttachReportTab selectReportName() {
        pageUtils.waitForElementToAppear(getReportNameDropdownElement());
        pageUtils.waitForElementAndClick(getReportNameDropdownElement());
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        this.selectValueFromDDL(workFlowData.getPublishResultsData().getReportName());
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        return this;
    }

    /**
     * Select currency code in AttachReport tab in publish results part
     *
     * @return AttachReportTab
     */
    public PRAttachReportTab selectCurrencyCode() {
        pageUtils.waitUntilDropdownOptionsLoaded(getCurrencyCodeDdl().findElement(By.tagName("select")));
        WebElement currencyCodeElement = getCurrencyCodeDdl().findElement(By.tagName("div")).findElement(By.tagName("div"));
        pageUtils.waitForElementAttributeToAppear(currencyCodeElement, "class", "ss-single-selected");
        if (!getCurrencyCodeDdl().getText().equals("USD")) {
            pageUtils.waitForElementToBeClickable(getCurrencyCodeDdl());
            pageUtils.waitForElementAndClick(getCurrencyCodeDdl());
            this.selectValueFromDDL(workFlowData.getPublishResultsData().getReportCurrencyCode());
        }
        return this;
    }

    /**
     * select cost rounding in Attach Report tab in publish results part
     *
     * @return AttachReportTab
     */
    public PRAttachReportTab selectCostRounding() {
        pageUtils.waitForElementToAppear(getCostRoundingDdl());
        pageUtils.waitForElementAndClick(getCostRoundingDdl());
        this.selectValueFromDDL(workFlowData.getPublishResultsData().getReportCostRounding());
        return this;
    }

    /**
     * Getter for Publish Results attach report empty report label element
     *
     * @return WebElement
     */
    public WebElement getEmptyPRReportLbl() {
        pageUtils.waitForElementToAppear(emptyPRReportLbl);
        return emptyPRReportLbl;
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
        return driver.findElement(with(By.xpath("//div[@tab-number='5']//div[@tab-number='2']//div[contains(@class, 'tw-flex-row')]//div[@class='widget-content widget-dropdown']"))
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

        return driver.findElement(with(By.xpath("//div[@tab-number='5']//div[@tab-number='2']//div[contains(@class, 'tw-flex-row')]//div[@class='widget-content widget-dropdown']"))
            .toLeftOf(currencyTextBoxElement));
    }

    /**
     * get the list of standard mappings rows
     *
     * @return list of standard mappings rows
     */
    public List<WebElement> getAttachReportDropdownFields() {
        return driver.findElements(By.xpath("//div[@tab-number='5']//div[@tab-number='2']//div[contains(@class, 'tw-flex-row')]//div[@class='widget-content widget-dropdown']"));
    }

    /**
     * get all the text fields from notification - attach report tab
     *
     * @return - list of webelements
     */
    public List<WebElement> getAttachReportTextFields() {
        return driver.findElements(By.xpath("//div[@tab-number='5']//div[@tab-number='2']//div[contains(@class, 'tw-flex-row')]//div[@class='widget-content widget-textbox']//input[@disabled='disabled']"));
    }

    /**
     * get report name dropdown element
     *
     * @return WebElement
     */
    private WebElement getReportNameDropdownElement() {
        return driver.findElement(with(By.xpath("//div[@tab-number='5']//div[@tab-number='2']//div[@class='widget-content widget-dropdown']"))
            .below(By.xpath("//div[@tab-number='5']//div[@tab-number='2']//div//span[.='Report Name']")));
    }

}
