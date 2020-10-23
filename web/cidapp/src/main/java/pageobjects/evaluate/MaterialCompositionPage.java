package pageobjects.evaluate;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaterialCompositionPage extends LoadableComponent<MaterialCompositionPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(MaterialCompositionPage.class);

    @FindBy(xpath = "//label[.='Type']/..//div[contains(@class,'apriori-select form-control')]")
    private WebElement typeDropdown;

    @FindBy(xpath = "//label[.='Selection Method']/..//div[contains(@class,'apriori-select form-control')]")
    private WebElement methodDropdown;

    @FindBy(xpath = "//label[normalize-space(text())='Search']/..//input")
    private WebElement searchInput;

    @FindBy(xpath = "//div[@class='apriori-table   scrollable-y ']")
    private WebElement materialTable;

    private WebDriver driver;
    private PageUtils pageUtils;

    public MaterialCompositionPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
     * @param materialType - the type
     * @return current page object
     */
    public MaterialCompositionPage selectType(String materialType) {
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
    public MaterialCompositionPage selectionMethod(String selectionMethod) {
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
    public MaterialCompositionPage search(String text) {
        pageUtils.waitForElementToAppear(searchInput).clear();
        searchInput.sendKeys(text);
        return this;
    }
}
