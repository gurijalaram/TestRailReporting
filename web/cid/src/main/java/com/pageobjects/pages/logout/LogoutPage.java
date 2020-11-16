package com.pageobjects.pages.logout;

import com.apriori.utils.PageUtils;

import com.pageobjects.pages.login.CidLoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class LogoutPage extends LoadableComponent<LogoutPage> {

    private final Logger logger = LoggerFactory.getLogger(LogoutPage.class);

    @FindBy(css = "li.user-menu-schema-info")
    private WebElement logOutInfo;

    @FindBy(css = ".user-menu-value")
    private WebElement userName;

    @FindBy(css = "button[data-ap-nav-dialog='showSwitchSchemaDialog']")
    private WebElement switchSchema;

    @FindBy(css = "a[href='session/logout']")
    private WebElement logOut;

    private WebDriver driver;
    private PageUtils pageUtils;

    public LogoutPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(logOutInfo);
    }

    /**
     * Gets the user name details
     *
     * @return the user name as a string
     */
    public String getUserName() {
        return userName.getText();
    }

    public CidLoginPage logOut() {
        pageUtils.waitForElementAndClick(logOut);
        return new CidLoginPage(driver);
    }
}