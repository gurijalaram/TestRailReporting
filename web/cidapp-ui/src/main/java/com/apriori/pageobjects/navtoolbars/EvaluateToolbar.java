package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.NewCostingLabelEnum;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author cfrith
 */

public class EvaluateToolbar extends ExploreToolbar {

    private static final Logger logger = LoggerFactory.getLogger(EvaluateToolbar.class);

    @FindBy(css = "[id='qa-sub-header-cost-button'] button")
    private WebElement costButton;

    @FindBy(css = ".alert")
    private WebElement costLabel;

    @FindBy(css = ".scenario-state-preview [data-icon='cog']")
    private List<WebElement> cogIcon;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;

    public EvaluateToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementToAppear(costButton);
        pageUtils.waitForElementsToNotAppear(cogIcon);
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
        pageUtils.waitForElementToAppear(costLabel);
        pageUtils.waitForElementAndClick(costButton);
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
        pageUtils.waitForElementAndClick(costButton);
        return this;
    }

    /**
     * Method to check cost label is in correct state
     */
    public void waitForCostLabel(int timeoutInMinutes) {
        By costingDialog = By.xpath("//h5[.='Cost Scenario']");

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

    /**
     * Select the publish button
     *
     * @param <T> - the object type
     * @return generic page object
     */
    public <T> T publishScenario(Class<T> klass) {
        modalDialogController.publishScenario(klass);
        return PageFactory.initElements(driver, klass);
    }
}
