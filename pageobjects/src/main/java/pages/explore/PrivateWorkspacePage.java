package main.java.pages.explore;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
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

    public PrivateWorkspacePage filterPrivateCriteria(String type, String attribute, String condition, String value) {
        filterButton.click();
        new FilterCriteriaPage(driver).clearAllCheckBoxes()
            .setPrivateWorkSpace()
            .setScenarioType(type)
            .selectAttribute(attribute)
            .selectCondition(condition)
            .setTypeOfValue(value);
        return new PrivateWorkspacePage(driver);
    }

    public PrivateWorkspacePage filterPublicCriteria(String type, String attribute, String condition, String value) {
        filterButton.click();
        new FilterCriteriaPage(driver).clearAllCheckBoxes()
            .setPublicWorkspace()
            .setScenarioType(type)
            .selectAttribute(attribute)
            .selectCondition(condition)
            .setTypeOfValue(value);
        return new PrivateWorkspacePage(driver);
    }
}
