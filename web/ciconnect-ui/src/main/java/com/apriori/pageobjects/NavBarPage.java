package com.apriori.pageobjects;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavBarPage {
    private static final Logger logger = LoggerFactory.getLogger(NavBarPage.class);

    @FindBy(css = "#root_button-30 > button")
    private WebElement accountButton;
    @FindBy(css = "#CIC_UserDropDown_MU-BMController-2_button-36 > button")
    private WebElement logoutButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public NavBarPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
    }

    /**
     * Click the account button in the navigation bar
     */
    public void clickAccountButton() {
        pageUtils.waitForElementAndClick(accountButton);
    }

    public void logOut() {
        clickAccountButton();
        pageUtils.waitForElementAndClick(logoutButton);
    }
}
