package main.java.pages.explore;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrivateWorkspacePage extends LoadableComponent<PrivateWorkspacePage> {

    private final Logger logger = LoggerFactory.getLogger(PrivateWorkspacePage.class);

    @FindBy(css = "a.dropdown-toggle.text-center span.glyphicon-file")
    WebElement newFileDropdown;

    @FindBy(css = "button[data-ap-comp='publishScenarioButton']")
    WebElement publishButton;

    @FindBy(css = "button[data-ap-comp='revertScenarioButton']")
    WebElement revertButton;

    @FindBy(css = "span.delete-button")
    WebElement deleteButton;

    @FindBy(css = "span.glyphicons-settings")
    WebElement actionsDropdown;

    @FindBy(css = "select.form-control.input-md.auto-width")
    WebElement workspaceDropdown;

    @FindBy(css = "button[data-ap-nav-dialog='showScenarioSearchCriteria']")
    WebElement filterButton;

    @FindBy(css = "span[data-ap-comp='resultCount']")
    WebElement objectsFound;

    @FindBy(css = "button[data-ap-nav-dialog='showTableViewEditor']")
    WebElement columnsButton;

    @FindBy(css = "button[data-ap-comp='togglePreviewButton']")
    WebElement previewButton;

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

    private WebDriver driver;
    private PageUtils pageUtils;

    public PrivateWorkspacePage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(deleteButton);
        pageUtils.waitForElementToAppear(filterButton);
    }

    public boolean isDeleteButtonPresent() {
        return deleteButton.isDisplayed();
    }

    public PrivateWorkspacePage filterPrivateCriteria(String attribute, String condition, String input) {
        filterButton.click();
        selectPrivateWorkSpace();
        selectPartType();
        selectAttribute(attribute);
        selectCondition(condition);
        setValueInput(input);
        return new PrivateWorkspacePage(driver);
    }

    private void selectPrivateWorkSpace() {
        pageUtils.waitForElementToAppear(privateCheckBox).click();
    }

    private void selectPublicWorkspace() {
        publicCheckBox.click();
    }

    private void selectPartType() {
        partCheckBox.click();
    }

    private void selectAssemblyType() {
        assemblyCheckBox.click();
    }

    private void selectComparisonType() {
        comparisonCheckBox.click();
    }

    private void selectAttribute(String attribute) {
        new Select(attributeDropdown).selectByVisibleText(attribute);
    }

    private void selectCondition(String condition) {
        new Select(conditionDropdown).selectByVisibleText(condition);
    }

    private void setValueInput(String input) {
        valueInput.click();
        pageUtils.clearInput(valueInput);
        valueInput.sendKeys(input);
    }
}
