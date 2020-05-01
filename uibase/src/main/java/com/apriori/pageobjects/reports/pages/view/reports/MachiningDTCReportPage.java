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

    public MachiningDTCReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }
}
