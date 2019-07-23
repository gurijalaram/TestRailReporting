package main.java.header;

import main.java.pages.settings.SettingsPage;
import main.java.utils.PageUtils;
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

    private static Logger logger = LoggerFactory.getLogger(PageHeader.class);

    @FindBy(css = "a[data-ap-comp='exploreButton'")
    private WebElement exploreTab;

    @FindBy(css = "button[data-ap-comp='recentScenarioButton']")
    private WebElement evaluateTab;

    @FindBy(css = "button#scenarioComparisonButton")
    private WebElement compareTab;

    @FindBy(css = "a[data-ap-comp='jobQueue']")
    private WebElement jobQueueButton;

    @FindBy(css = "a span.glyphicon-cog']")
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
        this.get();
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
    public PageHeader selectExploreButton() {
        pageUtils.waitForElementToBeClickable(exploreTab).click();
        return this;
    }

    /**
     * Selects the evaluate button
     *
     * @return current page object
     */
    public PageHeader selectEvaluateButton() {
        pageUtils.waitForElementToBeClickable(exploreTab).click();
        return this;
    }

    /**
     * Selects the compare button
     *
     * @return current page object
     */
    public PageHeader selectCompareButton() {
        pageUtils.waitForElementToBeClickable(exploreTab).click();
        return this;
    }

    /**
     * Opens the settings page
     *
     * @return new page object
     */
    public SettingsPage openSettings() {
        pageUtils.waitForElementToAppear(settingsButton).click();
        return new SettingsPage(driver);
    }

    /**
     * Selects the job queue button
     *
     * @return new page object
     */
    public PageHeader openJobQueue() {
        pageUtils.waitForElementToAppear(jobQueueButton).click();
        return this;
    }

    /**
     * Selects the help button
     *
     * @retun new page object
     */
    public PageHeader openHelp() {
        pageUtils.waitForElementToAppear(helpButton).click();
        return this;
    }

    /**
     * Selects the logout button
     *
     * @return new page object
     */
    public PageHeader openLogOut() {
        pageUtils.waitForElementToAppear(logoutButton).click();
        return this;
    }
}