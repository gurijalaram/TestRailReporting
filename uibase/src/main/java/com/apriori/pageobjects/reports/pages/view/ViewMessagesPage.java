package com.apriori.pageobjects.reports.pages.view;

import com.apriori.pageobjects.reports.header.ReportsPageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewMessagesPage extends ReportsPageHeader {

    private final Logger logger = LoggerFactory.getLogger(ViewMessagesPage.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(xpath = "//div[contains(@class, 'showingToolBar')]/div/div[1]/div[contains(@class, 'title')]")
    private WebElement messagesPageTitle;

    public ViewMessagesPage(WebDriver driver) {
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
    public String getMessagesTitleText() {
        pageUtils.waitForElementToAppear(messagesPageTitle);
        return messagesPageTitle.getText();
    }
}
