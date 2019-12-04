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
 * @author cfrith
 */

public class MaterialPage extends LoadableComponent<MaterialPage> {

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
        materialUtilizationTab.click();
        return new MaterialUtilizationPage(driver);
    }

    /**
     * Opens the stock tab
     *
     * @return new page object
     */
    public StockPage goToStockTab() {
        pageUtils.waitForElementAndClick(stockTab);
        return new StockPage(driver);
    }

    /**
     * Opens the part nesting tab
     *
     * @return new page object
     */
    public PartNestingPage goToPartNestingTab() {
        pageUtils.waitForElementAndClick(partNestingTab);
        return new PartNestingPage(driver);
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
    public MaterialPage expandPanel() {
        pageUtils.waitForElementAndClick(chevronButton);
        return this;
    }

    /**
     * Gets the button as a webelement
     *
     * @return the button as webelement
     */
    public WebElement getPartNestingButton() {
        pageUtils.waitForElementToAppear(panelDetails);
        pageUtils.waitForElementToAppear(partNestingTab);
        return partNestingTab;
    }

    /**
     * Clicks the help button
     *
     * @return the current page object
     */
    public MaterialPage clickHelp() {
        pageUtils.waitForElementAndClick(helpButton);
        return this;
    }

    public String getChildPageTitle() {
        return pageUtils.windowHandler().getTitle();
        // TODO remove code duplication
    }
}
