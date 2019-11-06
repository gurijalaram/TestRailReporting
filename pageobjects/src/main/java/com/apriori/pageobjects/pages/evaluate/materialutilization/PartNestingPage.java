package com.apriori.pageobjects.pages.evaluate.materialutilization;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.stock.StockPage;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author smccaffrey
 */

public class PartNestingPage extends LoadableComponent<PartNestingPage> {

    private final Logger logger = LoggerFactory.getLogger(PartNestingPage.class);

    @FindBy(css = "label[data-ap-field='selectedSheet']")
    private WebElement selectedSheet;

    @FindBy(css = "label[data-ap-field='blankSize']")
    private WebElement blankSize;

    @FindBy(css = "label[data-ap-field='partsPerSheet']")
    private WebElement partsPerSheet;

    @FindBy(css = "input[data-ap-comp='COMPUTED']")
    private WebElement rectangularNesting;

    @FindBy(css = "input[data-ap-comp='TRUE_PART']")
    private WebElement truePartNesting;

    @FindBy(css = "input[data-ap-comp='MACHINE_DEFAULT']")
    private WebElement machineDefaultPartNesting;

    @FindBy(css = "div[data-ap-comp='materialNestingDiagram']")
    private WebElement partNestingDiagram;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PartNestingPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(selectedSheet);
        pageUtils.waitForElementToAppear(partNestingDiagram);
    }

    /**
     * Opens the material & utilization tab
     *
     * @return new page object
     */
    public MaterialUtilizationPage goToMaterialUtilizationTab() {
        materialUtilizationTab.click();
        return new MaterialUtilizationPage(driver);
    }

    /**
     * Opens the stock tab
     *
     * @return new page object
     */
    public StockPage goToStockTab() {
        stockTab.click();
        return new StockPage(driver);
    }

    /**
     * Closes the material & utilization
     *
     * @return new page object
     */
    public EvaluatePage closeMaterialAndUtilizationPanel() {
        pageUtils.waitForElementAndClick(closePanelButton);
        return new EvaluatePage(driver);
    }

    /**
     * Gets material information
     *The fully qualified name of the property must be entered as the locator looks for an exact match eg. "(kg / m^3)"
     * @param info
     * @return string
     */
    public String getMaterialInfo(String info) {
        pageUtils.waitForElementToAppear(materialPropertiesTree);
        By propertiesInfo = By.xpath("//table[@class='table material-grid'] //td[.='" + info + "']/following-sibling::td");
        return driver.findElement(propertiesInfo).getAttribute("innerText");
    }

    /**
     * Expands the panel
     *
     *@return current page object
     */
    public PartNestingPage expandPanel() {
        pageUtils.waitForElementAndClick(chevronButton);
        return this;
    }
}
