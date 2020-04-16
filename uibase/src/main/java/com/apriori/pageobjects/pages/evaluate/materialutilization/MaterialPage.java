package com.apriori.pageobjects.pages.evaluate.materialutilization;

import com.apriori.pageobjects.pages.evaluate.materialutilization.stock.StockPage;
import com.apriori.pageobjects.toolbars.EvaluatePanelToolbar;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class MaterialPage extends EvaluatePanelToolbar {

    private final Logger logger = LoggerFactory.getLogger(MaterialUtilizationPage.class);

    @FindBy(css = ".panel.panel-details")
    private WebElement panelDetails;

    @FindBy(css = "div[data-ap-comp='materialDetails']")
    private WebElement materialTable;

    @FindBy(css = "a[href='#materialDetailsTab']")
    private WebElement materialUtilizationTab;

    @FindBy(css = "a[href='#stockDetailsTab']")
    private WebElement stockTab;

    @FindBy(css = "li[id='partNestingTabBookmark']")
    private WebElement partNestingTab;

    @FindBy(css = ".panel .glyphicon-remove")
    private WebElement closePanelButton;

    @FindBy(css = "[data-ap-comp=materialProperties]")
    private WebElement materialPropertiesTree;

    @FindBy(css = "button[data-ap-comp='expandPanelButton']")
    private WebElement chevronButton;

    @FindBy(css = ".details-viewport-part .glyphicon-question-sign")
    private WebElement helpButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public MaterialPage(WebDriver driver) {
        super(driver);
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
        pageUtils.waitForElementToAppear(panelDetails);
        pageUtils.waitForElementToAppear(materialTable);
    }

    /**
     * Opens the material & utilization tab
     *
     * @return new page object
     */
    public MaterialUtilizationPage goToMaterialUtilizationTab() {
        expandPanel();
        materialUtilizationTab.click();
        return new MaterialUtilizationPage(driver);
    }

    /**
     * Opens the stock tab
     *
     * @return new page object
     */
    public StockPage goToStockTab() {
        expandPanel();
        pageUtils.waitForElementAndClick(stockTab);
        return new StockPage(driver);
    }

    /**
     * Opens the part nesting tab
     *
     * @return new page object
     */
    public PartNestingPage goToPartNestingTab() {
        expandPanel();
        pageUtils.waitForElementAndClick(partNestingTab);
        return new PartNestingPage(driver);
    }

    /**
     * Gets material information
     * The fully qualified name of the property must be entered as the locator looks for an exact match eg. "(kg / m^3)"
     *
     * @param info
     * @return string
     */
    public String getMaterialInfo(String info) {
        pageUtils.waitForElementToAppear(materialPropertiesTree);
        By propertiesInfo = By.xpath("//table[@class='table material-grid'] //td[.='" + info + "']/following-sibling::td");
        return driver.findElement(propertiesInfo).getAttribute("innerText");
    }

    /**
     * Gets the button as a webelement
     *
     * @return the button as webelement
     */
    public WebElement getPartNestingButton() {
        return partNestingTab;
    }
}
