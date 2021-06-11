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

import java.util.List;
import java.util.stream.Stream;

/**
 * @author cfrith
 */

@Slf4j
public class GuidanceIssuesPage extends LoadableComponent<GuidanceIssuesPage> {

    @FindBy(css = ".design-guidance-detail-card .apriori-table")
    private WebElement chartTable;

    @FindBy(css = ".table-head .checkbox-icon")
    private WebElement gcdCheckbox;

    @FindBy(css = ".issue-description")
    private WebElement issueDescription;

    @FindBy(xpath = "//span[.='Investigation']")
    private WebElement investigationTab;

    @FindBy(xpath = "//span[.='Tolerances']")
    private WebElement tolerancesTab;

    @FindBy(xpath = "//span[.='Threads']")
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
        pageUtils.waitForElementAppear(chartTable);
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
     * Selects issue
     *
     * @param issueDropdown - the issue dropdown
     */
    private void selectIssue(String issueDropdown) {
        String[] issues = issueDropdown.split(",");

        Stream.of(issues).forEach(issue -> {
            WebElement byChevron = pageUtils.waitForElementToAppear(driver.findElement(By.xpath(String.format("//div[.='%s']/..", issue.trim())))
                .findElement(By.cssSelector("svg[data-icon='chevron-down']")));
            pageUtils.waitForElementAndClick(byChevron);
        });
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
     * Selects the gcd
     *
     * @param gcd - the gcd
     * @return current page object
     */
    private GuidanceIssuesPage selectGcd(String gcd) {
        By byGcd = designGuidanceController.getBy(gcd);
        deSelectAllGcd();
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
     * Selects all gcd checkbox
     *
     * @return current page object
     */
    public GuidanceIssuesPage selectAllGcd() {
        if (!getCheckboxStatus().contains("check")) {
            pageUtils.waitForElementAndClick(gcdCheckbox);
        }
        return this;
    }

    /**
     * Deselects all gcd checkbox
     *
     * @return current page object
     */
    public GuidanceIssuesPage deSelectAllGcd() {
        if (!getCheckboxStatus().equals("square")) {
            pageUtils.waitForElementAndClick(gcdCheckbox);
        }
        return this;
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
