package com.apriori.cir.ui.pageobjects.create;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cic.ui.utils.TableUtils;
import com.apriori.cir.ui.pageobjects.header.ReportsPageHeader;
import com.apriori.web.app.util.PageUtils;

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

    @FindBy(css = "div.primary div.title")
    private WebElement adHocViewPageTitle;

    @FindBy(css = "div.pageHeader-title-text")
    private WebElement adHocViewCreationPageTitle;

    @FindBy(css = "div.jr-uWidth-400px")
    private WebElement adHocViewDialog;

    @FindBy(xpath = "//p[contains(@data-tooltip, 'aPriori Ad Hoc')]/..")
    private WebElement adHocViewDataSourceOne;

    @FindBy(xpath = "//span[contains(text(), 'Data')]")
    private WebElement chooseDataButton;

    @FindBy(id = "toRight")
    private WebElement moveDataToRightButton;

    @FindBy(id = "goToDesigner")
    private WebElement goToDesignerOkButton;

    @FindBy(id = "changeVisualizationButton")
    private WebElement changeVisualizationButton;

    @FindBy(xpath = "//li[@data-chart-type='Table']")
    private WebElement visualizationTableOptionButton;

    @FindBy(xpath = "//button[@class='jr-mButton jr-mButtonText jr-mButtonPrimary jr']")
    private WebElement applyAndCloseVisualizationButton;

    @FindBy(xpath = "//div[@id='fields']//div[contains(@class, 'primary')]//input")
    private WebElement fieldsInput;

    @FindBy(xpath = "//div[@id='fields']//div[contains(@class, 'secondary')]//input")
    private WebElement measuresInput;

    @FindBy(id = "dataSizeSelector")
    private WebElement dataScopeSelector;

    @FindBy(xpath = "//ul[@id='menuList']/li[1]")
    private WebElement addAsColumnMenuOption;

    @FindBy(xpath = "//ul[@id='menuList']/li[3]")
    private WebElement createFilterMenuOption;

    @FindBy(xpath = "//ul[@id='measuresTree']//ul")
    private WebElement measuresSearchResultList;

    @FindBy(xpath = "//p[contains(@title, 'Scenario Fully Burdened Cost (R')]/..")
    private WebElement measuresFirstSearchResultOption;

    @FindBy(xpath = "//span[contains(text(), 'C')]/ancestor::div[contains(@class, 'jr')]//input")
    private WebElement createFilterInput;

    @FindBy(xpath = "//p[contains(@title, 'Combines')]/..")
    private WebElement firstDataPreSearchField;

    @FindBy(xpath = "//ul[@id='dimensionsTree']//ul/li[1]")
    private WebElement firstDataPostSearchField;

    @FindBy(xpath = "//ul[@id='dimensionsTree']//ul/li[2]")
    private WebElement secondDataPostSearchField;

    @FindBy(xpath = "//tbody[@id='tableDetails']")
    private WebElement adHocViewReportTable;

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
        pageUtils.waitForElementToAppear(firstDataPreSearchField);
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
        pageUtils.waitForElementToAppear(firstDataPreSearchField);
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

    /**
     * Adds required data to table
     *
     * @return CreateAdHocViewPage instance
     */
    public CreateAdHocViewPage addDataToTable() {
        addFieldsDataItemToTable("Scenario Part Number");
        addFieldsDataItemToTable("Scenario Name");

        Actions actions = new Actions(driver);

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

        pageUtils.setValueOfElement(
            fieldsInput,
            "Latest Export Set Name",
            Keys.ENTER
        );

        pageUtils.waitForElementToAppear(firstDataPostSearchField);

        actions.contextClick(firstDataPostSearchField)
            .build()
            .perform();
        pageUtils.waitForElementAndClick(createFilterMenuOption);

        pageUtils.waitForElementAndClick(By.xpath("//div[@id='filters']//a"));

        By createFilterInputLocator = By.xpath("//div[@id='filters']//a/following-sibling::div/input");
        pageUtils.waitForElementAndClick(createFilterInputLocator);
        driver.findElement(createFilterInputLocator).clear();
        driver.findElement(createFilterInputLocator).sendKeys("top-level");

        String dropdownSelectionGenericLocator = "//%s[@title='- - - 0 0 0-top-level']";
        pageUtils.waitForElementAndClick(By.xpath(String.format(dropdownSelectionGenericLocator, "li")));
        pageUtils.waitForElementToAppear(By.xpath(String.format(dropdownSelectionGenericLocator, "a")));

        pageUtils.waitForElementAndClick(By.xpath("//fieldset[@id='applyFilter']/button"));

        return this;
    }

    /**
     * Gets specified table cell value
     *
     * @param columnIndex - String
     * @param rowIndex    - String
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

    /**
     * Get matching row from table
     *
     * @param cellText
     * @return WebElement
     */
    public WebElement getTableRowByCellText(String cellText) {
        return new TableUtils(driver).getRowByCellText(adHocViewReportTable, cellText);
    }

    /**
     * Get cell value from row and column index
     *
     * @param tableRowElement
     * @param columnIndex
     * @return WebElement - Matching WebElement from row and column.
     */
    public WebElement getCellValue(WebElement tableRowElement, int columnIndex) {
        return new TableUtils(driver).getColumnByIndex(tableRowElement, columnIndex);
    }

    /**
     * Add fields data item to table
     *
     * @param fieldsTextToInput - String
     */
    private void addFieldsDataItemToTable(String fieldsTextToInput) {
        Actions actions = new Actions(driver);

        pageUtils.waitForElementAndClick(fieldsInput);

        pageUtils.setValueOfElement(fieldsInput, fieldsTextToInput, Keys.ENTER);

        pageUtils.waitForElementToAppear(firstDataPostSearchField);

        WebElement buttonToClick = fieldsTextToInput.equals("Scenario Name") ? secondDataPostSearchField : firstDataPostSearchField;
        actions.contextClick(buttonToClick).build().perform();
        pageUtils.waitForElementAndClick(addAsColumnMenuOption);
    }
}
