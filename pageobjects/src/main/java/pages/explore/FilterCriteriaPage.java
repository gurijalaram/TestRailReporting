package main.java.pages.explore;

import main.java.utils.PageUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class FilterCriteriaPage extends LoadableComponent<PrivateWorkspacePage> {

    private final Logger logger = LoggerFactory.getLogger(FilterCriteriaPage.class);

    @FindBy(css = "h3.modal-title")
    WebElement modalDialog;

    @FindBy(css = "input[data-ap-field='privateWorkspace']")
    WebElement privateCheckBox;

    @FindBy(css = "input[data-ap-field='publicWorkspace']")
    WebElement publicCheckBox;

    @FindBy(css = "input[data-ap-field='partScenarioType']")
    WebElement partCheckBox;

    @FindBy(css = "input[data-ap-field='assemblyScenarioType']")
    WebElement assemblyCheckBox;

    @FindBy(css = "input[data-ap-field='comparisonScenarioType']")
    WebElement comparisonCheckBox;

    @FindBy(css = "select[data-ap-field='criteria0.criteriaName']")
    WebElement attributeDropdown;

    @FindBy(css = "select[data-ap-field='criteria0.operation']")
    WebElement conditionDropdown;

    @FindBy(css = "input[data-ap-field='criteria0.value']")
    WebElement valueInput;

    @FindBy(css = "button.btn.dropdown-toggle.selectpicker.btn-default")
    WebElement valueInputDropdown;

    @FindBy(css = "button.gwt-Button.btn.btn-primary")
    WebElement applyButton;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
    WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private String attribute;

    public FilterCriteriaPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(modalDialog);
        pageUtils.waitForElementToAppear(attributeDropdown);
    }

    protected FilterCriteriaPage clearAllCheckBoxes() {
        WebElement[] checkBoxes = {privateCheckBox, publicCheckBox, partCheckBox, assemblyCheckBox, comparisonCheckBox};
        for (WebElement checkBox : checkBoxes) {
            if (checkBox.getAttribute("checked") != null) {
                checkBox.click();
            }
        }
        return this;
    }

    protected FilterCriteriaPage setScenarioType(String type) {
        switch (type) {
            case "Part":
                partCheckBox.click();
                break;
            case "Assembly":
                assemblyCheckBox.click();
                break;
            case "Comparison":
                comparisonCheckBox.click();
                break;
            default:
                throw new IllegalArgumentException("The type: " + type + " is not found");
        }
        return this;
    }

    protected FilterCriteriaPage setPrivateWorkSpace() {
        privateCheckBox.click();
        return this;
    }

    protected FilterCriteriaPage setPublicWorkspace() {
        publicCheckBox.click();
        return this;
    }

    protected FilterCriteriaPage selectAttribute(String attribute) {
        new Select(attributeDropdown).selectByVisibleText(attribute);
        this.attribute = attribute;
        return this;
    }

    protected FilterCriteriaPage selectCondition(String condition) {
        new Select(conditionDropdown).selectByVisibleText(condition);
        return this;
    }

    protected FilterCriteriaPage inputValue(String input) {
        valueInput.click();
        pageUtils.clearInput(valueInput);
        valueInput.sendKeys(input);
        return this;
    }

    protected FilterCriteriaPage selectValue(String input) {
        valueInputDropdown.sendKeys(input);
        valueInputDropdown.sendKeys(Keys.ESCAPE);
        return this;
    }

    public PrivateWorkspacePage apply() {
        applyButton.click();
        return new PrivateWorkspacePage(driver);
    }

    public PrivateWorkspacePage cancel() {
        cancelButton.click();
        return new PrivateWorkspacePage(driver);
    }

    protected enum Attribute {
        PROCESSGROUP("Process Group"),
        VPE("VPE"),
        STATUS("Status"),
        LASTSAVED("Last Saved"),
        COSTMATURITY("Cost Maturity"),
        ASSIGNEE("Assignee");

        private String attributeValue;

        Attribute(String attributeValue) {
            this.attributeValue = attributeValue;
        }

        public String getAttributeValue() {
            return attributeValue;
        }
    }

    protected void setTypeOfValue(String value) {
        if (Arrays.stream(Attribute.values()).map(Attribute::getAttributeValue).anyMatch(values -> values.equalsIgnoreCase(attribute))) {
            selectValue(value);
        } else {
            inputValue(value);
        }
    }
}