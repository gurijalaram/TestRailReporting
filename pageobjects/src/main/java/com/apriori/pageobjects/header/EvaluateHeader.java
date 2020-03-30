package com.apriori.pageobjects.header;

import com.apriori.pageobjects.pages.evaluate.CostingJobPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.utils.PageUtils;
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

    private static Logger logger = LoggerFactory.getLogger(EvaluateHeader.class);

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
        pageUtils.waitForElementAndClick(costButton);
        new CostingJobPage(driver).selectCostButton();
        checkForCostLabel(timeoutInMinutes);
        checkForImage();
        return new EvaluatePage(driver);
    }

    /**
     * Cost the scenario
     *
     * @return current page object
     */
    public EvaluatePage costScenario(int timeoutInMinutes, int imageTimeoutInMinutes) {
        pageUtils.waitForElementAndClick(costButton);
        new CostingJobPage(driver).selectCostButton();
        checkForCostLabel(timeoutInMinutes);
        checkForImage(imageTimeoutInMinutes);
        return new EvaluatePage(driver);
    }

    /**
     * Gets cost label
     *
     * @return true/false
     */
    public boolean getCostLabel(String text) {
        return pageUtils.checkElementContains(costLabel, text);
    }

    /**
     * Method to check for the loading image displayed/not displayed
     */
    public EvaluateHeader checkForImage() {
        checkForImage(0.5);
        return this;
    }

    /**
     * Method to check for the loading image displayed/not displayed
     */
    public EvaluateHeader checkForImage(double timeoutInMinutes) {
        pageUtils.isElementDisplayed(loadingImage);
        pageUtils.waitForElementNotDisplayed(loadingImage, timeoutInMinutes);
        driver.navigate().refresh();
        return this;
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
        pageUtils.checkElementContains(costLabel, CostingLabelEnum.COSTING_IN_PROGRESS.getCostingText());
        pageUtils.checkElementNotContain(costLabel, CostingLabelEnum.COSTING_IN_PROGRESS.getCostingText(), timeoutInMinutes);
    }
}