package main.java.pages.evaluate.process;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessOptionsPage extends LoadableComponent<ProcessOptionsPage> {

    private final Logger logger = LoggerFactory.getLogger(ProcessOptionsPage.class);

    @FindBy(css = "input[data-ap-comp='numberOfCavities.radioButtons.default']")
    private WebElement defaultValueRadioButton;

    @FindBy(css = "input[data-ap-comp='numberOfCavities.radioButtons.annualVolume']")
    private WebElement annVolumeRadioButton;

    @FindBy(css = "input[data-ap-comp='numberOfCavities.radioButtons.optimize']")
    private WebElement optimizeRadioButton;

    @FindBy(css = "input[data-ap-comp='numberOfCavities.radioButtons.user']")
    private WebElement definedValueRadioButton;

    @FindBy(css = "select[data-ap-field='numberOfCavities.modeValues.user.storedListValue']")
    private WebElement definedValueDropdown;

    @FindBy(css = "input[data-ap-comp='nominalWallThickness.radioButtons.deriveFromPart']")
    private WebElement derivedRadioButton;

    @FindBy(css = "input[data-ap-comp='nominalWallThickness.radioButtons.userOverride']")
    private WebElement overrideNominalRadioButton;

    @FindBy(css = "input[data-ap-field='nominalWallThickness.modeValues.userOverride.value']")
    private WebElement overrideInput;

    @FindBy(css = "input[data-ap-comp='colorantAdd.radioButtons.defaultColorant']")
    private WebElement noColorantRadioButton;

    @FindBy(css = "input[data-ap-comp='colorantAdd.radioButtons.colorantAdded']")
    private WebElement addColorantRadioButton;

    @FindBy(css = "input[data-ap-comp='colorantAdd.radioButtons.userDefined']")
    private WebElement definedColorRadioButton;

    @FindBy(css = "input[data-ap-field='colorantAdd.modeValues.userDefined.value']")
    private WebElement definedInput;

    @FindBy(css = "input[data-ap-comp='regrindAmount.radioButtons.defaultRegrind']")
    private WebElement materialRegrindRadioButton;

    @FindBy(css = "input[data-ap-comp='regrindAmount.radioButtons.userDefinedMode']")
    private WebElement materialDefinedRadioButton;

    @FindBy(css = "input[data-ap-field='regrindAmount.modeValues.userDefinedMode.value']")
    private WebElement materialRegrindInput;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ProcessOptionsPage(WebDriver driver) {
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
    }
}