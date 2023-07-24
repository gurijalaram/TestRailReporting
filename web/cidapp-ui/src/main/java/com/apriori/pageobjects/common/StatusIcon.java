package com.apriori.pageobjects.common;

import com.apriori.PageUtils;
import com.apriori.utils.enums.StatusIconEnum;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusIcon {

    private static final Logger logger = LoggerFactory.getLogger(StatusIcon.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    public StatusIcon(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Checks icon is displayed
     *
     * @param icon - the icon
     * @return true/false
     */
    public boolean isIconDisplayed(StatusIconEnum icon) {
        By iconStatus = By.cssSelector(String.format(".scenario-status-icons [data-icon='%s']", icon.getStatusIcon()));
        return pageUtils.waitForElementToAppear(iconStatus).isDisplayed();
    }
}
