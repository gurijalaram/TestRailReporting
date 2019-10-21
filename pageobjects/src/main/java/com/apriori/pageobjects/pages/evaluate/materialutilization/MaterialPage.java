package com.apriori.pageobjects.pages.evaluate.materialutilization;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.stock.StockPage;
import com.apriori.pageobjects.utils.PageUtils;

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

    @FindBy(css = "a[href='#partNestingTab']")
    private WebElement partNestingTab;

    @FindBy(css = ".panel .glyphicon-remove")
    private WebElement closePanelButton;

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
}
