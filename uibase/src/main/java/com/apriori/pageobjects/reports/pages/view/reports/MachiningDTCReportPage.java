package com.apriori.pageobjects.reports.pages.view.reports;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class MachiningDTCReportPage extends GenericReportPage {

    private final Logger logger = LoggerFactory.getLogger(MachiningDTCReportPage.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(xpath = "//*[local-name()='svg']//*[local-name()='g' and @class='highcharts-series-group']//*[local-name()='g'][2]//*[local-name()='path'][44]")
    private WebElement currentBlob;

    @FindBy(css = "tspan:nth-child(5)")
    private WebElement tooltipValueElement;

    public MachiningDTCReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets value from tooltip on chart
     *
     * @return
     */
    public BigDecimal getValueFromCentralCircleInChart() {
        pageUtils.waitForElementToAppear(currentBlob);
        Actions builder = new Actions(driver).moveToElement(currentBlob);
        builder.perform();
        pageUtils.waitForElementToAppear(tooltipValueElement);
        String value = tooltipValueElement.getAttribute("textContent")
            .replace(",", "")
            .replace(" ", "");
        return new BigDecimal(value);
    }
}
