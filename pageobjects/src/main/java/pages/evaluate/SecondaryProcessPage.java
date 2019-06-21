package main.java.pages.evaluate;

import main.java.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecondaryProcessPage extends LoadableComponent<SecondaryProcessPage> {

    private final Logger logger = LoggerFactory.getLogger(SecondaryProcessPage.class);

    @FindBy(css = ".secondary-treatments-panel-header-btn")
    private WebElement clearAllButton;

    @FindBy(css = "div[data-ap-comp='secondaryTreatmentsTable'] .gwt-Label")
    private WebElement treatmentTable;

    @FindBy(css = "div[data-ap-comp='secondaryTreatmentsTable'] div.v-grid-scroller-vertical")
    private WebElement processScroller;

    @FindBy(css = "input[data-ap-comp='plateThickness.radioButtons.standard")
    private WebElement standardRadioButton;

    @FindBy(css = "input[data-ap-comp='plateThickness.radioButtons.user']")
    private WebElement desiredRadioButton;

    @FindBy(css = "input[data-ap-field='plateThickness.modeValues.user.value']")
    private WebElement thicknessInput;

    @FindBy(css = "select[data-ap-field='platingMethod.modeValues.platingMethod.storedListValue']")
    private WebElement platingMethodSelect;

    @FindBy(css = "button.gwt-Button.btn.btn-primary")
    private WebElement applyButton;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public SecondaryProcessPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(treatmentTable);
    }

    /**
     * Select the secondary process checkbox
     *
     * @param secondaryProcess - the secondary process
     * @return current page object
     */
    public SecondaryProcessPage selectSecondaryProcess(String secondaryProcess) {
        By processBox = By.xpath("//div[@data-ap-comp='secondaryTreatmentsTable']//div[.='" + secondaryProcess + "']/ancestor::tr//input[@class='gwt-SimpleCheckBox']");
        pageUtils.scrollToElement(processBox, processScroller);
        return this;
    }

    /**
     * Selects the apply button
     *
     * @return new page object
     */
    protected EvaluatePage apply() {
        applyButton.click();
        return new EvaluatePage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    protected EvaluatePage cancel() {
        cancelButton.click();
        return new EvaluatePage(driver);
    }
}
