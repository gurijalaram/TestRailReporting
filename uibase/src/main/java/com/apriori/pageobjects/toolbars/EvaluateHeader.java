package com.apriori.pageobjects.toolbars;

import com.apriori.pageobjects.pages.evaluate.CostingJobPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.CostingLabelEnum;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author kpatel
 */

public class EvaluateHeader extends GenericHeader {

    private static final Logger logger = LoggerFactory.getLogger(EvaluateHeader.class);

    @FindBy(css = "button[data-ap-comp='costButton']")
    private WebElement costButton;

    @FindBy(css = ".bottom .popover-content .gwt-HTML")
    private WebElement costLabelPopover;

    @FindBy(css = ".bottom .popover-content .gwt-HTML")
    private List<WebElement> costLabelPopoverElement;

    @FindBy(css = "li[data-ap-comp='costButton']")
    private WebElement costLabel;

    @FindBy(css = "button.gwt-Button.btn.btn-primary")
    private WebElement dialogCostButton;

    @FindBy(css = ".panel.panel-default.spacer .panel-body")
    private WebElement loadingImage;

    private WebDriver driver;
    private PageUtils pageUtils;

    public EvaluateHeader(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Cost the scenario
     *
     * @return current page object
     */
    public EvaluatePage costScenario() {
        return costScenario(3);
    }

    /**
     * Cost the scenario
     *
     * @return current page object
     */
    public EvaluatePage costScenario(int timeoutInMinutes) {
        pageUtils.waitForElementToAppear(costLabel);
        pageUtils.waitForElementAndClick(costButton);
        new CostingJobPage(driver).selectCostButton();
        checkForCostLabel(timeoutInMinutes);
        return new EvaluatePage(driver);
    }

    /**
     * Gets cost label
     *
     * @return true/false
     */
    public boolean getCostLabel(String text) {
        return pageUtils.textPresentInElement(costLabel, text);
    }

    /**
     * Method to check cost label contains/doesn't contain text
     */
    private void checkForCostLabel() {
        checkForCostLabel(2);
    }

    /**
     * Method to check cost label contains/doesn't contain text
     */
    private void checkForCostLabel(int timeoutInMinutes) {
        pageUtils.textPresentInElement(costLabel, CostingLabelEnum.COSTING_IN_PROGRESS.getCostingText());
        pageUtils.textNotPresentInElement(costLabel, CostingLabelEnum.COSTING_IN_PROGRESS.getCostingText(), timeoutInMinutes);
    }

    /**
     * Clicks the cost status
     * @param className - the class the method should return
     * @param <T>       - the generic declaration type
     * @return generic page object
     */
    public <T> T clickCostStatus(Class<T> className) {
        pageUtils.waitForElementAndClick(costLabel);
        return PageFactory.initElements(driver, className);
    }
}