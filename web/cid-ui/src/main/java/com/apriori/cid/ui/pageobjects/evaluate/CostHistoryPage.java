package com.apriori.cid.ui.pageobjects.evaluate;

import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CostHistoryPage extends LoadableComponent<CostHistoryPage> {
    @FindBy(css = "h2")
    private WebElement header;

    @FindBy(css = "h2 button")
    private WebElement close;

    @FindBy(css = "div[role='dialog'] p:first-child")
    private List<WebElement> iterationList;

    @FindBy(css = "div[role='tooltip']")
    private WebElement changeSummary;

    @FindBy(id = "qa-change-summary-column-1-Secondary Processes-Machining")
    private WebElement leftColSecondaryProcessMachining;

    @FindBy(css="g[class*='xAxis']")
    private WebElement xAxis;

    @FindBy(css="g[class*='recharts-xAxis xAxis'] tspan")
    private List<WebElement> displayedChartIterations;

    @FindBy(css="button[aria-label='Download as image']")
    private WebElement downloadViewButton;

    @FindBy(css="div[data-testid='scenario-history-download-preview'] button[data-testid='secondary-button']")
    private WebElement back;

    @FindBy(css="div[data-testid='scenario-history-download-preview'] button[data-testid='primary-button']")
    private WebElement download;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;
    private final String iterationXPath = "//p[.='Iteration %d']";

    public CostHistoryPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(header);
    }

    /**
     * Click the Close button
     *
     * @return - EvaluatePage PO
     */
    public EvaluatePage close() {
        pageUtils.waitForElementAndClick(close);
        return new EvaluatePage(driver);
    }

    /**
     * Get the number of displayed iterations
     *
     * @return - Integer of the number of iterations displayed
     */
    public Integer iterationCount() {
        return iterationList.size();
    }

    /**
     * Get WebElement for iteration hide/show icon
     *
     * @param iterationNum - The number of the specified iteration
     *
     * @return String with value of data-icon attribute
     */
    public String iterationDisplayIcon(Integer iterationNum) {
        By locator = By.xpath(String.format(iterationXPath, iterationNum) + "/../following-sibling::button/span/*[name()='svg']");
        return driver.findElement(locator).getAttribute("data-icon");
    }

    /**
     * Hover over info icon for given iteration
     *
     * @param iterationNum - The number of the specified iteration
     */
    public ChangeSummaryPage openChangeSummary(Integer iterationNum) {
        pageUtils.waitForElementToAppear(iterationBy(iterationNum));
        pageUtils.mouseMove(iteration(iterationNum));
        pageUtils.waitForElementToAppear(iterationInfoTooltipIcon(iterationNum));
        pageUtils.mouseMove(iterationInfoTooltipIcon(iterationNum));
        return new ChangeSummaryPage(driver);
    }

    /**
     * Get list of displayed iterations in chart x-axis
     *
     * @return List of strings of displayed iterations
     */
    public List<String> displayedChartIterations() {
        return displayedChartIterations.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * Click the Download button
     *
     * @return - this PO
     */
    public CostHistoryPage openDownloadView() {
        pageUtils.waitForElementAndClick(downloadViewButton);
        return this;
    }

    /**
     * Clicks back button from Download View
     *
     * @return - This PO
     */
    public CostHistoryPage back() {
        pageUtils.waitForElementAndClick(back);
        return this;
    }

    /**
     * Clicks download button from Download View
     *
     * @return - This PO
     */
    public CostHistoryPage download() {
        pageUtils.waitForElementAndClick(download);
        return this;
    }

    /**
     * Get Web Element for specified iteration div
     *
     * @param iterationNum - The number of the specified iteration
     *
     * @return WebElement of specified iteration's container div
     */
    private WebElement iteration(Integer iterationNum) {
        By locator = By.xpath(String.format(iterationXPath, iterationNum) + "/..");
        return driver.findElement(locator);
    }


    /**
     * Get By for specified iteration div
     *
     * @param iterationNum - The number of the specified iteration
     *
     * @return By of specified iteration's container div
     */
    private By iterationBy(Integer iterationNum) {
        return By.xpath(String.format(iterationXPath, iterationNum) + "/..");
    }

    /**
     * Get Web Element for specified iteration's timestamp
     *
     * @param iterationNum - The number of the specified iteration
     *
     * @return WebElement of specified iteration's timestamp
     */
    private WebElement iterationTimestamp(Integer iterationNum) {
        By locator = By.xpath(String.format(iterationXPath, iterationNum) + "/..//p[last()]");
        return driver.findElement(locator);
    }

    /**
     * Get WebElement for iteration info tooltip icon
     *
     * @param iterationNum - The number of the specified iteration
     *
     * @return WebElement of specified iteration's info tooltip icon
     */
    private WebElement iterationInfoTooltipIcon(Integer iterationNum) {
        By locator = By.xpath(String.format(iterationXPath, iterationNum) + "//*[@data-icon='circle-info']");
        return driver.findElement(locator);
    }
}
