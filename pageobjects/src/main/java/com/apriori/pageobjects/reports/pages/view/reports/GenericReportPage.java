package com.apriori.pageobjects.reports.pages.view.reports;

import com.apriori.pageobjects.reports.header.ReportsPageHeader;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GenericReportPage extends ReportsPageHeader {

    private static Logger logger = LoggerFactory.getLogger(GenericReportPage.class);
    private Map<String, WebElement> exportSetMap = new HashMap<>();
    private Map<String, WebElement> assemblyMap = new HashMap<>();
    private Map<String, WebElement> currencyMap = new HashMap<>();

    @FindBy(xpath = "//label[contains(@title, 'Latest Export Date')]/input")
    protected WebElement latestExportDateInput;

    @FindBy(xpath = "//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='top-level']/div/a")
    protected WebElement topLevelExportSet;

    @FindBy(xpath = "//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='Piston Assembly']/div/a")
    protected WebElement pistonAssemblyExportSet;

    @FindBy(xpath = "//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='DTC_Casting']")
    protected WebElement dtcCastingExportSet;

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

    @FindBy(css = "li[title='Select All']")
    private WebElement exportSetSelectAll;

    @FindBy(css = "div[id='exportSetName'] > div > div > div > div > div > span")
    private WebElement availableExportSets;

    @FindBy(css = "div[id='exportSetName'] > div > div > div > div > div:nth-of-type(2) > span")
    private WebElement selectedExportSets;

    @FindBy(xpath = "//div[@title='Single export set selection.']//ul[@class='jr-mSelectlist jr']")
    private WebElement exportSetList;

    @FindBy(css = "li[title='Invert']")
    private WebElement exportSetInvert;

    @FindBy(css = "li[title='Deselect All']")
    private WebElement exportSetDeselect;


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
     *
     * @return current page object
     */
    public GenericReportPage selectExportSet(String exportSet) {
        pageUtils.waitForElementAndClick(exportSetMap.get(exportSet));
        return this;
    }

    /**
     * Generic scroll method
     *
     * @return current page object
     */
    public GenericReportPage scrollDownInputControls() {
        pageUtils.waitForElementToAppear(currentCurrencyElement);
        pageUtils.scrollWithJavaScript(currentCurrencyElement, true);
        return this;
    }

    /**
     * Sets specified assembly
     *
     * @return current page object
     */
    public GenericReportPage setAssembly(String assemblyName) {
        currentAssemblyElement.click();
        pageUtils.checkElementAttribute(currentAssemblyElement, "className", "jr-mSingleselect-input jr jr-isFocused");
        if (!currentAssemblyElement.getAttribute("title").equals(assemblyName)) {
            if (assemblyName.equals("TOP-LEVEL (Initial)")) {
                selectAssemblyOption(3);
            } else if (assemblyName.equals("SUB-SUB-ASM (Initial)")) {
                selectAssemblyOption(2);
            }
            inputBox.sendKeys(Keys.ENTER);
        }
        return this;
    }

    /**
     * Checks current currency selection, fixes if necessary
     *
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
     *
     * @return Assembly Details Report page object
     */
    public AssemblyDetailsReportPage clickApplyAndOk() {
        pageUtils.waitForElementAndClick(applyButton);
        pageUtils.waitForElementAndClick(okButton);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        okButton.click();
        return new AssemblyDetailsReportPage(driver);
    }

    /**
     * Ensures date is set to today
     *
     * @return current page object
     */
    public AssemblyDetailsReportPage ensureDateIsToday() {
        SimpleDateFormat formatterWithoutTime = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat formatterWithTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        if (!latestExportDateInput.getAttribute("value").contains(formatterWithoutTime.format(date).replace("/", "-"))) {
            latestExportDateInput.clear();
            latestExportDateInput.sendKeys(formatterWithTime.format(date).replace("/", "-"));
        }
        return new AssemblyDetailsReportPage(driver);
    }

    /**
     * Select Assembly option dropdown using send keys
     *
     * @param topIndex
     */
    private void selectAssemblyOption(int topIndex) {
        for (int i = 0; i < topIndex; i++) {
            inputBox.sendKeys(Keys.ARROW_DOWN);
        }
    }

    /**
     * Initialises export set hash map
     */
    private void initialiseCurrencyMap() {
        currencyMap.put("GBP", gbpCurrencyOption);
        currencyMap.put("USD", usdCurrencyOption);
    }

    /**
     * Initialises export set hash map
     */
    private void initialiseExportSetHashMap() {
        exportSetMap.put("top-level", topLevelExportSet);
        exportSetMap.put("Piston Assembly", pistonAssemblyExportSet);
        exportSetMap.put("DTC_Casting", dtcCastingExportSet);
    }

    /**
     * Initialises assembly hash map
     */
    private void initialiseAssemblyHashMap() {
        assemblyMap.put("SUB-ASSEMBLY (Initial)", subAssemblyOption);
        assemblyMap.put("SUB-SUB-ASM (Initial)", subSubAsmOption);
        assemblyMap.put("TOP-LEVEL (Initial)", topLevelOption);
    }

    /**
     * Click export set select all button
     *
     * @return current page object
     */
    public GenericReportPage exportSetSelectAll() {
        pageUtils.waitForElementAndClick(exportSetSelectAll);
        String exportSetCount = exportSetList.getAttribute("childElementCount");
        pageUtils.checkElementAttribute(selectedExportSets, "title", exportSetCount);
        return this;
    }

    /**
     * Get number of available export sets
     *
     * @return int
     */
    public int getAvailableExportSetCount() {
        String count = pageUtils.waitForElementToAppear(availableExportSets).getAttribute("title");
        count = count.substring((count.lastIndexOf(" ") + 1));
        return Integer.parseInt(count);
    }

    /**
     * Get number of selected export sets
     *
     * @return int
     */
    public int getSelectedExportSetCount() {
        String count = pageUtils.waitForElementToAppear(selectedExportSets).getAttribute("title");
        count = count.substring((count.lastIndexOf(" ") + 1));
        return Integer.parseInt(count);
    }

    /**
     * Deselect export set
     *
     * @return current page object
     */
    public GenericReportPage deselectExportSet(String exportSet) {
        pageUtils.waitForElementAndClick(exportSetMap.get(exportSet));
        pageUtils.checkElementAttribute(exportSetMap.get(exportSet), "class", "jr-mSelectlist-item jr-isHovered jr");
        return this;
    }

    /**
     * Invert export set selection
     *
     * @return current page object
     */
    public GenericReportPage invertExportSetSelection() {
        int expected = getAvailableExportSetCount() - getSelectedExportSetCount();
        pageUtils.waitForElementAndClick(exportSetInvert);
        pageUtils.checkElementAttribute(selectedExportSets, "title", "Selected: " + expected);
        return this;
    }

    /**
     * Deselect all export sets
     *
     * @return current page object
     */
    public GenericReportPage exportSetDeselectAll() {
        pageUtils.waitForElementAndClick(exportSetDeselect);
        pageUtils.checkElementAttribute(selectedExportSets, "title", "Selected: " + "0");
        return this;
    }


}
