package com.apriori.cic.ui.pageobjects.workflows.schedule.publishresults;

import com.apriori.cic.api.enums.ReportsEnum;
import com.apriori.cic.ui.utils.Constants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Attach Report tab in Publish Results part during workflow creation or modification
 */
public class PRAttachReportTab extends PublishResultsPart {

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_mashupcontainer-342_DrowpdownWidget-5'] > div > div > span.placeholder")
    private WebElement reportNameDropDownElement;

    @FindBy(css = "#CIC_EmptyReport_MU-2_label-3 > span")
    private WebElement emptyPRReportLbl;

    private String ciReportConfigDDCss = "#CIC_ReportConfigurationCell_MU-[ID]_DrowpdownWidget-25";
    private String getCiCurrCdeSelected = ciReportConfigDDCss + " > div > div > span.placeholder";


    public PRAttachReportTab(WebDriver driver) {
        super(driver);
    }

    public PublishResultsPart selectReport() {
        if (workFlowData.getPublishResultsData().getReportName().equals(ReportsEnum.PART_COST.getReportName())) {
            this.selectReportName();
        } else {
            this.selectReportName();
            this.selectCurrencyCode();
            this.selectCostRounding();
        }
        return new PublishResultsPart(this.driver);
    }


    /**
     * Select report name in AttachReport tab in publish results part
     *
     * @return AttachReportTab
     */
    public PRAttachReportTab selectReportName() {
        pageUtils.waitForElementToAppear(reportNameDropDownElement);
        pageUtils.waitForElementAndClick(reportNameDropDownElement);
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
        Integer rowID = Integer.parseInt(this.driver.findElements(By.cssSelector(reportConfigRootElementsRowsCss)).get(0).getAttribute("id").split("-")[1].trim());
        WebElement ciCurrenyCodeRootElement = driver.findElement(By.cssSelector(ciReportConfigDDCss.replace("[ID]", rowID.toString())));
        WebElement ciCurrenyCodeSelectedElement = driver.findElement(By.cssSelector(getCiCurrCdeSelected.replace("[ID]", rowID.toString())));
        if (!ciCurrenyCodeSelectedElement.getText().equals("USD")) {
            pageUtils.waitForElementAndClick(ciCurrenyCodeRootElement);
            pageUtils.waitFor(Constants.DEFAULT_WAIT);
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
        Integer rowID = Integer.parseInt(this.driver.findElements(By.cssSelector(reportConfigRootElementsRowsCss)).get(1).getAttribute("id").split("-")[1].trim());
        WebElement ciCostRoundingElement = driver.findElement(By.cssSelector(ciReportConfigDDCss.replace("[ID]", rowID.toString())));
        pageUtils.waitForElementAndClick(ciCostRoundingElement);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        this.selectValueFromDDL(workFlowData.getPublishResultsData().getReportCostRounding());
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
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
     * Getter for Publish Results attach report currency code dropdown element
     *
     * @return WebElement
     */
    public WebElement getCurrencyCodeDdl() {
        Integer size = this.driver.findElements(By.cssSelector(reportConfigRootElementsRowsCss)).size();
        size = (size > 0) ? size - 2 : size;
        Integer rowID = Integer.parseInt(this.driver.findElements(By.cssSelector(reportConfigRootElementsRowsCss)).get(size).getAttribute("id").split("-")[1].trim());
        pageUtils.waitForElementToAppear(driver.findElement(By.cssSelector(ciReportConfigDDCss.replace("[ID]", rowID.toString()))));
        return driver.findElement(By.cssSelector(ciReportConfigDDCss.replace("[ID]", rowID.toString())));
    }

    /**
     * Getter for Publish Results attach report Cost Rounding dropdown element
     *
     * @return WebElement
     */
    public WebElement getCostRoundingDdl() {
        Integer size = this.driver.findElements(By.cssSelector(reportConfigRootElementsRowsCss)).size();
        size = (size > 0) ? size - 1 : size;
        Integer rowID = Integer.parseInt(this.driver.findElements(By.cssSelector(reportConfigRootElementsRowsCss)).get(size).getAttribute("id").split("-")[1].trim());
        pageUtils.waitForElementToAppear(driver.findElement(By.cssSelector(ciReportConfigDDCss.replace("[ID]", rowID.toString()))));
        return driver.findElement(By.cssSelector(ciReportConfigDDCss.replace("[ID]", rowID.toString())));
    }


}
