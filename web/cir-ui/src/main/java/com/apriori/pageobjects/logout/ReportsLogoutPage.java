package com.apriori.pageobjects.logout;

import com.apriori.PageUtils;
import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.properties.PropertiesContext;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportsLogoutPage extends ReportsPageHeader {

    private static final Logger logger = LoggerFactory.getLogger(ReportsLogoutPage.class);

    @FindBy(css = "h2[class='textAccent']")
    private WebElement loginPageTitle;

    @FindBy(css = "button[type='submit']")
    private WebElement cloudLoginButton;

    @FindBy(css = "button[id='submitButton']")
    private WebElement loginButtonOnPremReports;

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public ReportsLogoutPage(WebDriver driver) {
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

    /**
     * Gets isDisplayed value for header
     *
     * @return boolean - isDisplayed
     */
    public boolean isHeaderDisplayed() {
        pageUtils.waitForElementToAppear(loginPageTitle);
        return pageUtils.isElementDisplayed(loginPageTitle);
    }

    /**
     * Gets isEnabled value for header
     *
     * @return boolean - isEnabled
     */
    public boolean isHeaderEnabled() {
        pageUtils.waitForElementToAppear(loginPageTitle);
        return pageUtils.isElementEnabled(loginPageTitle);
    }

    /**
     * Checks if login button is enabled
     * @return boolean
     */
    public boolean isLoginButtonEnabled() {
        WebElement elementToUse = driver.findElement(By.xpath(PropertiesContext.get("${env}.reports.login_locator")));
        pageUtils.waitForElementToBeClickable(elementToUse);
        return elementToUse.isEnabled();
    }
}
