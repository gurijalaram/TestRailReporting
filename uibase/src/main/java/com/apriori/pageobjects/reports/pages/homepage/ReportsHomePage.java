package com.apriori.pageobjects.reports.pages.homepage;

import com.apriori.pageobjects.admin.pages.help.HelpPage;
import com.apriori.pageobjects.reports.header.ReportsPageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportsHomePage extends ReportsPageHeader {

    private final Logger logger = LoggerFactory.getLogger(ReportsHomePage.class);

    private PageUtils pageUtils;
    private HelpPage helpPage;
    private WebDriver driver;

    public ReportsHomePage(WebDriver driver) {
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
}