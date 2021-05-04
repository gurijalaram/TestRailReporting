package com.apriori.pageobjects.pages.evaluate.designguidance;

import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.help.HelpDocPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author cfrith
 */

public class GuidanceIssuesPage extends LoadableComponent<GuidanceIssuesPage> {

    private static final Logger logger = LoggerFactory.getLogger(GuidanceIssuesPage.class);

    @FindBy(css = ".design-guidance-detail-card .apriori-table")
    private WebElement chartTable;

    @FindBy(css = ".table-head .checkbox-icon")
    private WebElement gcdCheckbox;

    @FindBy(css = ".issue-description")
    private WebElement issueDescription;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;

    public GuidanceIssuesPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
        selectIssue(issueDropdown);
        selectIssueType(issueType);
        selectGcd(gcd.trim());
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
        By byIssueType = By.cssSelector(String.format("[id*='Attribute-%s']", issueType.substring(0, 1).toLowerCase() + issueType.substring(1).replace(" ", "").trim()));
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
        WebElement byGcd = pageUtils.waitForElementToAppear(driver.findElement(By.xpath(String.format("//div[.='%s']/..", gcd))).findElement(By.cssSelector("[role='cell']")));
        pageUtils.waitForElementAndClick(byGcd);
        return this;
    }

    /**
     * Gets severity column
     *
     * @return string
     */
    public String getSeverity(String issueType) {
        List<WebElement> cells = driver.findElements(By.xpath(String.format("//div[.='%s']/..//div[@role='cell']", issueType.trim())));
        return cells.get(1).findElement(By.cssSelector("svg")).getAttribute("data-icon");
    }

    /**
     * Gets count column
     *
     * @return string
     */
    public String getGcdCount(String issueType) {
        return getColumn(issueType, 2);
    }

    /**
     * Gets current column
     *
     * @return string
     */
    public String getGcdCurrent(String gcd) {
        return getColumn(gcd, 1);
    }

    /**
     * Gets suggested column
     *
     * @return string
     */
    public String getGcdSuggested(String gcd) {
        return getColumn(gcd, 2);
    }

    /**
     * Gets column
     *
     * @param gcd - the gcd
     * @return string
     */
    private String getColumn(String gcd, int column) {
        List<WebElement> cells = driver.findElements(By.xpath(String.format("//div[.='%s']/..//div[@role='cell']", gcd.trim())));
        return cells.get(column).findElement(By.cssSelector(".cell-text")).getAttribute("textContent");
    }

    /**
     * Selects all gcd checkbox
     *
     * @return current page object
     */
    public GuidanceIssuesPage selectAll() {
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
    public GuidanceIssuesPage deSelectAll() {
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
