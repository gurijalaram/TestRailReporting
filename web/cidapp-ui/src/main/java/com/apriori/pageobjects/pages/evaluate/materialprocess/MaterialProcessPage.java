package com.apriori.pageobjects.pages.evaluate.materialprocess;

import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.help.HelpDocPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class MaterialProcessPage extends LoadableComponent<MaterialProcessPage> {

    private static final Logger logger = LoggerFactory.getLogger(MaterialProcessPage.class);

    @FindBy(css = ".tab.active [data-icon='clock']")
    private WebElement processesTabActive;

    @FindBy(css = "div[dir='ltr']")
    private WebElement chartContainer;

    @FindBy(css = "[id='qa-process-totals-section'] div.left")
    private WebElement totalChevron;

    @FindBy(css = "[id='qa-process-details-section'] div.left")
    private WebElement processDetailsChevron;

    @FindBy(css = "[id='qa-process-options-section'] div.left")
    private WebElement optionsChevron;

    @FindBy(xpath = "//button[.='Material Utilization']")
    private WebElement materialUtilizationTab;

    @FindBy(xpath = "//button[.='Stock']")
    private WebElement stockTab;

    @FindBy(xpath = "//button[.='Part Nesting']")
    private WebElement partNestingTab;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;

    public MaterialProcessPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(processesTabActive);
        pageUtils.waitForElementAppear(chartContainer);
    }

    /**
     * Gets total result
     *
     * @param label - the label
     * @return double
     */
    public double getTotalResult(String label) {
        By costResult = By.xpath(String.format("//span[.='%s']/following-sibling::span[@class='property-value']", label));
        return Double.parseDouble(pageUtils.waitForElementToAppear(costResult).getAttribute("textContent").replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Checks the specified total is displayed
     *
     * @param label - the label
     * @param value - the value
     * @return true/false
     */
    public boolean isTotalResultDisplayed(String label, String value) {
        By costResult = By.xpath(String.format("//span[.='%s']/following-sibling::span[.='%s']", label, value));
        pageUtils.waitForElementToAppear(costResult);
        return driver.findElement(costResult).isDisplayed();
    }

    /**
     * Opens material utilization tab
     * @return new page object
     */
    public MaterialUtilizationPage openMaterialUtilizationTab() {
        pageUtils.waitForElementAndClick(materialUtilizationTab);
        return new MaterialUtilizationPage(driver);
    }

    /**
     * Go to stock tab
     * @return new page object
     */
    public StockPage goToStockTab() {
        pageUtils.waitForElementAndClick(stockTab);
        return new StockPage(driver);
    }

    /**
     * Closes current panel
     *
     * @return new page object
     */
    public EvaluatePage closePanel() {
        return panelController.closePanel();
    }

    /**
     * Opens the help page
     *
     * @return new page object
     */
    public HelpDocPage openHelp() {
        return panelController.openHelp();
    }

    /**
     * Go to Part Nesting tab
     * @return new page object
     */
    public PartNestingPage goToPartNestingTab() {
        pageUtils.waitForElementAndClick(partNestingTab);
        return new PartNestingPage(driver);
    }
}
