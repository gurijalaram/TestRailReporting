package com.apriori.pageobjects.pages.help;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnlineHelpPage extends LoadableComponent<OnlineHelpPage> {

    private Logger logger = LoggerFactory.getLogger(OnlineHelpPage.class);

    @FindBy(css = "[class='Documentation_Cover_Page_Title']")
    private WebElement coverPageTitle;


    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(coverPageTitle);
    }

    private WebDriver driver;
    private PageUtils pageUtils;

    public OnlineHelpPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    /**
     * Gets page Title
     * @return - string
     */
    public String getPageTitle() {
        return pageUtils.waitForElementToAppear(coverPageTitle).getText();
    }

    /**
     * Gets window url
     * @return - string
     */
    public String getChildWindowURL() {
        return pageUtils.windowHandler().getCurrentUrl();
    }
}
