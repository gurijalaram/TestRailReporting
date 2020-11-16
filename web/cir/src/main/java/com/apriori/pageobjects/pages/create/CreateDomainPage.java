package com.apriori.pageobjects.pages.create;

import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateDomainPage extends ReportsPageHeader {

    private final Logger logger = LoggerFactory.getLogger(CreateDomainPage.class);

    @FindBy(css = "div[data-name='repositoryResourceChooserDialog']")
    private WebElement domainDialog;

    private PageUtils pageUtils;
    private WebDriver driver;

    public CreateDomainPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Gets current URL of new tab
     *
     * @return String
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Gets dialog isDisplayed value
     *
     * @return boolean - isDisplayed
     */
    public boolean isDialogDisplayed() {
        pageUtils.waitForElementToAppear(domainDialog);
        return domainDialog.isDisplayed();
    }

    /**
     * Gets dialog isEnabled value
     *
     * @return boolean - is Enabled
     */
    public boolean isDialogEnabled() {
        pageUtils.waitForElementToAppear(domainDialog);
        return domainDialog.isEnabled();
    }

    /**
     * Gets URL to assert against
     *
     * @return String
     */
    public String getUrlToCheck() {
        return pageUtils.getUrlToCheck();
    }
}
