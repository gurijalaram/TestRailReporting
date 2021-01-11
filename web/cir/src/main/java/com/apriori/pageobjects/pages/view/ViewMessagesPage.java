package com.apriori.pageobjects.pages.view;

import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewMessagesPage extends ReportsPageHeader {

    private final Logger LOGGER = LoggerFactory.getLogger(ViewMessagesPage.class);

    @FindBy(xpath = "//div[contains(@class, 'showingToolBar')]/div/div[1]/div[contains(@class, 'title')]")
    private WebElement messagesPageTitle;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ViewMessagesPage(WebDriver driver) {
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
    public String getMessagesTitleText() {
        pageUtils.waitForElementToAppear(messagesPageTitle);
        return messagesPageTitle.getText();
    }
}
