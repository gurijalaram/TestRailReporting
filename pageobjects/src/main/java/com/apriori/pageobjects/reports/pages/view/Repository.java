package com.apriori.pageobjects.reports.pages.view;

import com.apriori.pageobjects.reports.header.ReportsHeader;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Repository extends ReportsHeader {

    private final Logger logger = LoggerFactory.getLogger(Repository.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(css = "div[id='results'] > div > div:nth-child(1) > div")
    private WebElement repositoryPageTitle;

    public Repository(WebDriver driver) {
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
     * @return String - page title text
     */
    public String getRepositoryTitleText() {
        pageUtils.waitForElementToAppear(repositoryPageTitle);
        return repositoryPageTitle.getText();
    }
}
