package com.apriori.cid.ui.pageobjects.navtoolbars;

import com.apriori.cid.ui.pageobjects.evaluate.ChangeSummaryPage;
import com.apriori.cid.ui.pageobjects.evaluate.CostHistoryPage;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * @author cfrith
 */

@Slf4j
public class EvaluateToolbar extends ExploreToolbar {

    @FindBy(css = "[id='qa-sub-header-scenario-history-button'] button")
    private WebElement historyButton;

    @FindBy(css = "div[data-testid='scenario-state-preview']")
    private WebElement costLabel;

    @FindBy(css = "[id='qa-sub-header-cost-button'] button")
    private WebElement costButton;

    @FindBy(css = ".scenario-state-preview [data-icon='cog']")
    private List<WebElement> cogIcon;

    @FindBy(css = "[id='qa-sub-header-refresh-view-button'] button")
    private WebElement refreshButton;

    @FindBy(css = "div[data-testid='cost-mode-toggle'] div button[value='SIMULATE']")
    private WebElement aprioriCostModeButton;

    @FindBy(css = "div[data-testid='cost-mode-toggle'] div button[value='MANUAL']")
    private WebElement manualCostModeButton;

    @FindBy(css = "div[id='qa-sub-header-save-as-button'] button")
    private WebElement saveAsButton;

    @FindBy(css = "div[data-testid='apriori-alert']")
    private WebElement saveLabel;

    private PageUtils pageUtils;
    private WebDriver driver;

