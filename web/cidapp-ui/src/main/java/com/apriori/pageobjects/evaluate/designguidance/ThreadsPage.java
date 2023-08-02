package com.apriori.pageobjects.evaluate.designguidance;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.DesignGuidanceController;
import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.help.HelpDocPage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ThreadsPage extends LoadableComponent<ThreadsPage> {

    @FindBy(css = ".active [data-icon='screwdriver']")
    private WebElement threadTabActive;

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
        pageUtils.waitForElementToAppear(threadTabActive);
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
     * Gets count column
     *
     * @return string
     */
    public int getGcdCount(String issueType) {
        return Integer.parseInt(designGuidanceController.getColumn(issueType, 2));
    }

    /**
     * Gets threaded column
     *
     * @return string
     */
    public String getThreaded(String threaded) {
        return designGuidanceController.getColumnIcon(threaded, 1);
    }

    /**
     * Gets source column
     *
     * @return string
     */
    public String getSource(String source) {
        return designGuidanceController.getColumn(source, 2);
    }

    /**
     * Gets length column
     *
     * @return string
     */
    public String getLength(String length) {
        return designGuidanceController.getColumn(length, 3);
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
