package com.pageobjects.toolbars;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kpatel
 */

public class ExploreHeader extends GenericHeader {

    private static final Logger logger = LoggerFactory.getLogger(ExploreHeader.class);

    private PageUtils pageUtils;

    public ExploreHeader(WebDriver driver) {
        super(driver);
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }
}