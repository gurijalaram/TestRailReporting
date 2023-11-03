package com.apriori.cid.ui.pageobjects.navtoolbars;

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
        By costingDialog = By.cssSelector("[role='dialog'] .dialog-title");

        pageUtils.waitForElementToAppear(costingDialog);
        pageUtils.waitForElementsToNotAppear(costingDialog);
        pageUtils.waitForElementsToNotAppear(By.xpath(String.format("//div[.='%s']", NewCostingLabelEnum.COSTING_IN_PROGRESS.getCostingText())), timeoutInMinutes);
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
}