    public EvaluateToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementsToNotAppear(cogIcon);
    }

    /**
     * Check the enabled status of Progress button
     *
     * @return - Boolean representation of whether button is enabled / disabled
     */
    public Boolean isProgressButtonEnabled() {
        return pageUtils.isElementEnabled(historyButton);
    }

    /**
     * Click the History button
     *
     * @return - Cost History modal PO
     */
    public CostHistoryPage clickHistory() {
        pageUtils.waitForElementAndClick(historyButton);
        return new CostHistoryPage(driver);
    }

    /**
     * Cost the scenario
     *
     * @return new page object
     */
    public EvaluatePage costScenario() {
        return costScenario(2);
    }

    /**
     * Cost the scenario
     *
     * @param timeoutInMinutes - timeout in minutes
     * @return current page object
     */
    public EvaluatePage costScenario(int timeoutInMinutes) {
        clickCostButton();
        waitForCostLabel(timeoutInMinutes);
        return new EvaluatePage(driver);
    }

    /**
     * Clicks the cost button
     *
     * @return current page object
     */
    public EvaluateToolbar clickCostButton() {
        pageUtils.waitForElementToAppear(costLabel);
        clickCostButton(EvaluateToolbar.class);
        return this;
    }

    /**
     * Method to check cost label is in correct state
     */
    public void waitForCostLabel(int timeoutInMinutes) {
        waitForLabel(NewCostingLabelEnum.COSTING_IN_PROGRESS, timeoutInMinutes);
    }

    /**
     * Method to check cost label is in correct state
     */
    public void waitForSaveLabel(int timeoutInMinutes) {
        waitForLabel(NewCostingLabelEnum.SAVING_IN_PROGRESS, timeoutInMinutes);
    }

    /**
     * Method to check cost label is in correct state
     *
     * @param costLabel        - the cost label type
     * @param timeoutInMinutes - time out in minutes
     * @return - new page object
     */
    public EvaluatePage waitForCostLabelNotContain(NewCostingLabelEnum costLabel, int timeoutInMinutes) {
        By byCostLabel = By.xpath(String.format("//div[.='%s']", costLabel.getCostingText()));
        pageUtils.waitForElementToAppear(byCostLabel);
        pageUtils.waitForElementsToNotAppear(byCostLabel, timeoutInMinutes);
        return new EvaluatePage(driver);
    }

    /**
     * Gets cost label
     *
     * @return boolean
     */
    public boolean isCostLabel(NewCostingLabelEnum label) {
        pageUtils.waitForElementToAppear(costLabel);
        return pageUtils.textPresentInElement(costLabel, label.getCostingText());
    }

    /**
     * Gets background colour of cost label
     *
     * @return hex code as string
     */
    public String getCostColour() {
        return Color.fromString(pageUtils.waitForElementToAppear(costLabel).getCssValue("background-color")).asHex();
    }

    /**
     * Confirms to go ahead with costing with a Yes or No
     *
     * @param buttonLabel - "Yes" or "No"
     * @return - new page object
     */
    public EvaluatePage confirmCost(String buttonLabel) {
        By byButton = By.xpath(String.format("//button[contains(text(),'%s')]", buttonLabel));
        pageUtils.waitForElementAndClick(byButton);
        return new EvaluatePage(driver);
    }

    /**
     * Open Change Summary pop-up from Simulate Cost Mode view
     *
     * @return - Change Summary PO
     */
    public ChangeSummaryPage changeSummarySimulate() {
        pageUtils.waitForElementToAppear(costLabel);
        pageUtils.mouseMove(costButton);
        return new ChangeSummaryPage(driver);
    }

    /**
     * Open Change Summary pop-up from Manual Cost Mode view
     *
     * @return - Change Summary PO
     */
    public ChangeSummaryPage changeSummaryManual() {
        pageUtils.waitForElementToAppear(saveLabel);
        pageUtils.mouseMove(saveLabel);
        return new ChangeSummaryPage(driver);
    }

    /**
     * Select 'Simulate' Cost Mode
     *
     * @return - New EvaluatePage PO
     */
    public SwitchCostModePage clickSimulateModeButton() {
        pageUtils.waitForElementAndClick(aprioriCostModeButton);
        return new SwitchCostModePage(driver);
    }

    /**
     * Determine if 'aPriori' Cost Mode selected
     *
     * @return - Boolean of mode state
     */
    public Boolean isSimulateCostModeSelected() {
        pageUtils.waitForElementToAppear(aprioriCostModeButton);
        return Boolean.parseBoolean(aprioriCostModeButton.getAttribute("aria-pressed"));
    }

    /**
     * Select 'Manual' Cost Mode
     *
     * @return - New EvaluatePage PO
     */
    public SwitchCostModePage clickManualModeButton() {
        pageUtils.waitForElementAndClick(manualCostModeButton);
        return new SwitchCostModePage(driver);
    }

    /**
     * Select 'Manual' Cost Mode while scenario in Uncosted state
     *
     * @return - New EvaluatePage PO
     */
    public EvaluatePage clickManualModeButtonWhileUncosted() {
        pageUtils.waitForElementAndClick(manualCostModeButton);
        return new EvaluatePage(driver);
    }

    /**
     * Determine if 'Manual' Cost Mode selected
     *
     * @return - Boolean of mode state
     */
    public Boolean isManualCostModeSelected() {
        pageUtils.waitForElementToAppear(manualCostModeButton);
        return Boolean.parseBoolean(manualCostModeButton.getAttribute("aria-pressed"));
    }

    /**
     * Check if Cost Mode Toggle buttons are disabled
     *
     * @return - Boolean of enabled state for cost mode toggle buttons
     */
    public Boolean isCostModeToggleEnabled() {
        pageUtils.waitForElementToAppear(aprioriCostModeButton);
        return aprioriCostModeButton.isEnabled() && manualCostModeButton.isEnabled();
    }

    /**
     * Check if Manual Mode Toggle button is disabled
     *
     * @return - Boolean of enabled state for manual mode toggle button
     */
    public Boolean isManualModeToggleEnabled() {
        pageUtils.waitForElementToAppear(manualCostModeButton);
        return manualCostModeButton.isEnabled();
    }

    /**
     * Check state of Manual Costing Save As button
     *
     * @return - Boolean of Manual Costing Save As button enabled state
     */
    public Boolean isSaveButtonEnabled() {
        pageUtils.waitForElementToAppear(saveAsButton);
        return saveAsButton.isEnabled();
    }

    /**
     * Click Manual Costing Save As button
     *
     * @return - New Evaluate Page
     */
    public EvaluatePage clickSaveButton() {
        pageUtils.waitForElementAndClick(saveAsButton);
        waitForSaveLabel(2);
        return new EvaluatePage(driver);
    }

    /**
     * Wait for the status label to finish Costing/Saving
     *
     * @param inProgressLabel - Enum for Cost/Save label
     * @param timeoutInMinutes - Time in minutes before wait times out
     */
    private void waitForLabel(NewCostingLabelEnum inProgressLabel, int timeoutInMinutes) {
        By costingDialog = By.cssSelector("[role='dialog'] .dialog-title");

        pageUtils.waitForElementToAppear(costingDialog);
        pageUtils.waitForElementsToNotAppear(costingDialog);
        pageUtils.waitForElementsToNotAppear(By.xpath(String.format("//div[.='%s']", inProgressLabel.getCostingText())), timeoutInMinutes);
    }

}
