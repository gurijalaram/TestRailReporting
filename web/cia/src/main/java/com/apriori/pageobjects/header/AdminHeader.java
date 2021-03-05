package com.apriori.pageobjects.header;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminHeader extends PageHeader {

    private static final Logger logger = LoggerFactory.getLogger(PageHeader.class);

    private PageUtils pageUtils;

    public AdminHeader(WebDriver driver) {
        super(driver);
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

}
