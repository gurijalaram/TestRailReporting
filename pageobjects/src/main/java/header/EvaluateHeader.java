package main.java.header;

import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.evaluate.PublishPage;
import main.java.pages.explore.ExplorePage;
import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kpatel
 */

public class EvaluateHeader extends GenericHeader {

    private static Logger logger = LoggerFactory.getLogger(EvaluateHeader.class);

    @FindBy(css = "button[data-ap-comp='costButton']")
    private WebElement costButton;

    @FindBy(css = ".bottom .popover-content .gwt-HTML")
    private WebElement costLabelPopover;

    @FindBy(css = "li[data-ap-comp='costButton']")
    private WebElement costLabel;

    @FindBy(css = "button.gwt-Button.btn.btn-primary")
    private WebElement dialogCostButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private static final String COST_UP_TO_DATE = "Cost up to\n" + "Date";

    public EvaluateHeader(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Cost the scenario. Enter 'null' if the cost label is expected to be default label
     * @param costText - the text for the cost label
     * @return current page object
     */
    public EvaluatePage costScenario(String costText) {
        costButton.click();
        dialogCostButton.click();
        costText = costText == "Success" ? COST_UP_TO_DATE : costText;
        getCostLabel();
        checkCostLabel(costText);
        return new EvaluatePage(driver);
    }

    /**
     * Checks the text in the cost label
     * @param costText - the cost label text
     * @return true or false
     */
    public boolean checkCostLabel(String costText) {
        return costLabelPopover(costText);
    }

    /**
     * Publish the scenario
     * @return new page object
     */
    public ExplorePage publishScenario() {
        return publishScenario();
    }

    /**
     * Publish the scenario
     * @param status - the status dropdown
     * @param costMaturity - the cost maturity dropdown
     * @param assignee - the assignee
     * @return new page object
     */
    public PublishPage publishScenario(String status, String costMaturity, String assignee) {
        return publishScenario(status, costMaturity, assignee);
    }

    /**
     * Wait for cost label popover
     *
     * @return boolean true/false
     */
    public boolean costLabelPopover(String costText) {
        return pageUtils.waitForElementToAppear(costLabelPopover).getText().equalsIgnoreCase(costText);
    }

    /**
     * Gets cost label
     *
     * @return webelement
     */
    public WebElement getCostLabel() {
        return pageUtils.waitForElementToAppear(costLabel);
    }
}