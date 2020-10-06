package pageobjects.navtoolbars;

import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.CostingLabelEnum;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvaluateToolbar extends ExploreToolbar{

    private final Logger LOGGER = LoggerFactory.getLogger(EvaluateToolbar.class);

    @FindBy(xpath = "//button[.='Cost']")
    private WebElement costButton;

    @FindBy(xpath = "div[class~='scenario-state-button']")
    private WebElement costLabel;

    private PageUtils pageUtils;
    private WebDriver driver;

    public EvaluateToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Cost the scenario
     *
     * @return current page object
     */
    public EvaluateToolbar costScenario() {
        pageUtils.waitForElementAndClick(costButton);
        checkForCostLabel(2);
        return this;
    }

    /**
     * Method to check cost label contains/doesn't contain text
     */
    public void checkForCostLabel(int timeoutInMinutes) {
        pageUtils.textPresentInElement(costLabel, CostingLabelEnum.COSTING_IN_PROGRESS.getCostingText());
        pageUtils.textNotPresentInElement(costLabel, CostingLabelEnum.COSTING_IN_PROGRESS.getCostingText(), timeoutInMinutes);
    }

    /**
     * Gets cost label
     *
     * @return string
     */
    public String getCostLabel() {
        return pageUtils.waitForElementToAppear(costLabel).getText();
    }
}
