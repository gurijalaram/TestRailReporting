package com.apriori.pageobjects.pages.create;

import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateDashboardPage extends ReportsPageHeader {

    private final Logger LOGGER = LoggerFactory.getLogger(CreateDashboardPage.class);

    @FindBy(css = "div[class='pageHeader-title-text']")
    private WebElement dashboardPageTitle;

    private PageUtils pageUtils;
    private WebDriver driver;

    public CreateDashboardPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Get page title text
     *
     * @return String - page title text
     */
    public String getAdHocViewTitleText() {
        pageUtils.waitForElementToAppear(dashboardPageTitle);
        return dashboardPageTitle.getText();
    }
}
