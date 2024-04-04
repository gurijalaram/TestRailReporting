package com.apriori.cia.ui.pageobjects.homepage;

import com.apriori.cia.ui.pageobjects.header.AdminPageHeader;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class AdminHomePage extends AdminPageHeader {

    @FindBy(xpath = "//div[@class='devices']")
    private WebElement onPremWelcomeText;

    @FindBy(xpath = "//div[@class='index-welcome-text'][1]")
    private WebElement cloudReportsHomeText;

    private WebDriver driver;
    private PageUtils pageUtils;
    WebElement welcomeHomeElementToUse;

    public AdminHomePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        welcomeHomeElementToUse =  PropertiesContext.get("env").equals("onprem") ? onPremWelcomeText : cloudReportsHomeText;
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();

    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        if (PropertiesContext.get("env").equals("onprem")) {
            pageUtils.waitForElementToAppear(onPremWelcomeText);
        } else {
            pageUtils.waitForElementToAppear(cloudReportsHomeText);
        }
    }

    /**
     * Gets current URL of new tab
     *
     * @return String
     */
    public String getCurrentUrl() {
        return pageUtils.getTabTwoUrl();
    }

    /**
     * Gets count of open tabs
     *
     * @return int
     */
    public int getTabCount() {
        return pageUtils.getCountOfOpenTabs();
    }

    /**
     * Wait for element to appear
     */
    public void waitForReportsLogoutDisplayedToAppear() {
        pageUtils.switchToWindow(1);
        pageUtils.waitForElementToAppear(welcomeHomeElementToUse);
    }
}
