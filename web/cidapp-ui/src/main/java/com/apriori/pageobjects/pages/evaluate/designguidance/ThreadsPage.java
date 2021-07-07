package com.apriori.pageobjects.pages.evaluate.designguidance;

import com.apriori.pageobjects.common.DesignGuidanceController;
import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.help.HelpDocPage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ThreadsPage extends LoadableComponent<ThreadsPage> {

    @FindBy(css = "[data-icon='screwdriver']")
    private WebElement threadTab;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;
    private DesignGuidanceController designGuidanceController;

    public ThreadsPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        this.designGuidanceController = new DesignGuidanceController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
     * Selects issue type and gcd
     *
     * @param issueType     - the issue type
     * @param gcd           - the gcd
     * @return current page object
     */
    public ThreadsPage selectIssueTypeGcd(String issueType, String gcd) {
        selectIssueType(issueType)
            .selectGcd(gcd.trim());
        return this;
    }

    /**
     * Selects the issue type
     *
     * @param issueType - the issue type
     * @return current page object
     */
    private ThreadsPage selectIssueType(String issueType) {
        By byIssueType = designGuidanceController.getBy(issueType);
        pageUtils.waitForElementAndClick(byIssueType);
        return this;
    }

    /**
     * Selects the gcd
     *
     * @param gcd - the gcd
     * @return current page object
     */
    private ThreadsPage selectGcd(String gcd) {
        By byGcd = designGuidanceController.getBy(gcd);
        designGuidanceController.deSelectAllGcd();
        pageUtils.waitForElementAndClick(byGcd);
        return this;
    }

    /**
     * Gets threaded column
     *
     * @return string
     */
    public int getThreaded(String threaded) {
        return Integer.parseInt(designGuidanceController.getColumn(threaded, 1));
    }

    /**
     * Gets source column
     *
     * @return string
     */
    public int getSource(String source) {
        return Integer.parseInt(designGuidanceController.getColumn(source, 2));
    }

    /**
     * Gets length column
     *
     * @return string
     */
    public int getLength(String length) {
        return Integer.parseInt(designGuidanceController.getColumn(length, 3));
    }

    /**
     * Closes current panel
     *
     * @return new page object
     */
    public EvaluatePage closePanel() {
        return panelController.closePanel();
    }

    /**
     * Opens the help page
     *
     * @return new page object
     */
    public HelpDocPage openHelp() {
        return panelController.openHelp();
    }
}
