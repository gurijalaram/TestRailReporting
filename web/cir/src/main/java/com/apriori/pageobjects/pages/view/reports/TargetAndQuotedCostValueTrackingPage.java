package com.apriori.pageobjects.pages.view.reports;

import com.apriori.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TargetAndQuotedCostValueTrackingPage extends GenericReportPage {

    private final Logger LOGGER = LoggerFactory.getLogger(TargetQuotedCostTrendReportPage.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    public TargetAndQuotedCostValueTrackingPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    public String getFinalCost() {
        By locator = By.xpath("//table[@class='jrPage']//tr[30]/td[23]/span");
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }
}
