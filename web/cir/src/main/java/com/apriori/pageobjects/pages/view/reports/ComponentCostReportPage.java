package com.apriori.pageobjects.pages.view.reports;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentCostReportPage extends GenericReportPage {

    private final Logger logger = LoggerFactory.getLogger(ComponentCostReportPage.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    public ComponentCostReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }
}
