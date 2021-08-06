package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.pages.login.ElectronicsDataCollectionPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavigationBar extends LoadableComponent<NavigationBar> {

    private static final Logger logger = LoggerFactory.getLogger(ElectronicsDataCollectionPage.class);

    @FindBy(css = ".help-dropdown")
    private WebElement helpDropdown;

    private PageUtils pageUtils;
    private WebDriver driver;

    public NavigationBar(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(helpDropdown);
    }

    /**
     * Selects the help dropdown and go to Help
     *
     * @retun new page object
     */
    public HelpPage clickHelpDropdown() {
        pageUtils.waitForElementAndClick(helpDropdown);
        return new HelpPage(driver);
    }
}
