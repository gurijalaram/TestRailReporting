package com.apriori.pageobjects.pages.evaluate.materialprocess;

import static org.junit.Assert.assertTrue;

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

import java.util.List;

/**
 * @author cfrith
 */

public class MaterialUtilizationPage extends LoadableComponent<MaterialUtilizationPage> {

    private static final Logger logger = LoggerFactory.getLogger(MaterialUtilizationPage.class);

    @FindBy(xpath = "//div[contains(@class,'apriori-card dark tabbed')]")
    private WebElement panelDetails;

    @FindBy(xpath = "//button[.='Material Utilization']")
    private WebElement materialTab;

    @FindBy(xpath = "//span[contains(.,'Material Carbon Factor')]")
    private List<WebElement> materialCarbonFactor;

    @FindBy(xpath = "//button[.='Stock']")
    private WebElement stockPanel;

    @FindBy(xpath = "(//span[contains(text(), 'Name')])[1]/following-sibling::span")
    private WebElement materialName;

    private PageUtils pageUtils;
    private WebDriver driver;
    private PanelController panelController;

    public MaterialUtilizationPage(WebDriver driver) {
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
        assertTrue("Material Utilization tab was not selected", materialTab.getAttribute("class").contains("active"));
    }

    /**
     * Gets material information
     *
     * @param label
     * @return string
     */
    public String getUtilizationInfo(String label) {
        By info = By.xpath(String.format("//span[.='%s']/following-sibling::span", label));
        return pageUtils.waitForElementToAppear(info).getAttribute("textContent");
    }

    /**
     * Gets the material name
     *
     * @return material name as String
     */
    public String getMaterialName() {
        pageUtils.waitForElementToAppear(materialName);
        return materialName.getText();
    }

    /**
     * Go to stock tab
     * @return new page object
     */
    public StockPage goToStockTab() {
        pageUtils.waitForElementAndClick(stockPanel);
        return new StockPage(driver);
    }

    /**
     * verify if material carobn is presented on material utilization page
     * @return true/false
     */
    public boolean isMaterialCarbonPresent() {
        pageUtils.waitForElementsToAppear(materialCarbonFactor);
        if (materialCarbonFactor.size() == 2) {
            return true;
        }
        return false;

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
}
