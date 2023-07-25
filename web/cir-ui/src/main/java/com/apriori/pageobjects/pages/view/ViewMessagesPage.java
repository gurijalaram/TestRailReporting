package com.apriori.pageobjects.pages.view;

import com.apriori.PageUtils;
import com.apriori.pageobjects.header.ReportsPageHeader;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewMessagesPage extends ReportsPageHeader {

    private static final Logger logger = LoggerFactory.getLogger(ViewMessagesPage.class);

    @FindBy(css = "div.pageHeader-title-text")
    private WebElement messagesPageTitle;

    private final PageUtils pageUtils;
    private final WebDriver driver;

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
