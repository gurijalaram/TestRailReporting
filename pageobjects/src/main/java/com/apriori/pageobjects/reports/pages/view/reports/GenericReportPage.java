package com.apriori.pageobjects.reports.pages.view.reports;

import com.apriori.pageobjects.reports.header.ReportsPageHeader;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class GenericReportPage extends ReportsPageHeader {

    private static Logger logger = LoggerFactory.getLogger(GenericReportPage.class);
    private Map<String, WebElement> exportSetMap = new HashMap<>();
    private Map<String, WebElement> assemblyMap = new HashMap<>();
    private Map<String, WebElement> currencyMap = new HashMap<>();

    @FindBy(xpath = "//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='top-level']/div/a")
    private WebElement topLevelExportSet;

    @FindBy(xpath = "//label[@title='Assembly Select']/div/div/div/a")
    private WebElement currentAssemblyElement;

    @FindBy(xpath = "//div[@id='partNumber']/label/div/div/div/a")
    private WebElement currentAssElement;

    @FindBy(xpath = "//a[contains(text(), 'SUB-ASSEMBLY')]")
    private WebElement subAssOption;

    @FindBy(xpath = "//div[@id='assemblySelect']//input")
    private WebElement assemblyInput;

    @FindBy(css = "li[title='SUB-ASSEMBLY (Initial)'] > div > a")
    private WebElement subAssemblyOption;

    @FindBy(xpath = "//label[@title='Assembly Select']//input")
    private WebElement inputBox;

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
        initialiseCurrencyMap();
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
        pageUtils.waitFor(1000);
        pageUtils.checkElementAttribute(currentAssemblyElement, "className", "jr-mSingleselect-input jr jr-isOpen");
        if (!currentAssemblyElement.getAttribute("title").equals(assemblyName)) {
            if (assemblyName.equals("TOP-LEVEL (Initial)")) {
                for (int i = 0; i < 3; i++) {
                    inputBox.sendKeys(Keys.ARROW_DOWN);
                }
            } else if (assemblyName.equals("SUB-SUB-ASM (Initial)")) {
                for (int i = 0; i < 2; i++) {
                    inputBox.sendKeys(Keys.ARROW_DOWN);
                }
            }
            inputBox.sendKeys(Keys.ENTER);
        }
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
            currencyMap.get(currency).click();
        }
        return this;
    }

    /**
     * Clicks apply and ok
     * @return Assembly Details Report page object
     */
    public AssemblyDetailsReportPage clickApplyAndOk() {
        pageUtils.waitForElementAndClick(applyButton);
        pageUtils.waitForElementAndClick(okButton);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        okButton.click();
        return new AssemblyDetailsReportPage(driver);
    }

    private void initialiseCurrencyMap() {
        currencyMap.put("GBP", gbpCurrencyOption);
        currencyMap.put("USD", usdCurrencyOption);
    }

    /**
     * Initialises export set hash map
     */
    private void initialiseExportSetHashMap() {
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
