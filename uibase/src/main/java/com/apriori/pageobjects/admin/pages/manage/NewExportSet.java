package com.apriori.pageobjects.admin.pages.manage;

import com.apriori.pageobjects.admin.header.AdminHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @FindBy(css = "select[id='DTE_Field_scenarioKey.typeName']")
    private WebElement componentTypes;

    private WebDriver driver;
    private PageUtils pageUtils;

    public NewExportSet(WebDriver driver) {
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
        //pageUtils.waitForElementAndClick(componentTypeDropdown);
        pageUtils.selectDropdownOption(componentTypes, componentType);
        return this;
    }
}
