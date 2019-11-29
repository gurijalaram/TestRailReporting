package com.apriori.pageobjects.reports.header;

import com.apriori.pageobjects.header.ExploreHeader;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportsHeader extends PageHeader {
    private static Logger logger = LoggerFactory.getLogger(ExploreHeader.class);

    private PageUtils pageUtils;

    public ReportsHeader(WebDriver driver) {
        super(driver);
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }
}
