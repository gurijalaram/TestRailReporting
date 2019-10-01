package com.apriori.pageobjects.header;

import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.jobqueue.JobQueuePage;

/**
 * @author cfrith
 */

public class PageHeader extends LoadableComponent<PageHeader> {

    private static Logger logger = LoggerFactory.getLogger(PageHeader.class);

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

    @FindBy(css = "span.glyphicon-user")
    private WebElement logoutButton;

    @FindBy(css = "a > span.glyphicon-user")
    private WebElement userInfoDropdown;

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
     * Selects the compare button
     *
     * @return current page object
     */
    public ComparePage selectCompareButton() {
        pageUtils.waitForElementAndClick(compareTab);
        return new ComparePage(driver);
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
    public PageHeader openHelp() {
        pageUtils.waitForElementAndClick(helpButton);
        return this;
    }

    /**
     * Selects the logout button
     *
     * @return new page object
     */
    public PageHeader openLogOut() {
        pageUtils.waitForElementAndClick(logoutButton);
        return this;
    }
}