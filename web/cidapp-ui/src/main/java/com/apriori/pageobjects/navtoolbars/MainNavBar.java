package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.help.HelpPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.pageobjects.pages.settings.DisplayPreferencesPage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

/**
 * @author cfrith
 */

@Slf4j
public class MainNavBar extends LoadableComponent<MainNavBar> {

    @FindBy(xpath = "//button[.='Explore']")
    private WebElement exploreButton;

    @FindBy(xpath = "//button[.='Evaluate']")
    private WebElement evaluateButton;

    @FindBy(xpath = "//button[.='Comparisons']")
    private WebElement compareButton;

    @FindBy(css = "[id='qa-header-preferences-button'] button")
    private WebElement settingsButton;

    @FindBy(css = "[id='qa-header-help-button']")
    private WebElement helpDropdown;

    @FindBy(xpath = "//button[.='Help']")
    private WebElement helpButton;

    @FindBy(xpath = "//button[.='About']")
    private WebElement aboutButton;

    @FindBy(css = ".user-dropdown.dropdown")
    private WebElement userDropdown;

    @FindBy(xpath = "//button[.='Logout']")
    private WebElement logoutButton;

    @FindBy(css = ".apt-recommendation-bubble-frame")
    private WebElement gainsightHelp;

    private PageUtils pageUtils;
    private WebDriver driver;

    public MainNavBar(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
        if (pageUtils.isElementDisplayed(gainsightHelp)) {
            pageUtils.javaScriptDelete(gainsightHelp);
        }
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(settingsButton);
        pageUtils.waitForElementToAppear(helpDropdown);
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
