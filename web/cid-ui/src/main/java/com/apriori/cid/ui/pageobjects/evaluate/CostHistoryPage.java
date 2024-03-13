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

    @FindBy(css = "p[data-testid='scenario-history-dialog-error-message']")
    private WebElement noPlotMessage;

    @FindBy(css = "div[role='dialog'] p:first-child")
    private List<WebElement> iterationList;

    @FindBy(css = "div[role='tooltip']")
    private WebElement changeSummary;

    @FindBy(id = "qa-change-summary-column-1-Secondary Processes-Machining")
    private WebElement leftColSecondaryProcessMachining;

    @FindBy(css = "g[class*='xAxis']")
    private WebElement xAxis;

    @FindBy(css = "g[class*='recharts-xAxis xAxis'] tspan")
    private List<WebElement> displayedChartIterations;

    @FindBy(css = "g line[orientation='left'] + g g")
    private List<WebElement> leftAxisTicks;

    @FindBy(css = "g line[orientation='right'] + g g")
    private List<WebElement> rightAxisTicks;

    @FindBy(id = "qa-scenario-history-primary-select")
    private WebElement primaryAxisDropDown;

    @FindBy(id = "qa-scenario-history-secondary-select")
    private WebElement secondaryAxisDropDown;

    @FindBy(css = "button[aria-label='Download as image']")
    private WebElement downloadViewButton;

    @FindBy(css = "div[data-testid='scenario-history-download-preview'] h1")
    private WebElement downloadPreviewTitle;

    @FindBy(css = "div[data-testid='scenario-history-download-preview'] h1 + p")
    private WebElement downloadPreviewDate;

    @FindBy(css = "div[data-testid='scenario-history-download-primary-key'")
    private WebElement downloadPreviewFirstAxisLegend;

    @FindBy(css = "div[data-testid='scenario-history-download-secondary-key']")
    private WebElement downloadPreviewSecondAxisLegend;

    @FindBy(css = "svg[data-testid='logo']")
    private WebElement downloadPreviewWatermark;

    @FindBy(css = "div[role='dialog'] span[data-testid='checkbox']")
    private WebElement dataTableCheckbox;

    @FindBy(xpath = "//p[contains(text(),'Iteration')]")
    private List<WebElement> dataTableIterations;

    @FindBy(css = "div[data-testid='scenario-history-download-preview'] button[data-testid='secondary-button']")
    private WebElement back;

    @FindBy(css = "div[data-testid='scenario-history-download-preview'] button[data-testid='primary-button']")
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
     * Get the message displayed if no graph is available
     *
     * @return - String containing displayed message
     */
    public String getPlotAvailableMessage() {
        pageUtils.waitForElementToAppear(noPlotMessage);
        return noPlotMessage.getText();
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
     * Get List of all iterations in table
     *
     * @return List of Strings containing iteration names displayed in table
     */
    public List<String> displayedTableIterations() {
        return iterationList.stream().map(WebElement::getText).collect(Collectors.toList());
    }


    /**
     * Get name of iteration hide/show icon
     *
     * @param iterationNum - The number of the specified iteration
     *
     * @return String with value of data-icon attribute | eye or eye-slash
     */
    public String iterationDisplayIcon(Integer iterationNum) {
        return showHideIterationIcon(iterationNum).getAttribute("data-icon");
    }

    /**
     * Is specified Show/Hide icon displayed
     *
     * @param iterationNum - The number of the specified iteration
     *
     * @return Boolean representation of icon visibility
     */
    public Boolean showHideIconDisplayed(Integer iterationNum) {
        By locator = By.xpath(String.format(iterationXPath, iterationNum) + "/../following-sibling::button");
        return pageUtils.waitForVisibilityOfElement(locator);
    }

    /**
     * Show/Hide a given iteration
     *
     * @param iterationNum - The number of the specified iteration
     *
     * @return - This PO
     */
    public CostHistoryPage showHideIteration(Integer iterationNum) {
        pageUtils.waitForElementAndClick(showHideIterationButton(iterationNum));
        return this;
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
     * Get currently selected primary axis
     *
     * @return - String of currently selected value for primary axis
     */
    public String selectedPrimaryAxis() {
        pageUtils.waitForElementToAppear(primaryAxisDropDown);
        return primaryAxisDropDown.getText();
    }

    /**
     * Select value for primary axis
     *
     * @param axisName - Name of specified axis
     *
     * @return This PO
     */
    public CostHistoryPage setPrimaryAxis(String axisName) {
        By requestedAxis = By.xpath(String.format("//div[.='%s']/div/div", axisName));

        pageUtils.waitForElementAndClick(primaryAxisDropDown);

        return this;
    }

    /**
     * Get currently selected secondary axis
     *
     * @return - String of currently selected value for primary axis
     */
    public String selectedSecondaryAxis() {
        pageUtils.waitForElementToAppear(primaryAxisDropDown);
        return secondaryAxisDropDown.getText();
    }

    /**
     * Select value for secondary axis
     *
     * @param axisName - Name of specified axis
     *
     * @return This PO
     */
    public CostHistoryPage setSecondaryAxis(String axisName) {
        By requestedAxis = By.xpath(String.format("//div[.='%s']/div/div", axisName));

        pageUtils.waitForElementAndClick(secondaryAxisDropDown);
        pageUtils.waitForElementAndClick(requestedAxis);
        return this;
    }

    /**
     * Get list of displayed iterations in chart x-axis
     *
     * @return List of strings of displayed iterations
     */
    public List<String> displayedChartIterations() {
        pageUtils.waitForElementsToAppear(displayedChartIterations);
        return displayedChartIterations.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * Get list of tick labels from left axis
     *
     * @return List of strings containing tick labels
     */
    public List<String> leftAxisLabels() {
        pageUtils.waitForElementsToAppear(leftAxisTicks);
        return leftAxisTicks.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * Get list of tick labels from left axis
     *
     * @return List of strings containing tick labels
     */
    public List<String> rightAxisLabels() {
        pageUtils.waitForElementsToAppear(rightAxisTicks);
        return rightAxisTicks.stream().map(WebElement::getText).collect(Collectors.toList());
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
     * Get the Download Preview Title
     *
     * @return - String of graph title
     */
    public String downloadPreviewTitle() {
        return downloadPreviewTitle.getText();
    }

    /**
     * Get the Download Preview Date
     *
     * @return - String of Date
     */
    public String downloadPreviewDate() {
        return downloadPreviewDate.getText();
    }

    /**
     * Get the Download Preview First Axis Name
     *
     * @return - String of First Axis Name
     */
    public String downloadPreviewFirstAxisName() {
        return downloadPreviewFirstAxisLegend.getText();
    }

    /**
     * Get the Download Preview Second Axis Name
     *
     * @return - String of Second Axis Name
     */
    public String downloadPreviewSecondAxisName() {
        return downloadPreviewSecondAxisLegend.getText();
    }

    /**
     * Get the display state of Download Preview watermark
     *
     * @return - Boolean of display status of watrermark
     */
    public Boolean downloadPreviewWatermarkDisplayed() {
        return downloadPreviewWatermark.isDisplayed();
    }

    /**
     * Click the Data Table checkbox
     *
     * @return this PO
     */
    public CostHistoryPage clickDataTableCheckbox() {
        pageUtils.waitForElementToAppear(dataTableCheckbox);
        dataTableCheckbox.click();
        return this;
    }

    /**
     * Get list of iterations shown in Data Table
     *
     * @return List of Strings of Iterations shown in Data Table
     */
    public List<String> dataTableIterationsList() {
        return dataTableIterations.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * Clicks back button from Download View
     *
     * @return - This PO
     */
    public CostHistoryPage clickBackButton() {
        pageUtils.waitForElementAndClick(back);
        return this;
    }

    /**
     * Clicks download button from Download View
     *
     * @return - This PO
     */
    public CostHistoryPage clickDownloadButton() {
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

    /**
     * Get WebElement for show/hide iteration button
     *
     * @param iterationNum - The number of the specified iteration
     *
     * @return - WebElement of the specified iteration's button
     */
    private WebElement showHideIterationButton(Integer iterationNum) {
        By locator = By.xpath(String.format(iterationXPath, iterationNum) + "/../following-sibling::button");
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator);
    }

    /**
     * Get WebElement for show/hide iteration button
     *
     * @param iterationNum - The number of the specified iteration
     *
     * @return - WebElement of the specified iteration's button
     */
    private WebElement showHideIterationIcon(Integer iterationNum) {
        By locator = By.xpath(String.format(iterationXPath, iterationNum) + "/../following-sibling::button/span/*[name()='svg']");
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator);
    }
}
