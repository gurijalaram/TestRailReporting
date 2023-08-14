package com.apriori.pageobjects.evaluate.materialprocess;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.help.HelpDocPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class PartNestingPage extends LoadableComponent<PartNestingPage> {

    @FindBy(css = "[id='qa-part-nesting-utilization-mode-select'] .apriori-select [data-icon='chevron-down']")
    private WebElement utilizationModeDropDown;

    @FindBy(css = ".stock-width")
    private WebElement stockWidth;

    @FindBy(css = ".stock-length")
    private WebElement stockLength;

    @FindBy(css = "[id='qa-part-nesting-utilization-mode-select'] > label")
    private WebElement utilizationMode;

    @FindBy(xpath = "//button[.='Part Nesting']")
    private WebElement partNestingTab;

    private PageUtils pageUtils;
    private WebDriver driver;
    private PanelController panelController;

    public PartNestingPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue(partNestingTab.getAttribute("class").contains("active"), "Part Nesting tab was not selected");
    }

    /**
     * Gets the Selected Sheet text value on the page
     *
     * @param label - the label
     * @return String
     */
    public String getNestingInfo(String label) {
        By selectedSheet = By.xpath(String.format("//span[.='%s']/following-sibling::span", label));
        return pageUtils.waitForElementToAppear(selectedSheet).getAttribute("textContent");
    }

    /**
     * Gets the Utilization Mode page source for assertion
     *
     * @param text - the text
     * @return Boolean
     */
    public boolean isUtilizationModeInfo(String text) {
        return pageUtils.waitForElementToAppear(utilizationMode).getAttribute("textContent").equalsIgnoreCase(text);
    }

    /**
     * Gets the Stock Width value on the page
     *
     * @return String
     */
    public String getStockWidthInfo() {
        return pageUtils.waitForElementToAppear(stockWidth).getAttribute("textContent");
    }

    /**
     * Gets the Stock Length value on the page
     *
     * @return String
     */
    public String getStockLengthInfo() {
        return pageUtils.waitForElementToAppear(stockLength).getAttribute("textContent");
    }

    /**
     * Uses type ahead to input the status
     *
     * @param status - the status
     * @return current page object
     */
    public PartNestingPage selectUtilizationModeDropDown(String status) {
        pageUtils.typeAheadSelect(utilizationModeDropDown, "qa-part-nesting-utilization-mode-select", status);
        return this;
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
     * Closes current panel
     *
     * @return new page object
     */
    public EvaluatePage closePanel() {
        return panelController.closePanel();
    }
}


