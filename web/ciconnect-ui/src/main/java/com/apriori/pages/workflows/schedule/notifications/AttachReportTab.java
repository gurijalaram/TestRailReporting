package com.apriori.pages.workflows.schedule.notifications;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.Constants;

/**
 * Attach Report tab in Notification part during workflow creation or modification
 */
public class AttachReportTab extends NotificationsPart {

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_DrowpdownWidget-242'] > div > div > span.placeholder")
    private WebElement reportNameDropDownElement;

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_DrowpdownWidget-173'] > div > div > span.placeholder")
    private WebElement currencyCode;

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_textbox-175'] > table > tbody > tr > td > input")
    private WebElement recipientEmailAddressTxtElement;

    private String ciReportConfigDDCss = "#CIC_ReportConfigurationCell_MU-[ID]_DrowpdownWidget-25";
    private String getCiCurrCdeSelected = ciReportConfigDDCss + " > div > div > span.placeholder";
    private String initialFieldRow;

    public AttachReportTab(WebDriver driver) {
        super(driver);
    }

    public NotificationsPart setUpEmailConfiguration() {
        this.selectReportName();
        this.selectCurrencyCode();
        this.selectCostRounding();
        return new NotificationsPart(this.driver);
    }

    /**
     * Select Report Name
     *
     * @return
     */
    public AttachReportTab selectReportName() {
        pageUtils.waitForElementAndClick(reportNameDropDownElement);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        this.selectValueFromDDL(workFlowData.getNotificationsData().getReportName());
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        return this;
    }

    /**
     * Select currency code
     *
     * @return AttachReport object
     */
    public AttachReportTab selectCurrencyCode() {
        Integer rowID = Integer.parseInt(this.driver.findElements(By.cssSelector(reportConfigRootElementsRowsCss)).get(0).getAttribute("id").split("-")[1].trim());
        WebElement ciCurrenyCodeRootElement = driver.findElement(By.cssSelector(ciReportConfigDDCss.replace("[ID]", rowID.toString())));
        WebElement ciCurrenyCodeSelectedElement = driver.findElement(By.cssSelector(getCiCurrCdeSelected.replace("[ID]", rowID.toString())));
        if (!ciCurrenyCodeSelectedElement.getText().equals("USD")) {
            pageUtils.waitForElementAndClick(ciCurrenyCodeRootElement);
            this.selectValueFromDDL(workFlowData.getNotificationsData().getReportCurrencyCode());
            pageUtils.waitFor(Constants.DEFAULT_WAIT);
        }
        return this;
    }

    /**
     * select cost rounding
     *
     * @return AttachReport object
     */
    public AttachReportTab selectCostRounding() {
        Integer rowID = Integer.parseInt(this.driver.findElements(By.cssSelector(reportConfigRootElementsRowsCss)).get(1).getAttribute("id").split("-")[1].trim());
        WebElement ciCostRoundingElement = driver.findElement(By.cssSelector(ciReportConfigDDCss.replace("[ID]", rowID.toString())));
        pageUtils.waitForElementAndClick(ciCostRoundingElement);
        this.selectValueFromDDL(workFlowData.getNotificationsData().getReportCostRounding());
        pageUtils.waitFor(Constants.DEFAULT_WAIT);

        return this;
    }

}
