package com.pageobjects.pages.evaluate;

import com.apriori.utils.PageUtils;

import com.utils.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author cfrith
 */

public class MaterialCompositionPage extends LoadableComponent<MaterialCompositionPage> {

    private final Logger logger = LoggerFactory.getLogger(MaterialCompositionPage.class);

    @FindBy(css = "select[data-ap-field='materialType']")
    private WebElement typeDropdown;

    @FindBy(css = "select[data-ap-field='materialSelections']")
    private WebElement methodDropdown;

    @FindBy(css = "div[data-ap-comp='materialSelectionTable'] .v-grid-cell")
    private WebElement materialTable;

    @FindBy(css = "div[data-ap-comp='materialSelectionTable'] div.v-grid-scroller-vertical")
    private WebElement materialScroller;

    @FindBy(css = ".material-selection-dialog button.btn.btn-primary")
    private WebElement applyButton;

    @FindBy(css = ".material-selection-dialog button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public MaterialCompositionPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(materialTable);
    }

    /**
     * Selects the material composition type
     *
     * @param type - the type
     * @return current page object
     */
    public MaterialCompositionPage type(String type) {
        pageUtils.selectDropdownOption(typeDropdown, type);
        return this;
    }

    /**
     * Selects the method
     *
     * @param method - the selection method
     * @return current page object
     */
    public MaterialCompositionPage method(String method) {
        new Select(methodDropdown).selectByVisibleText(method);
        return this;
    }

    /**
     * Selects the material name
     *
     * @param materialName - the material name
     * @return current page object
     */
    public MaterialCompositionPage selectMaterialComposition(String materialName) {
        pageUtils.waitForElementAppear(findMaterialComposition(materialName)).click();
        return this;
    }

    /**
     * Selects the material name
     *
     * @param materialName - the material name
     * @return current page object
     */
    public WebElement findMaterialComposition(String materialName) {
        setCompositionTableTopOfPage();
        By material = By.xpath("//div[@data-ap-comp='materialSelectionTable']//tbody//td[.='" + materialName + "']");
        return pageUtils.scrollToElement(material, materialScroller, Constants.PAGE_DOWN);
    }

    /**
     * Gets the list of material types from the dropdown
     *
     * @return hashset as duplicates need to be removed
     */
    public HashSet<String> getListOfMaterialTypes() {
        return new HashSet<>(Arrays.asList(pageUtils.waitForElementToAppear(typeDropdown, typeDropdown.getAttribute("innerText")).getText().split("\n")));
    }

    /**
     * Selects the apply button
     *
     * @return new page object
     */
    public EvaluatePage apply() {
        pageUtils.waitForElementAndClick(applyButton);
        return new EvaluatePage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public EvaluatePage cancel() {
        pageUtils.waitForElementAndClick(cancelButton);
        return new EvaluatePage(driver);
    }

    /**
     * Sets the scroll the scrollbar to the top of the page by selecting the first option in the dropdown
     */
    private void setCompositionTableTopOfPage() {
        new Select(typeDropdown).getOptions()
            .stream()
            .filter(option -> option.getText().equalsIgnoreCase("All"))
            .forEach(WebElement::click);
    }
}
