package main.java.pages.explore;

import main.java.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PrivateWorkspacePage extends LoadableComponent<PrivateWorkspacePage> {

    private final Logger logger = LoggerFactory.getLogger(PrivateWorkspacePage.class);

    @FindBy(css = "a.dropdown-toggle.text-center span.glyphicon-file")
    private WebElement newFileDropdown;

    @FindBy(css = "button[data-ap-comp='newComponentButton']")
    private WebElement componentButton;

    @FindBy(css = "button[data-ap-comp='saveAsButton']")
    private WebElement scenarioButton;

    @FindBy(css = "button[data-ap-comp='newComparisonButton']")
    private WebElement comparisonButton;

    @FindBy(css = "button[data-ap-comp='publishScenarioButton']")
    private WebElement publishButton;

    @FindBy(css = "button[data-ap-comp='revertScenarioButton']")
    private WebElement revertButton;

    @FindBy(css = "span.delete-button")
    private WebElement deleteButton;

    @FindBy(css = "span.glyphicons-settings")
    private WebElement actionsDropdown;

    @FindBy(css = "select[data-ap-field='filter'] option")
    private List<WebElement> workspaceDropdownList;

    @FindBy(css = "select.form-control.input-md.auto-width")
    private WebElement workspaceDropdown;

    @FindBy(css = "button[data-ap-nav-dialog='showScenarioSearchCriteria']")
    private WebElement filterButton;

    @FindBy(css = "span[data-ap-comp='resultCount']")
    private WebElement objectsFound;

    @FindBy(css = "button[data-ap-nav-dialog='showTableViewEditor']")
    private WebElement columnsButton;

    @FindBy(css = "button[data-ap-comp='togglePreviewButton']")
    private WebElement previewButton;

    By scenario = By.cssSelector("div[data-ap-comp='componentTable'] a[href='#openFromSearch::sk,partState,PLASTIC MOULDED CAP THINPART,Initial,0']");
    //By scenario = By.cssSelector("div[data-ap-comp='componentTable'] a[href='#openFromSearch::sk,partState,CASTING 1,Initial,0']");
    //@FindBy(css = "div[data-ap-comp='componentTable'] a[href='#openFromSearch::sk,partState,PLASTIC MOULDED CAP THINPART,Initial,0']")
    //private WebElement scenario;

    @FindBy(css = "div[data-ap-comp='componentTable'] div.v-grid-scroller-vertical")
    private WebElement componentScroller;

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
        pageUtils.waitForElementsToAppear(workspaceDropdownList);
    }

    public boolean isDeleteButtonPresent() {
        return deleteButton.isDisplayed();
    }

    public PrivateWorkspacePage uploadFile(String scenarioName, String filePath, String fileName) {
        newFileDropdown.click();
        componentButton.click();
        new FileUploadPage(driver).uploadFile(scenarioName, filePath, fileName);
        return this;
    }

    public PrivateWorkspacePage filterPrivateCriteria(String type, String attribute, String condition, String value) {
        filterButton.click();
        new FilterCriteriaPage(driver).clearAllCheckBoxes()
            .setPrivateWorkSpace()
            .setScenarioType(type)
            .selectAttribute(attribute)
            .selectCondition(condition)
            .setTypeOfValue(value)
            .apply();
        return new PrivateWorkspacePage(driver);
    }

    public PrivateWorkspacePage filterPublicCriteria(String type, String attribute, String condition, String value) {
        filterButton.click();
        new FilterCriteriaPage(driver).clearAllCheckBoxes()
            .setPublicWorkspace()
            .setScenarioType(type)
            .selectAttribute(attribute)
            .selectCondition(condition)
            .setTypeOfValue(value)
            .apply();
        return new PrivateWorkspacePage(driver);
    }

    public PrivateWorkspacePage selectWorkSpace(String workspace) {
        new Select(workspaceDropdown).selectByVisibleText(workspace);
        return this;
    }

    public PrivateWorkspacePage findScenario() {

        pageUtils.waitForElementToAppear(componentScroller);

        long startTime = System.currentTimeMillis() / 1000;
        do {
            componentScroller.sendKeys(Keys.DOWN);
        } while (driver.findElements(scenario).size() < 1 && ((System.currentTimeMillis() / 1000) - startTime) < 60);

        Coordinates processCoordinates = ((Locatable) driver.findElement(scenario)).getCoordinates();
        processCoordinates.inViewPort();
        return this;
    }

    public PrivateWorkspacePage openScenario() {
        pageUtils.waitForElementToAppear(scenario).click();
        return this;
    }
}
