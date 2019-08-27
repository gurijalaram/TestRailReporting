package main.java.header;

import main.java.pages.evaluate.CostingJobPage;
import main.java.pages.evaluate.EvaluatePage;
import main.java.utils.PageUtils;
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
     * @return current page object
     */
    public EvaluatePage costScenario() {
        pageUtils.waitForElementAndClick(costButton);
        new CostingJobPage(driver).selectCost();
        checkCostLabelAppears();
        return new EvaluatePage(driver);
    }

    /**
     * Wait for cost label popover
     *
     * @return boolean true/false
     */
    public boolean checkCostLabelAppears() {
        return pageUtils.checkElementVisibleByBoolean(costLabelPopoverElement);
    }


    /**
     * Wait for cost label popover
     * @param timeOut - timeout in milliseconds
     * @return
     */
    public boolean checkCostLabelAppears(int timeOut) {
        return pageUtils.checkElementVisibleByBoolean(costLabelPopoverElement, timeOut);
    }

    /**
     * Gets cost label
     *
     * @return true/false
     */
    public Boolean getCostLabel(String text) {
        return pageUtils.checkElementContains(costLabel, text);
    }
}