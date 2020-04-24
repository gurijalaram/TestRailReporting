package com.apriori.pageobjects.admin.pages.manage;

import com.apriori.pageobjects.admin.header.AdminHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class NewExportSet extends AdminHeader {

    private final Logger logger = LoggerFactory.getLogger(NewExportSet.class);

    @FindBy(css = "[class='DTE_Header_Content']")
    private WebElement newExportSetTitle;

    @FindBy(css = "[id='DTE_Field_name']")
    private WebElement setName;

    @FindBy(css = "select[title='Scenario']")
    private WebElement exportScopeDropdown;

    @FindBy(css = "button[data-id='DTE_Field_scenarioKey.typeName']")
    private WebElement componentTypeDropdown;

    @FindBy(xpath = "//a[contains(@data-normalized-text, 'Assembly')]")
    private WebElement assemblyOption;

    @FindBy(xpath = "//a[contains(@data-normalized-text, 'Part')]")
    private WebElement partOption;

    @FindBy(xpath = "//button[@title='Roll-up']/../div/ul/li[3]/a")
    private WebElement rollUpOption;

    @FindBy(xpath = "//a[contains(@data-normalized-text, 'Dynamic Roll-up')]")
    private WebElement dynamicRollUpOption;

    @FindBy(css = "select[id='DTE_Field_scenarioKey.typeName']")
    private WebElement componentTypes;

    private WebDriver driver;
    private PageUtils pageUtils;
    private HashMap<String, WebElement> componentTypeMap;

    public NewExportSet(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        initialiseComponentTypeHashMap();
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(setName);
    }

    private void setInput(WebElement locator, String value) {
        pageUtils.waitForElementToAppear(locator).clear();
        locator.sendKeys(value);
    }

    /**
     * Inputs Set Name
     *
     * @param text - the text
     * @return current page object
     */
    public NewExportSet inputSetName(String text) {
        pageUtils.waitForElementAppear(setName);
        setInput(setName, text);
        return this;
    }

    /**
     * Selects the export scope from the dropdown
     *
     * @param exportScope - workspace dropdown
     * @return current page object
     */
    public NewExportSet selectExportScope(String exportScope) {
        pageUtils.selectDropdownOption(exportScopeDropdown, exportScope);
        return this;
    }

    /**
     * Selects the export scope from the dropdown
     *
     * @param componentType - component Type dropdown
     * @return current page object
     */
    public NewExportSet selectComponentType(String componentType) {
        WebElement elementToUse = componentTypeMap.get(componentType);
        pageUtils.waitForElementAndClick(componentTypeDropdown);
        pageUtils.waitForElementAndClick(elementToUse);
        return this;
    }

    /**
     * Initialises Hash Map to simplify Element selection
     */
    private void initialiseComponentTypeHashMap() {
        componentTypeMap = new HashMap<>();
        componentTypeMap.put("Part", partOption);
        componentTypeMap.put("Assembly", assemblyOption);
        componentTypeMap.put("Roll-up", rollUpOption);
        componentTypeMap.put("Dynamic Roll-up", dynamicRollUpOption);
    }
}
