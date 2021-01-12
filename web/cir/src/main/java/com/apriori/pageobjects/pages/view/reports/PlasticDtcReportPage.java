package com.apriori.pageobjects.pages.view.reports;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlasticDtcReportPage extends GenericReportPage {
    private final Logger LOGGER = LoggerFactory.getLogger(AssemblyDetailsReportPage.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    public PlasticDtcReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }
}
