package main.java.pages.evaluate.materialutilization;

import main.java.pages.evaluate.EvaluatePage;
import main.java.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @FindBy(css = "button.gwt-Button.btn.btn-primary")
    private WebElement applyButton;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
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
        new Select(typeDropdown).selectByVisibleText(type);
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

    /** Selects the material name
     * @param materialName - the material name
     * @return current page object
     */
    public MaterialCompositionPage materialComposition(String materialName) {
        By material = By.xpath("//div[@data-ap-comp='materialSelectionTable']//tbody//td[.='" + materialName + "']");
        pageUtils.scrollToElement(material, materialScroller).click();
        return this;
    }

    /**
     * Selects the apply button
     *
     * @return new page object
     */
    public EvaluatePage apply() {
        applyButton.click();
        return new EvaluatePage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public EvaluatePage cancel() {
        cancelButton.click();
        return new EvaluatePage(driver);
    }
}
