package com.apriori.pageobjects.reports.pages.homepage;

import com.apriori.pageobjects.pages.login.HelpPage;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.apriori.pageobjects.reports.header.ReportsHeader;

public class HomePage extends ReportsHeader {

    private final Logger logger = LoggerFactory.getLogger(HomePage.class);

    private PageUtils pageUtils;
    private HelpPage helpPage;
    private WebDriver driver;

    public HomePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.helpPage = new HelpPage(driver);
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

    @FindBy(css = "button[aria-label='Create Data Sources']")
    private WebElement createButton;

    /**
     * Check if Create button is displayed
     *
     * @return Visibility of button
     */
    public boolean isCreateButtonDisplayed() {
        pageUtils.waitForElementToAppear(createButton);
        return createButton.isDisplayed();
    }

    /**
     * Gets current URL of new tab
     * @return String
     */
    public String getCurrentUrl() {
        return pageUtils.getTabTwoUrl();
    }

    /**
     * Gets count of open tabs
     * @return int
     */
    public int getTabCount() {
        return pageUtils.getCountOfOpenTabs();
    }
}