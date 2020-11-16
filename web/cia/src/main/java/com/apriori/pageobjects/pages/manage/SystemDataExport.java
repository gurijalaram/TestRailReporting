package com.apriori.pageobjects.pages.manage;

import com.apriori.pageobjects.header.AdminHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemDataExport extends AdminHeader {

    private final Logger logger = LoggerFactory.getLogger(SystemDataExport.class);

    private WebDriver driver;
    private PageUtils pageUtils;

    @FindBy(css = "h1")
    private WebElement manageSystemDataExportTitle;

    public SystemDataExport(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Gets isDisplayed value for header
     *
     * @return boolean - isDisplayed
     */
    public boolean isHeaderDisplayed() {
        pageUtils.waitForElementToAppear(manageSystemDataExportTitle);
        return pageUtils.isElementDisplayed(manageSystemDataExportTitle);
    }

    /**
     * Gets isEnabled value for header
     *
     * @return boolean - isEnabled
     */
    public boolean isHeaderEnabled() {
        pageUtils.waitForElementToAppear(manageSystemDataExportTitle);
        return pageUtils.isElementEnabled(manageSystemDataExportTitle);
    }
}
