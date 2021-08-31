package com.apriori.pageobjects.pages.evaluate;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MaterialSelectorPage extends LoadableComponent<MaterialSelectorPage> {

    @FindBy(xpath = "//label[.='Type']/following-sibling::div[contains(@class,'apriori-select form-control')]")
    private WebElement typeDropdown;

    @FindBy(xpath = "//label[.='Selection Method']/following-sibling::div[contains(@class,'apriori-select form-control')]")
    private WebElement methodDropdown;

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

    private WebDriver driver;
    private PageUtils pageUtils;

    public MaterialSelectorPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(materialTable);
        pageUtils.waitForElementAppear(rowText);
    }

    /**
     * Selects the material composition type
     *
     * @param materialType - the type
     * @return current page object
     */
    public MaterialSelectorPage selectType(String materialType) {
        pageUtils.waitForElementAndClick(typeDropdown);
        By type = By.xpath(String.format("//button[.='%s']", materialType));
        pageUtils.scrollWithJavaScript(driver.findElement(type), true).click();
        return this;
    }

    /**
     * Selects the method
     *
     * @param selectionMethod - the selection method
     * @return current page object
     */
    public MaterialSelectorPage selectionMethod(String selectionMethod) {
        pageUtils.waitForElementAndClick(methodDropdown);
        By method = By.xpath(String.format("//button[.='%s']", selectionMethod));
        pageUtils.scrollWithJavaScript(driver.findElement(method), true).click();
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
        By material = By.xpath(String.format("//div[@role='row']//div[contains(text(),'%s')]", materialName));
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
}
