package main.java.pages.evaluate.process;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessPage extends LoadableComponent<ProcessPage> {

    private final Logger logger = LoggerFactory.getLogger(ProcessPage.class);

    @FindBy(css = "div[data-ap-comp='processCycleTime']")
    private WebElement routingTable;

    @FindBy(css = ".highcharts-xaxis-labels tspan")
    private WebElement routingLabels;

    @FindBy(css = "select[data-ap-field='chartSelectionField']")
    private WebElement contributionsDropdown;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ProcessPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(routingTable);
    }

    /**
     * Gets the routing labels
     * @return string
     */
    public String getRoutingLabels() {
        return routingLabels.getText();
    }

    /**
     * Selects the contribution dropdown
     * @param contribution - the contribution
     * @return current page object
     */
    public ProcessPage selectContribution(String contribution) {
        new Select(contributionsDropdown).selectByVisibleText(contribution);
        return this;
    }
}
