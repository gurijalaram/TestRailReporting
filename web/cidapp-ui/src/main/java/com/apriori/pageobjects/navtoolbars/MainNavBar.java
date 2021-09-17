package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.help.HelpPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.pageobjects.pages.settings.DisplayPreferencesPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
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

public class MainNavBar extends LoadableComponent<MainNavBar> {

    private static final Logger logger = LoggerFactory.getLogger(ExplorePage.class);

    @FindBy(xpath = "//button[.='Explore']")
    private WebElement exploreButton;

    @FindBy(xpath = "//button[.='Evaluate']")
    private WebElement evaluateButton;

    @FindBy(xpath = "//button[.='Compare']")
    private WebElement compareButton;

    @FindBy(id = "qa-header-preferences-button")
    private WebElement settingsButton;

    @FindBy(id = "qa-header-help-button")
    private WebElement helpDropdown;

    @FindBy(xpath = "//button[.='Help']")
    private WebElement helpButton;

    @FindBy(xpath = "//button[.='About']")
    private WebElement aboutButton;

    @FindBy(css = ".user-dropdown.dropdown")
    private WebElement userDropdown;

    @FindBy(css = "[data-icon='sign-out-alt']")
    private WebElement logoutButton;

    private PageUtils pageUtils;
    private WebDriver driver;

    public MainNavBar(WebDriver driver) {
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
        pageUtils.waitForElementAppear(settingsButton);
        pageUtils.waitForElementAppear(helpDropdown);
        pageUtils.waitForElementsToNotAppear(By.cssSelector("[role='status']"));
        pageUtils.waitForSteadinessOfElement(By.xpath("//button[.='Explore']"));
    }

    /**
     * Selects the settings button
     *
     * @return new page object
     */
    public DisplayPreferencesPage openSettings() {
        pageUtils.waitForElementAndClick(settingsButton);
        return new DisplayPreferencesPage(driver);
    }

    /**
     * Selects the help dropdown and go to Help
     *
     * @retun new page object
     */
    public HelpPage goToHelp() {
        pageUtils.waitForElementAndClick(helpDropdown);
        return new HelpPage(driver);
    }

    /**
     * Selects the help dropdown and go to About
     *
     * @retun new page object
     */
    public MainNavBar goToAbout() {
        pageUtils.waitForElementAndClick(helpDropdown);
        pageUtils.waitForElementAndClick(aboutButton);
        return this;
    }

    /**
     * Navigates to the explore page
     *
     * @return new page object
     */
    public ExplorePage clickExplore() {
        pageUtils.waitForElementAndClick(exploreButton);
        return new ExplorePage(driver);
    }

    /**
     * Navigates to the evaluate page
     *
     * @return new page object
     */
    public EvaluatePage clickEvaluate() {
        pageUtils.waitForElementAndClick(exploreButton);
        return new EvaluatePage(driver);
    }

    /**
     * Navigates to the compare page
     *
     * @return new page object
     */
    public ComparePage clickCompare() {
        pageUtils.waitForElementAndClick(compareButton);
        return new ComparePage(driver);
    }

    /**
     * Logout of the application
     *
     * @return new page object
     */
    public CidAppLoginPage logout() {
        pageUtils.waitForElementAndClick(userDropdown);
        pageUtils.waitForElementAndClick(logoutButton);
        return new CidAppLoginPage(driver);
    }
}
