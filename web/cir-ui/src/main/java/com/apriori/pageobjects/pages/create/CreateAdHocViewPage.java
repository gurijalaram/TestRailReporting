package com.apriori.pageobjects.pages.create;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateAdHocViewPage extends ReportsPageHeader {

    private static final Logger logger = LoggerFactory.getLogger(CreateAdHocViewPage.class);

    @FindBy(xpath = "//div[@id='display']/div[2]/div/div[1]/div")
    private WebElement adHocViewPageTitle;

    @FindBy(xpath = "//div[@class='pageHeader-title-text']")
    private WebElement adHocViewCreationPageTitle;

    @FindBy(xpath = "//div[contains(@class, 'sourceDialogNew ')]")
    private WebElement adHocViewDialog;

    @FindBy(xpath = "(//li[@class='leaf domain view11'])[1]")
    private WebElement adHocViewDataSourceOne;

    @FindBy(xpath = "//span[contains(text(), 'Choose Data...')]")
    private WebElement chooseDataButton;

    @FindBy(xpath = "(//p[@class='wrap button draggable'])[3]")
    private WebElement chooseDataFirstOption;

    @FindBy(id = "toRight")
    private WebElement moveDataToRightButton;

    @FindBy(id = "goToDesigner")
    private WebElement goToDesignerOkButton;

    @FindBy(id = "changeVisualizationButton")
    private WebElement changeVisualizationButton;

    @FindBy(xpath = "//li[@data-chart-type='Table']")
    private WebElement visualizationTableOptionButton;

    @FindBy(xpath = "//span[contains(text(), 'Apply and Close')]")
    private WebElement applyAndCloseVisualizationButton;

    @FindBy(xpath = "(//div[@id='fields']//input)[1]")
    private WebElement fieldsInput;

    @FindBy(xpath = "(//div[@id='fields']//input)[2]")
    private WebElement measuresInput;

    @FindBy(id = "dataSizeSelector")
    private WebElement dataScopeSelector;

    @FindBy(xpath = "(//ul[@id='dimensionsTree']//li)[1]")
    private WebElement firstFieldsField;

    @FindBy(xpath = "(//ul[@id='dimensionsTree']//li)[3]")
    private WebElement secondFieldsField;

    @FindBy(xpath = "//ul[@id='menuList']/li[1]")
    private WebElement addAsColumnMenuOption;

    @FindBy(xpath = "//ul[@id='menuList']/li[3]")
    private WebElement createFilterMenuOption;

    @FindBy(xpath = "(//ul[@id='measuresTree']//li)[1]")
    private WebElement measuresSearchResultList;

    @FindBy(xpath = "(//ul[@id='measuresTree']//li)[2]")
    private WebElement measuresFirstSearchResultOption;

    @FindBy(xpath = "//div[@id='filters']//a")
    private WebElement filterDropdown;

    @FindBy(xpath = "//div[@id='filters']//a/following-sibling::div/input")
    private WebElement createFilterInput;

    @FindBy(xpath = "(//li[@class='folders node closed'])[1]")
    private WebElement firstDataField;

    private final PageUtils pageUtils;
    private final WebDriver driver;

    public CreateAdHocViewPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Get page title text
     *
     * @return String - page title text
     */
    public String getAdHocViewTitleText() {
        pageUtils.waitForElementToAppear(adHocViewPageTitle);
        return adHocViewPageTitle.getText();
    }

    /**
     * Gets dialog isDisplayed value
     *
     * @return boolean - isDisplayed
     */
    public boolean isDialogDisplayed() {
        pageUtils.waitForElementToAppear(adHocViewDialog);
        return adHocViewDialog.isDisplayed();
    }

    /**
     * Gets dialog isEnabled value
     *
     * @return boolean - is Enabled
     */
    public boolean isDialogEnabled() {
        pageUtils.waitForElementToAppear(adHocViewDialog);
        return adHocViewDialog.isEnabled();
    }

    /**
     * Checks if create ad hoc view page is displayed and enabled
     *
     * @return boolean
     */
    public boolean isCreateAdHocViewPageDisplayedAndEnabled() {
        return adHocViewCreationPageTitle.isEnabled() && adHocViewCreationPageTitle.isDisplayed();
    }

    /**
     * Click first data source
     *
     * @return CreateAdHocViewPage instance
     */
    public CreateAdHocViewPage clickFirstDataSource() {
        pageUtils.waitForElementAndClick(adHocViewDataSourceOne);
        return this;
    }

    /**
     * Click choose data button
     *
     * @return CreateAdHocViewPage instance
     */
    public CreateAdHocViewPage clickChooseData() {
        pageUtils.waitForElementAndClick(chooseDataButton);
        return this;
    }

    /**
     * Wait for choose data dialog data to appear
     *
     * @return CreateAdHocViewPage instance
     */
    public CreateAdHocViewPage waitForChooseDataDialogToAppear() {
        pageUtils.waitForElementToAppear(chooseDataFirstOption);
        return this;
    }

    /**
     * Click move data to right button
     *
     * @return CreateAdHocViewPage instance
     */
    public CreateAdHocViewPage clickMoveDataToRightButton() {
        pageUtils.waitForElementAndClick(moveDataToRightButton);
        return this;
    }

    /**
     * Click go to designer
     *
     * @return CreateAdHocViewPage instance
     */
    public CreateAdHocViewPage clickGoToDesignerOkButton() {
        pageUtils.waitForElementAndClick(goToDesignerOkButton);
        assertThat(isCreateAdHocViewPageDisplayedAndEnabled(), is(equalTo(true)));
        return this;
    }

    /**
     * Change visualization to table
     *
     * @return CreateAdHocViewPage instance
     */
    public CreateAdHocViewPage changeVisualizationToTable() {
        pageUtils.waitForElementToAppear(firstDataField);
        pageUtils.waitForElementAndClick(changeVisualizationButton);
        pageUtils.waitForElementAndClick(visualizationTableOptionButton);
        pageUtils.waitForElementAndClick(applyAndCloseVisualizationButton);
        pageUtils.waitForElementToAppear(By.xpath("//tr[@id='rows']/td[contains(@class, 'title')]"));
        return this;
    }

    /**
     * Change data scope to full
     *
     * @return CreateAdHocViewPage instance
     */
    public CreateAdHocViewPage changeDataScopeToFull() {
        Select dataScope = new Select(dataScopeSelector);
        dataScope.selectByValue("full");
        assertThat(dataScopeSelector.getAttribute("value"), is(equalTo("full")));
        return this;
    }

    public CreateAdHocViewPage addDataToTable() {
        Actions actions = new Actions(driver);

        // add Scenario Part Number
        pageUtils.waitForElementAndClick(fieldsInput);

        pageUtils.setValueOfElement(
            fieldsInput,
            "Scenario Part Number",
            Keys.ENTER
        );

        pageUtils.waitForElementToAppear(firstFieldsField);

        actions.contextClick(firstFieldsField).build().perform();
        pageUtils.waitForElementAndClick(addAsColumnMenuOption);

        // add Scenario Name
        pageUtils.waitForElementAndClick(fieldsInput);

        pageUtils.setValueOfElement(
            fieldsInput,
            "Scenario Name",
            Keys.ENTER
        );

        pageUtils.waitForElementToAppear(secondFieldsField);

        actions.contextClick(secondFieldsField).build().perform();
        pageUtils.waitForElementAndClick(addAsColumnMenuOption);

        // add scenario fbc to table
        pageUtils.waitForElementAndClick(measuresInput);

        pageUtils.setValueOfElement(measuresInput, "Scenario Fully Burdened Cost", Keys.ENTER);

        pageUtils.waitForElementToAppear(measuresSearchResultList);

        actions.contextClick(measuresFirstSearchResultOption).build().perform();
        pageUtils.waitForElementAndClick(addAsColumnMenuOption);

        return this;
    }

    /**
     * Add filter to table
     *
     * @return CreateAdHocViewPage instance
     */
    public CreateAdHocViewPage addFilterToTable() {
        Actions actions = new Actions(driver);
        pageUtils.waitForElementAndClick(fieldsInput);

        pageUtils.setValueOfElement(
            fieldsInput,
            "Latest Export Set Name",
            Keys.ENTER
        );

        pageUtils.waitForElementToAppear(firstFieldsField);

        actions.contextClick(firstFieldsField)
            .build()
            .perform();
        pageUtils.waitForElementAndClick(createFilterMenuOption);

        pageUtils.waitForElementAndClick(By.xpath("//div[@id='filters']//a"));

        By createFilterInputLocator = By.xpath("//div[@id='filters']//a/following-sibling::div/input");
        pageUtils.waitForElementAndClick(createFilterInputLocator);
        driver.findElement(createFilterInputLocator).clear();
        driver.findElement(createFilterInputLocator).sendKeys("top-level");

        String dropdownSelectionGenericLocator = "//%s[@title='---01-top-level']";
        pageUtils.waitForElementAndClick(By.xpath(String.format(dropdownSelectionGenericLocator, "li")));
        pageUtils.waitForElementToAppear(By.xpath(String.format(dropdownSelectionGenericLocator, "a")));

        pageUtils.waitForElementAndClick(By.xpath("//fieldset[@id='applyFilter']/button"));

        pageUtils.waitForElementToAppear(By.xpath("//tbody[@id='tableDetails']/tr[1]/td[1]/span[contains(text(), '3538968')]"));

        return this;
    }

    /**
     * Gets specified table cell value
     *
     * @param columnIndex - String
     * @param rowIndex - String
     * @return String of value retrieved
     */
    public String getTableCellValue(String columnIndex, String rowIndex) {
        return driver.findElement(
            By.xpath(
                String.format(
                    "//tbody[@id='tableDetails']/tr[%s]/td[%s]/span",
                    columnIndex,
                    rowIndex)))
            .getText();
    }
}
