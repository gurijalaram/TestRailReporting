package com.apriori.pageobjects.reports.pages.view.reports;

import com.apriori.pageobjects.reports.header.ReportsPageHeader;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GenericReportPage extends ReportsPageHeader {

    private static Logger logger = LoggerFactory.getLogger(GenericReportPage.class);
    private Map<String, WebElement> exportSetMap = new HashMap<>();
    private Map<String, WebElement> assemblyMap = new HashMap<>();

    @FindBy(xpath = "//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='All Plastic']/div/a")
    private WebElement allPlasticExportSet;

    @FindBy(xpath = "//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='All.Default Scenarios']/div/a")
    private WebElement allDefaultScenariosExportSet;

    @FindBy(xpath = "//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='design outlier']/div/a")
    private WebElement designOutlierExportSet;

    @FindBy(xpath = "//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='Design Outlier 2']/div/a")
    private WebElement designOutlier2ExportSet;

    @FindBy(xpath = "//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='DTC Machining Data']/div/a")
    private WebElement dtcMachiningDataExportSet;

    @FindBy(xpath = "//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='qa test 1']/div/a")
    private WebElement qatest1ExportSet;

    @FindBy(xpath = "//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='Sheet Metal DTC']/div/a")
    private WebElement sheetMetalDtcExportSet;

    @FindBy(xpath = "//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='Test DTC']/div/a")
    private WebElement testDtcExportSet;

    @FindBy(xpath = "//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='top-level']/div/a")
    private WebElement topLevelExportSet;

    @FindBy(xpath = "//label[@title='Assembly Select']/div/div/div/a")
    private WebElement currentAssemblyElement;

    @FindBy(css = "li[title='SUB-ASSEMBLY (Initial)'] > div > a")
    private WebElement subAssemblyOption;

    @FindBy(css = "li[title='SUB-SUB-ASM (Initial)'] > div > a")
    private WebElement subSubAsmOption;

    @FindBy(css = "li[title='TOP-LEVEL (Initial)'] > div > a")
    private WebElement topLevelOption;

    @FindBy(xpath = "//label[@title='Currency Code']/div/div/div/a")
    private WebElement currentCurrencyElement;

    @FindBy(css = "li[title='USD'] > div > a")
    private WebElement usdCurrencyOption;

    @FindBy(css = "li[title='GBP'] > div > a")
    private WebElement gbpCurrencyOption;

    @FindBy(id = "apply")
    private WebElement applyButton;

    @FindBy(id = "ok")
    private WebElement okButton;

    @FindBy(id = "reset")
    private WebElement resetButton;

    @FindBy(id = "cancelButton")
    private WebElement cancelButton;

    @FindBy(id = "save")
    private WebElement saveButton;

    @FindBy(id = "loading")
    private WebElement loadingPopup;

    private WebDriver driver;
    private PageUtils pageUtils;

    public GenericReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        initialiseExportSetHashMap();
        initialiseAssemblyHashMap();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Selects specified export set
     * @return current page object
     */
    public GenericReportPage selectExportSet(String exportSet) {
        pageUtils.waitForElementAndClick(exportSetMap.get(exportSet));
        return this;
    }

    /**
     * Generic scroll method
     * @return current page object
     */
    public GenericReportPage scrollDownInputControls() {
        pageUtils.waitForElementToAppear(currentCurrencyElement);
        pageUtils.scrollWithJavaScript(currentCurrencyElement, true);
        return this;
    }

    /**
     * Sets specified assembly
     * @return current page object
     */
    public GenericReportPage setAssembly(String assemblyName) {
        currentAssemblyElement.click();
        assemblyMap.get(assemblyName).click();
        return this;
    }

    /**
     * Checks current currency selection, fixes if necessary
     * @param currency
     * @return current page object
     */
    public GenericReportPage checkCurrencySelected(String currency) {
        currentCurrencyElement.click();
        if (!currentCurrencyElement.getAttribute("title").equals(currency)) {
            switch (currency) {
                case "USD":
                    usdCurrencyOption.click();
                    break;
                case "GBP":
                    gbpCurrencyOption.click();
                    break;
            }
        }
        return this;
    }

    /**
     * Clicks apply and ok
     * @return Assembly Details Report page object
     */
    public AssemblyDetailsReportPage clickApplyAndOk() {
        pageUtils.waitForElementToAppear(applyButton);
        applyButton.click();
        pageUtils.waitForElementToAppear(okButton);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        okButton.click();
        return new AssemblyDetailsReportPage(driver);
    }

    /**
     * Initialises export set hash map
     */
    private void initialiseExportSetHashMap() {
        exportSetMap.put("All Plastic", allPlasticExportSet);
        exportSetMap.put("All.Default Scenarios", allDefaultScenariosExportSet);
        exportSetMap.put("design outlier", designOutlierExportSet);
        exportSetMap.put("Design Outlier 2", designOutlier2ExportSet);
        exportSetMap.put("DTC Machining Data", dtcMachiningDataExportSet);
        exportSetMap.put("qa test 1", qatest1ExportSet);
        exportSetMap.put("Sheet Metal DTC", sheetMetalDtcExportSet);
        exportSetMap.put("Test DTC", testDtcExportSet);
        exportSetMap.put("top-level", topLevelExportSet);
    }

    /**
     * Initialises assembly hash map
     */
    private void initialiseAssemblyHashMap() {
        assemblyMap.put("SUB-ASSEMBLY (Initial)", subAssemblyOption);
        assemblyMap.put("SUB-SUB-ASM (Initial)", subSubAsmOption);
        assemblyMap.put("TOP-LEVEL (Initial)", topLevelOption);
    }
}
