package com.apriori.pageobjects.navtoolbars;

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

    public EvaluateToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementToAppear(costButton);
    }

    /**
     * Cost the scenario
     *
     * @return new page object
     */
    public EvaluatePage costScenario() {
        pageUtils.waitForElementToAppear(costLabel);
        pageUtils.waitForElementAndClick(costButton);
        waitForCostLabel(2);
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
        waitForCostLabel(timeoutInMinutes);
        return new EvaluatePage(driver);
    }

    /**
     * Method to check cost label is in correct state
     */
    public void waitForCostLabel(int timeoutInMinutes) {
        this.isLoaded();
        pageUtils.waitForElementsToNotAppear(By.xpath(String.format("//div[.='%s']", NewCostingLabelEnum.COSTING_IN_PROGRESS.getCostingText())), timeoutInMinutes);
        pageUtils.waitForElementsToNotAppear(cogIcon);
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
}
