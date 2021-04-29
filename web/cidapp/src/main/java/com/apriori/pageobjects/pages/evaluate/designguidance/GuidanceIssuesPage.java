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

    public GuidanceIssuesPage selectIssueTypeGcd(String issueDropdown, String issueType, String gcd) {
        String[] issues = issueDropdown.split(",");

        Stream.of(issues).forEach(x -> {
            WebElement byChevron = driver.findElement(By.xpath(String.format("//div[.='%s']/..", x.trim()))).findElement(By.cssSelector("svg[data-icon='chevron-down']"));
            pageUtils.waitForElementAndClick(byChevron);
        });

        selectIssueType(issueType);
        selectGcd(gcd);
        return this;
    }

    public GuidanceIssuesPage selectIssueType(String issueType) {

        return this;
    }

    public GuidanceIssuesPage selectGcd(String gcd) {
//        WebElement byGcd = driver.findElement(By.xpath(String.format("//div[.='%s']/..", gcd))).findElement(By.cssSelector(".checkbox-cell"));
//        pageUtils.waitForElementAndClick(byGcd);
        return this;
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
