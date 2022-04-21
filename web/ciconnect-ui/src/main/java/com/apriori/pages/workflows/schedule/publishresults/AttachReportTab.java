package com.apriori.pages.workflows.schedule.publishresults;

import com.apriori.pages.workflows.schedule.notifications.NotificationsPart;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.Constants;

/**
 * Attach Report tab in Publish Results part during workflow creation or modification
 */
public class AttachReportTab extends PublishResultsPart {

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_mashupcontainer-342_DrowpdownWidget-5'] > div > div > span.placeholder")
    private WebElement reportNameDropDownElement;

    private String ciReportConfigDDCss = "#CIC_ReportConfigurationCell_MU-[ID]_DrowpdownWidget-25";
    private String getCiCurrCdeSelected = ciReportConfigDDCss + " > div > div > span.placeholder";


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
     * Select report name in AttachReport tab in publish results part
     *
     * @return AttachReportTab
     */
    public AttachReportTab selectReportName() {
        pageUtils.waitForElementToAppear(reportNameDropDownElement);
        pageUtils.waitForElementAndClick(reportNameDropDownElement);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        this.selectValueFromDDL(workFlowData.getNotificationsData().getReportName());
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        return this;
    }

    /**
     * Select currency code in AttachReport tab in publish results part
     *
     * @return AttachReportTab
     */
    public AttachReportTab selectCurrencyCode() {
        Integer rowID = Integer.parseInt(this.driver.findElements(By.cssSelector(reportConfigRootElementsRowsCss)).get(0).getAttribute("id").split("-")[1].trim());
        WebElement ciCurrenyCodeRootElement = driver.findElement(By.cssSelector(ciReportConfigDDCss.replace("[ID]", rowID.toString())));
        WebElement ciCurrenyCodeSelectedElement = driver.findElement(By.cssSelector(getCiCurrCdeSelected.replace("[ID]", rowID.toString())));
        if (!ciCurrenyCodeSelectedElement.getText().equals("USD")) {
            pageUtils.waitForElementAndClick(ciCurrenyCodeRootElement);
            pageUtils.waitFor(Constants.DEFAULT_WAIT);
            this.selectValueFromDDL(workFlowData.getNotificationsData().getReportCurrencyCode());
        }
        return this;
    }

    /**
     * select cost rounding in Attach Report tab in publish results part
     *
     * @return AttachReportTab
     */
    public AttachReportTab selectCostRounding() {
        Integer rowID = Integer.parseInt(this.driver.findElements(By.cssSelector(reportConfigRootElementsRowsCss)).get(1).getAttribute("id").split("-")[1].trim());
        WebElement ciCostRoundingElement = driver.findElement(By.cssSelector(ciReportConfigDDCss.replace("[ID]", rowID.toString())));
        pageUtils.waitForElementAndClick(ciCostRoundingElement);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        this.selectValueFromDDL(workFlowData.getNotificationsData().getReportCostRounding());
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        return this;
    }
}
