package com.apriori.cid.ui.pageobjects.evaluate;

import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MaterialSelectorPage extends LoadableComponent<MaterialSelectorPage> {

    @FindBy(css = "div[id='qa-material-type-select'] [data-icon='chevron-down']")
    private WebElement typeDropdown;

    @FindBy(css = "[id='qa-material-type-select']")
    private WebElement materialTypeList;

    @FindBy(css = "div[id='qa-material-selection-method-select'] [data-icon='chevron-down']")
    private WebElement modeDropdown;

    @FindBy(xpath = "//label[normalize-space(text())='Search']/following-sibling::input")
    private WebElement searchInput;

    @FindBy(xpath = "//div[@class='table-body']")
    private WebElement materialTable;

    @FindBy(xpath = "//div[@class='cell-content']")
    private WebElement rowText;

    @FindBy(css = ".material-selector .table-body [role='cell']")
    private List<WebElement> materialRow;

    @FindBy(css = ".material-selector [type='Submit']")
    private WebElement submitButton;

    @FindBy(css = ".material-selector [type='button']")
    private WebElement cancelButton;

    @FindBy(css = "div[role='status']")
    private WebElement loadingSpinner;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public MaterialSelectorPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(materialTable);
        pageUtils.waitForElementToAppear(rowText);
        pageUtils.waitForElementNotVisible(loadingSpinner, 1);
    }

    /**
     * Selects the material composition type
     *
     * @param materialType - the type
     * @return current page object
     */
    public MaterialSelectorPage selectType(String materialType) {
        pageUtils.typeAheadSelect(typeDropdown, "qa-material-type-select", materialType);
        return this;
    }

    /**
     * Selects the mode
     * <p>The material mode has to be the fully qualified name eg. Digital Factory Default [Steel, Hot Worked, AISI 1010] </p>
     *
     * @param materialMode - the material mode
     * @return current page object
     */
    public MaterialSelectorPage selectMaterialMode(String materialMode) {
        pageUtils.typeAheadSelect(modeDropdown, materialMode);
        return this;
    }

    /**
     * Enters text in search box
     *
     * @param text - the text
     * @return current page object
     */
    public MaterialSelectorPage search(String text) {
        pageUtils.waitForElementToAppear(searchInput).clear();
        searchInput.sendKeys(text);
        return this;
    }

    /**
     * Selects the material
     *
     * @param materialName - material name
     * @return current page object
     */
    public MaterialSelectorPage selectMaterial(String materialName) {
        By material = By.xpath(String.format("//div[@class='cell-content']//div[.='%s']", materialName));
        pageUtils.waitForElementToAppear(material);
        pageUtils.scrollWithJavaScript(driver.findElement(material), true);

        if (!pageUtils.jsGetParentElement(driver.findElement(By.xpath(String.format("//div[@role='row']//div[.='%s']", materialName)))).getAttribute("class").contains("selected")) {
            pageUtils.waitForElementAndClick(material);
        }
        return this;
    }

    /**
     * Get list of material
     *
     * @return list of string
     */
    public List<String> getListOfMaterials() {
        pageUtils.waitForElementsToAppear(materialRow);
        return materialRow.stream().map(x -> x.getAttribute("textContent").trim()).collect(Collectors.toList());
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        pageUtils.waitForElementAndClick(submitButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Selects the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        pageUtils.waitForElementAndClick(cancelButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Clicks the x button to close the modal
     *
     * @return generic page object
     */
    public <T> T closeDialog(Class<T> klass) {
        return modalDialogController.closeDialog(klass);
    }

    /**
     * Gets list of material types
     *
     * @return list as string
     */
    public List<String> getListOfMaterialTypes() {
        pageUtils.waitForElementAndClick(materialTypeList);
        return Arrays.stream(materialTypeList.getText().split("\n")).filter(x -> !x.equalsIgnoreCase("Type")).collect(Collectors.toList());
    }
}
