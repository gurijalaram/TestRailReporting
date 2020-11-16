package com.pageobjects.pages.login;

import com.apriori.utils.PageUtils;

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

public class LearnMorePage extends LoadableComponent<LearnMorePage> {

    private final Logger logger = LoggerFactory.getLogger(LearnMorePage.class);

    @FindBy(id = "menu-main-menu")
    private WebElement mainMenu;

    @FindBy(css = ".page_title")
    private WebElement heading;

    private WebDriver driver;
    private PageUtils pageUtils;

    public LearnMorePage(WebDriver driver) {
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
        return pageUtils.waitForElementToAppear(heading).getText();
    }

    /**
     * Gets window url
     *
     * @return - string
     */
    public String getChildWindowURL() {
        return pageUtils.windowHandler(1).getCurrentUrl();
    }
}