package com.apriori.pageobjects.pages.evaluate.components;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreePage {

    private static final Logger logger = LoggerFactory.getLogger(TreePage.class);

    private WebDriver driver;
    private PageUtils pageUtils;

    public TreePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }
}
