package com.pageobjects.toolbars;

import com.apriori.utils.PageUtils;

import com.pageobjects.pages.compare.ComparePage;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.explore.ExplorePage;
import com.pageobjects.pages.help.HelpPage;
import com.pageobjects.pages.jobqueue.JobQueuePage;
import com.pageobjects.pages.login.CidLoginPage;
import com.pageobjects.pages.settings.SettingsPage;
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

public class PageHeader extends LoadableComponent<PageHeader> {

    private static final Logger logger = LoggerFactory.getLogger(PageHeader.class);

    @FindBy(css = "a[data-ap-comp='exploreButton'")
    private WebElement exploreTab;

    @FindBy(css = "button[data-ap-comp='recentScenarioButton']")
    private WebElement evaluateTab;

    @FindBy(css = "button#scenarioComparisonButton")
    private WebElement compareTab;

    @FindBy(css = "a[data-ap-comp='jobQueue']")
    private WebElement jobQueueButton;

    @FindBy(css = "a span.glyphicon-cog")
    private WebElement settingsButton;

    @FindBy(css = "a.navbar-help")
    private WebElement helpButton;

    @FindBy(css = "a > span.glyphicon-user")
    private WebElement userInfoDropdown;

    @FindBy(xpath = "//a[contains(text(),'Log Out')]")
    private WebElement logOutButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PageHeader(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(jobQueueButton);
        pageUtils.waitForElementToAppear(userInfoDropdown);
    }

    /**
     * Selects the explore button
     *
     * @return current page object
     */
    public ExplorePage selectExploreButton() {
        pageUtils.waitForElementAndClick(exploreTab);
        return new ExplorePage(driver);
    }

    /**
     * Selects the evaluate button
     *
     * @return current page object
     */
    public EvaluatePage selectEvaluateButton() {
        pageUtils.waitForElementAndClick(evaluateTab);
        return new EvaluatePage(driver);
    }

    /**
     * Hover over the evaluate tab
     *
     * @return current page object
     */
    public PageHeader hoverOnEvaluateTab() {
        pageUtils.mouseMove(evaluateTab);
        return this;
    }

    /**
     * Selects the compare button
     *
     * @return current page object
     */
    public ComparePage selectCompareButton() {
        pageUtils.waitForElementAndClick(compareTab);
        return new ComparePage(driver);
    }

    /**
     * Hover over the compare tab
     *
     * @return current page object
     */
    public PageHeader hoverOnCompareTab() {
        pageUtils.mouseMove(compareTab);
        return this;
    }

    /**
     * Opens the settings page
     *
     * @return new page object
     */
    public SettingsPage openSettings() {
        pageUtils.waitForElementAndClick(settingsButton);
        return new SettingsPage(driver);
    }

    /**
     * Selects the job queue button
     *
     * @return new page object
     */
    public JobQueuePage openJobQueue() {
        pageUtils.waitForElementAndClick(jobQueueButton);
        return new JobQueuePage(driver);
    }

    /**
     * Selects the help button
     *
     * @retun new page object
     */
    public HelpPage openHelpMenu() {
        pageUtils.waitForElementAndClick(helpButton);
        return new HelpPage(driver);
    }

    /**
     * Selects the admin dropdown button
     *
     * @return new page object
     */
    public PageHeader openAdminDropdown() {
        pageUtils.waitForElementAndClick(userInfoDropdown);
        return this;
    }

    /**
     * Selects the log out button
     *
     * @return new page object
     */
    public CidLoginPage selectLogOut() {
        pageUtils.waitForElementAndClick(logOutButton);
        return new CidLoginPage(driver);
    }

    /**
     * Gets title of Evaluate tab
     *
     * @return the text as String
     */
    public String getTitleEvaluateTab() {
        return evaluateTab.getAttribute("title");
    }

    /**
     * Gets title of Compare tab
     *
     * @return the text as String
     */
    public String getTitleCompareTab() {
        return compareTab.getAttribute("title");
    }
}