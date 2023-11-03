package com.apriori.cid.ui.pageobjects.common;

import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomAttributesInputsController {

    private static final Logger logger = LoggerFactory.getLogger(CustomAttributesInputsController.class);

    private WebDriver driver;
    private PageUtils pageUtils;

    public CustomAttributesInputsController(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }
}
