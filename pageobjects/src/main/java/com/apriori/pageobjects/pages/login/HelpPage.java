package com.apriori.pageobjects.pages.login;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.apriori.pageobjects.reports.header.ReportsHeader;

/**
 * @author cfrith
 */

public class HelpPage extends ReportsHeader {

    private Logger logger = LoggerFactory.getLogger(HelpPage.class);

    @FindBy(id = "menu-main-menu")
    private WebElement mainMenu;

    @FindBy(css = "body > h1")
    private WebElement heading;

    @FindBy(css = "iframe[id='topic']")
    private WebElement mainContentIframe;

    private WebDriver driver;
    private PageUtils pageUtils;

    public HelpPage(WebDriver driver) {
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
    protected void isLoaded() {
    }

    /**
     * Gets page heading
     * @return - string
     */
    public String getPageHeading() {
        return pageUtils.getPageHeading(heading);
    }

    /**
     * Gets window url
     * @return - string
     */
    public String getChildWindowURL() {
        return pageUtils.getTabTwoUrl();
    }

    /**
     * Gets number of open tabs
     * @return int - number of tabs
     */
    public int getTabCount() {
        return pageUtils.getCountOfOpenTabs();
    }

    /**
     * Waits for header to appear (page load)
     */
    public void waitForHeaderLoad() {
        pageUtils.waitForElementToAppear(heading);
    }
}