package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.NewCostingLabelEnum;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class EvaluateToolbar extends ExploreToolbar {

    private static final Logger logger = LoggerFactory.getLogger(EvaluateToolbar.class);

    @FindBy(css = "[id='qa-sub-header-cost-button'] button")
    private WebElement costButton;

    @FindBy(css = ".alert")
    private WebElement costLabel;

    private PageUtils pageUtils;
    private WebDriver driver;

    public EvaluateToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementAppear(costButton);
    }

    /**
     * Cost the scenario
     *
     * @return new page object
     */
    public EvaluatePage costScenario() {
        pageUtils.waitForElementToAppear(costLabel);
        pageUtils.waitForElementAndClick(costButton);
        checkForCostLabel(2);
        return new EvaluatePage(driver);
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
        checkForCostLabel(timeoutInMinutes);
        return new EvaluatePage(driver);
    }

    /**
     * Method to check cost label contains/doesn't contain text
     */
    public void checkForCostLabel(int timeoutInMinutes) {
        pageUtils.waitForElementToAppear(costLabel);
        pageUtils.textPresentInElement(costLabel, NewCostingLabelEnum.COSTING_IN_PROGRESS.getCostingText());
        pageUtils.textNotPresentInElement(costLabel, NewCostingLabelEnum.COSTING_IN_PROGRESS.getCostingText(), timeoutInMinutes);
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
     * @return hex code as string
     */
    public String getCostColour() {
        return Color.fromString(pageUtils.waitForElementAppear(costLabel).getCssValue("background-color")).asHex();
    }
}
