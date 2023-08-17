package com.apriori.pageobjects.create;

import com.apriori.PageUtils;
import com.apriori.pageobjects.header.ReportsPageHeader;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateDataSourcePage extends ReportsPageHeader {

    private static final Logger logger = LoggerFactory.getLogger(CreateDataSourcePage.class);

    @FindBy(xpath = "//div[@id='display']/div/div/div/div[contains(@class, 'header')]/div")
    private WebElement dataSourcePageTitle;

    private final PageUtils pageUtils;
    private final WebDriver driver;

    public CreateDataSourcePage(WebDriver driver) {
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
     * Get page title text
     *
     * @return String - page title text
     */
    public String getDataSourceTitleText() {
        pageUtils.waitForElementToAppear(dataSourcePageTitle);
        return dataSourcePageTitle.getText();
    }
}
