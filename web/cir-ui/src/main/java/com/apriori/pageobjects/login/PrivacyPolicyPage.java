package com.apriori.pageobjects.login;

import com.apriori.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrivacyPolicyPage extends LoadableComponent<PrivacyPolicyPage> {
    private static final Logger logger = LoggerFactory.getLogger(PrivacyPolicyPage.class);

    @FindBy(id = "menu-main-menu")
    private WebElement mainMenu;

    @FindBy(css = "section[id='services_title_section'] > div > h1")
    private WebElement heading;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PrivacyPolicyPage(WebDriver driver) {
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
     * Gets page heading
     *
     * @return - string
     */
    public String getPageHeading() {
        pageUtils.switchToWindow(1);
        return heading.getText();
    }

    /**
     * Gets window url
     *
     * @return - string
     */
    public String getChildWindowURL() {
        return pageUtils.getTabTwoUrl();
    }

    /**
     * Gets count of open tabs
     *
     * @return int - open tab count
     */
    public int getTabCount() {
        return pageUtils.getCountOfOpenTabs();
    }
}
