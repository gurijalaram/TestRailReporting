package com.apriori.cir.ui.pageobjects.manage;

import com.apriori.cir.ui.pageobjects.header.ReportsPageHeader;
import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManageUsersPage extends ReportsPageHeader {

    private static final Logger logger = LoggerFactory.getLogger(ManageUsersPage.class);

    @FindBy(xpath = "//div[@id='users']/div/div[1]/div[contains(@class, 'title')]")
    private WebElement usersPageTitle;

    private final PageUtils pageUtils;
    private final WebDriver driver;

    public ManageUsersPage(WebDriver driver) {
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
    public String getUsersTitleText() {
        pageUtils.waitForElementToAppear(usersPageTitle);
        return usersPageTitle.getText();
    }
}
