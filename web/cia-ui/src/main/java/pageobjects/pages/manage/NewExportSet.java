package pageobjects.pages.manage;

import com.apriori.pageobjects.header.AdminHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Random;

public class NewExportSet extends AdminHeader {

    private final Logger logger = LoggerFactory.getLogger(NewExportSet.class);

    @FindBy(css = "[class='DTE_Header_Content']")
    private WebElement newExportSetTitle;

    @FindBy(css = "[id='DTE_Field_name']")
    private WebElement setNameInput;

    @FindBy(css = "select[title='Scenario']")
    private WebElement exportScopeDropdown;

    @FindBy(css = "button[data-id='DTE_Field_scenarioKey.typeName']")
    private WebElement componentTypeDropdown;

    @FindBy(xpath = "//a[contains(@data-normalized-text, 'Assembly')]")
    private WebElement assemblyOption;

    @FindBy(xpath = "//a[contains(@data-normalized-text, 'Part')]")
    private WebElement partOption;

    @FindBy(xpath = "//button[@title='Roll-up']/../div/ul/li[3]/a")
    private WebElement rollUpOption;

    @FindBy(xpath = "//a[contains(@data-normalized-text, 'Dynamic Roll-up')]")
    private WebElement dynamicRollUpOption;

    @FindBy(css = "select[id='DTE_Field_scenarioKey.typeName']")
    private WebElement componentTypes;

    @FindBy(xpath = "//div[@id='DTE_Field_schedule.onceDateTime']/span")
    private WebElement dateSetButton;

    @FindBy(xpath = "//label[@for='DTE_Field_schedule.onceDateTime']")
    private WebElement dateSetTitleLabel;

    @FindBy(xpath = "//input[@id='DTE_Field_scenarioKey-masterName']")
    private WebElement namePartNumberInput;

    @FindBy(xpath = "//input[@id='DTE_Field_scenarioKey-stateName']")
    private WebElement scenarioNameInput;

    @FindBy(xpath = "//button[contains(text(), 'Create')]")
    private WebElement createExportButton;

    @FindBy(xpath = "//a[@href='#tab_exporthistories']")
    private WebElement historyTabButton;

    @FindBy(xpath = "//a[@id='ToolTables_exporthistorieslist_0']")
    private WebElement refreshHistoryButton;

    @FindBy(xpath = "//table[@id='exporthistorieslist']/tbody/tr[1]/td[1]")
    private WebElement firstExportSetNameInTable;

    @FindBy(xpath = "//table[@id='exporthistorieslist']/tbody/tr[1]/td[5]/span")
    private WebElement firstExportStatusInTable;

    private WebDriver driver;
    private PageUtils pageUtils;
    private int exportSetName;
    private HashMap<String, WebElement> componentTypeMap;

    public NewExportSet(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        initialiseComponentTypeHashMap();
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(setNameInput);
    }

    private void setInput(WebElement element, String value) {
        pageUtils.waitForElementToAppear(element);
        element.clear();
        element.click();
        element.sendKeys(value);
    }

    /**
     * Inputs Set Name
     * @return current page object
     */
    public NewExportSet inputSetName() {
        exportSetName = randomNameGenerator();
        setInput(setNameInput, String.valueOf(exportSetName));
        return this;
    }

    /**
     * Selects the export scope from the dropdown
     *
     * @param exportScope - workspace dropdown
     * @return current page object
     */
    public NewExportSet selectExportScope(String exportScope) {
        pageUtils.selectDropdownOption(exportScopeDropdown, exportScope);
        return this;
    }

    /**
     * Selects the export scope from the dropdown
     *
     * @param componentType - component Type dropdown
     * @return current page object
     */
    public NewExportSet selectComponentType(String componentType) {
        WebElement elementToUse = componentTypeMap.get(componentType);
        pageUtils.waitForElementAndClick(componentTypeDropdown);
        pageUtils.waitForElementAndClick(elementToUse);
        return this;
    }

    /**
     * Inputs Name Part Number
     * @param partNameNumber
     * @return Instance of NewExportSet
     */
    public NewExportSet inputNamePartNumber(String partNameNumber) {
        setInput(namePartNumberInput, partNameNumber);
        return this;
    }

    /**
     * Inputs Scenario Name
     * @param scenarioName
     * @return Instance of NewExportSet
     */
    public NewExportSet inputScenarioName(String scenarioName) {
        setInput(scenarioNameInput, scenarioName);
        return this;
    }

    /**
     * Sets date time field to now using button
     * @return Instance of NewExportSet
     */
    public NewExportSet setDateTimeToNow() {
        pageUtils.waitForElementAndClick(dateSetButton);
        pageUtils.waitForElementAndClick(dateSetTitleLabel);
        return this;
    }

    /**
     * Clicks create export button
     * @return Instance of NewExportSet
     */
    public NewExportSet clickCreateExportButton() {
        pageUtils.waitForElementAndClick(createExportButton);
        return this;
    }

    /**
     * Goes to History tab
     * @return NewExportSet instance
     */
    public NewExportSet goToHistoryTab() {
        pageUtils.waitForElementAndClick(historyTabButton);
        return this;
    }

    /**
     * Clicks refresh button
     * @return NewExportSet instance
     */
    public NewExportSet clickRefreshButton() {
        pageUtils.waitForElementAndClick(refreshHistoryButton);
        refreshHistoryButton.click();
        pageUtils.waitFor(2000);
        return this;
    }

    /**
     * Get expected export set name
     * @return String
     */
    public String getExpectedExportSetName() {
        return String.valueOf(exportSetName);
    }

    /**
     * Get first Export Set name in table
     * @return String
     */
    public String getFirstExportSetNameFromTable() {
        return firstExportSetNameInTable.getText();
    }

    /**
     * Get first Export Set status in table
     * @return Strings
     */
    public String getFirstExportSetStatusFromTable() {
        return firstExportStatusInTable.getText();
    }

    /**
     * Generates a random name
     * @return int - random name
     */
    private int randomNameGenerator() {
        Random random = new Random();
        return random.nextInt();
    }

    /**
     * Initialises Hash Map to simplify Element selection
     */
    private void initialiseComponentTypeHashMap() {
        componentTypeMap = new HashMap<>();
        componentTypeMap.put("Part", partOption);
        componentTypeMap.put("Assembly", assemblyOption);
        componentTypeMap.put("Roll-up", rollUpOption);
        componentTypeMap.put("Dynamic Roll-up", dynamicRollUpOption);
    }
}
