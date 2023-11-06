package com.apriori.cid.ui.pageobjects.evaluate.designguidance;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.cid.ui.pageobjects.common.DesignGuidanceController;
import com.apriori.cid.ui.pageobjects.common.PanelController;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.help.HelpDocPage;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.Arrays;
import java.util.List;

/**
 * @author cfrith
 */

@Slf4j
public class GuidanceIssuesPage extends LoadableComponent<GuidanceIssuesPage> {

    @FindBy(xpath = "//button[.='Issues']")
    private WebElement issuesTab;

    @FindBy(css = ".design-guidance-detail-card .apriori-table")
    private WebElement chartTable;

    @FindBy(css = ".table-head .checkbox-icon")
    private WebElement gcdCheckbox;

    @FindBy(css = ".issue-description")
    private WebElement issueDescription;

    @FindBy(css = "svg[data-icon='eye']")
    private WebElement investigationTab;

    @FindBy(css = "svg[data-icon='ruler']")
    private WebElement tolerancesTab;

    @FindBy(css = "svg[data-icon='screwdriver']")
    private WebElement threadsTab;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;
    private DesignGuidanceController designGuidanceController;

    public GuidanceIssuesPage(WebDriver driver) {
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
        assertTrue(issuesTab.getAttribute("class").contains("active"), "Issues tab was not selected");
    }

    /**
     * Selects issue type and gcd
     *
     * @param issueDropdown - the issue type dropdown
     * @param issueType     - the issue type
     * @param gcd           - the gcd
     * @return current page object
     */
    public GuidanceIssuesPage selectIssueTypeGcd(String issueDropdown, String issueType, String gcd) {
        collapseDropdowns();
        selectIssueType(issueDropdown, issueType)
            .selectGcd(gcd.trim());
        return this;
    }

    /**
     * Selects issue type
     *
     * @param issueDropdown - the issue dropdown
     * @param issueType     - the issue type
     * @return current page object
     */
    public GuidanceIssuesPage selectIssueType(String issueDropdown, String issueType) {
        selectIssue(issueDropdown);
        selectIssueType(issueType);
        return this;
    }

    /**
     * Selects the issue type
     *
     * @param issueType - the issue type
     * @return current page object
     */
    private GuidanceIssuesPage selectIssueType(String issueType) {
        By byIssueType = designGuidanceController.getBy(issueType);
        pageUtils.waitForElementAndClick(byIssueType);
        return this;
    }

    /**
     * Selects issue
     *
     * @param issueDropdown - the issue dropdown
     */
    private void selectIssue(String issueDropdown) {
        String[] issues = issueDropdown.split(",");

        Arrays.stream(issues).map(issue -> pageUtils.waitForElementToAppear(By.xpath(String.format("//div[@role='cell'][.='%s']", issue.trim())))
                .findElement(By.cssSelector("[data-icon='circle-chevron-down']")))
            .forEach(dropdown -> pageUtils.scrollWithJavaScript(dropdown, true).click());
    }

    /**
     * Collapses issue dropdowns if expanded
     */
    private void collapseDropdowns() {
        driver.findElements(By.cssSelector("[role='cell'] .expanded"))
            .forEach(WebElement::click);
    }

    /**
     * Selects the gcd
     *
     * @param gcd - the gcd
     * @return current page object
     */
    private GuidanceIssuesPage selectGcd(String gcd) {
        By byGcd = designGuidanceController.getBy(gcd);
        designGuidanceController.deSelectAllGcd();
        pageUtils.waitForElementAndClick(byGcd);
        return this;
    }

    /**
     * Gets severity column
     *
     * @return string
     */
    public boolean isSeverity(String issueType, Severity severity) {
        List<WebElement> cells = driver.findElements(By.xpath(String.format("//div[.='%s']/..//div[@role='cell']", issueType.trim())));
        return cells.get(1).findElement(By.cssSelector("svg")).getAttribute("data-icon").equals(severity.severityValue);
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
     * Gets current column
     *
     * @return string
     */
    public int getGcdCurrent(String gcd) {
        return Integer.parseInt(designGuidanceController.getColumn(gcd, 1));
    }

    /**
     * Gets suggested column
     *
     * @return string
     */
    public String getGcdSuggested(String gcd) {
        return designGuidanceController.getColumn(gcd, 2);
    }


    /**
     * Gets status of header gcd checkbox
     *
     * @return string
     */
    private String getCheckboxStatus() {
        return pageUtils.waitForElementToAppear(gcdCheckbox.findElement(By.cssSelector("svg"))).getAttribute("data-icon");
    }

    /**
     * Gets issue description
     *
     * @return string
     */
    public String getIssueDescription() {
        return pageUtils.waitForElementToAppear(issueDescription).getAttribute("textContent");
    }

    /**
     * Gets the dtc issue count
     *
     * @param issueType - issue type
     * @return dtc issue count as string
     */
    public String getDtcIssueCount(String issueType) {
        By locator = By.xpath(String.format(
            "//div[contains(text(), '%s')]/ancestor::div[@class='table-row ']/div[3]/div/div",
            issueType)
        );
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Opens investigation tab
     *
     * @return new page object
     */
    public InvestigationPage openInvestigationTab() {
        pageUtils.waitForElementAndClick(investigationTab);
        return new InvestigationPage(driver);
    }

    /**
     * Opens tolerances tab
     *
     * @return new page object
     */
    public TolerancesPage openTolerancesTab() {
        pageUtils.waitForElementAndClick(tolerancesTab);
        return new TolerancesPage(driver);
    }

    /**
     * Opens threads tab
     *
     * @return new page object
     */
    public ThreadsPage openThreadsTab() {
        pageUtils.waitForElementAndClick(threadsTab);
        return new ThreadsPage(driver);
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

    /**
     * Enum to get severity value
     */
    public enum Severity {
        WARNING("exclamation-circle"),
        FAILED("ban");

        private final String severityValue;

        Severity(String severityValue) {

            this.severityValue = severityValue;
        }

        public String getSeverityValue() {

            return severityValue;
        }
    }
}
