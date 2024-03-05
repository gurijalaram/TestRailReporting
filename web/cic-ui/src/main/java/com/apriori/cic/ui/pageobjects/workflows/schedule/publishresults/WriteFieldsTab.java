package com.apriori.cic.ui.pageobjects.workflows.schedule.publishresults;

import com.apriori.cic.api.enums.CICFieldType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.enums.PublishResultsWriteRule;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;


/**
 * To validate write fields tab in publish results part during workflow creation
 * * #### This class will be used in future when there are test cases like write fields back to PLM during workflow creation.####
 */
@Slf4j
public class WriteFieldsTab extends PublishResultsPart {

    @FindBy(xpath = PUBLISH_RESULTS_TAB + "//div[@tab-number='1']//button[.='Add Row']")
    protected WebElement wftAddRowBtn;

    private String cssTextboxSelector = "input[type='text']";
    private String writeFieldRowsSelector = PUBLISH_RESULTS_TAB + "//div[@tab-number='1']//div[contains(@class, 'tw-flex-row')]";

    public WriteFieldsTab(WebDriver driver) {
        super(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() {

    }

    /**
     * Click on the Write Fields tab
     */
    public WriteFieldsTab selectWriteFieldsTab() {
        if (!pageUtils.isElementEnabled(selectedWriteFieldsTab)) {
            pageUtils.waitForElementAndClick(writeFieldsTab);
        }
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return new WriteFieldsTab(driver);
    }

    /**
     * click add row button in standard mappings tab
     */
    public WriteFieldsTab clickAddRowBtn() {
        pageUtils.waitForElementAndClick(wftAddRowBtn);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        pageUtils.waitUntilWebElementsLoaded(By.xpath(writeFieldRowsSelector));
        pageUtils.waitForElementNotEnabled(getSaveButtonElement(), 30);
        return new WriteFieldsTab(driver);
    }

    /**
     * get the list of standard mappings rows
     *
     * @return list of standard mappings rows
     */
    public List<WebElement> getWriteFieldRows() {
        pageUtils.waitUntilWebElementsLoaded(By.xpath(writeFieldRowsSelector));
        return driver.findElements(By.xpath(writeFieldRowsSelector));
    }

    /**
     * verify field Value element enabled or disabled
     *
     * @param writingRule - PublishResultsWriteRule enum
     */
    public Boolean isFieldValueElementEnabled(PublishResultsWriteRule writingRule) {
        return (writingRule.getWritingRule().equals(PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE.getWritingRule()))
            ? pageUtils.checkElementAttribute(getFieldValueElement(CICFieldType.TEXT_BOX), "class", "cic-input-disabled") : false;
    }

    /**
     * remove row to a publish results - write fields row
     *
     * @return Current class object
     */
    public WriteFieldsTab deleteRow() {
        WebElement webElement = getWriteFieldRows().get(getWriteFieldRows().size() - 1).findElement(By.cssSelector("button"));
        pageUtils.waitForElementAndClick(webElement);
        pageUtils.waitForElementNotVisible(webElement, 30);
        return this;
    }

    /**
     * add Write Fields Row
     *
     * @param plmTypeAttributes - Costing input field
     * @param writingRule       - MappingRuleEnum
     * @param fieldValue        - field value
     * @return WriteFieldsTab
     */
    public WriteFieldsTab addWriteFieldsRow(PlmTypeAttributes plmTypeAttributes, PublishResultsWriteRule writingRule, String fieldValue) {
        this.clickAddRowBtn();
        selectCiConnectField(plmTypeAttributes);
        selectWritingRule(writingRule);
        if (plmTypeAttributes.getCicGuiField().equals("Process Group") ||
            plmTypeAttributes.getCicGuiField().equals("Digital Factory")) {
            inputFieldValue(fieldValue, CICFieldType.DROP_DOWN);
        }
        if (fieldValue.isEmpty()) {
            pageUtils.checkElementAttribute(getFieldValueElement(CICFieldType.TEXT_BOX), "class", "cic-input-disabled");
        } else {
            inputFieldValue(fieldValue, CICFieldType.TEXT_BOX);
        }
        return this;
    }

    /**
     * get Matching column from connector list table
     *
     * @param plmTypeAttributes - PlmTypeAttributes enum
     * @param colIndex          - column index to search for in connector table
     * @return WebElement
     */
    public WebElement getMatchedConnectFieldColumn(PlmTypeAttributes plmTypeAttributes, Integer colIndex) {
        WebElement matchedElement = getWriteFieldRows().stream().map(webElement -> webElement.findElements(getColumnSelector())
                .get(colIndex - 1)
                .findElement(By.cssSelector("div[class^='ss-single-selected'] span[class='placeholder']")))
            .filter(element -> element.getText().equals(plmTypeAttributes.getCicGuiField()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Could not find element with attribute " + plmTypeAttributes.getCicGuiField()));

        return matchedElement.findElement(By.xpath("../../..")).findElement(By.tagName("select"));
    }

    /**
     * Get the data row from connector Standard Mappings Rows.
     *
     * @param plmTypeAttributes - PlmTypeAttributes enum
     * @return list<WebElement>  - Number of rows
     */
    public List<WebElement> getMatchedConnectFieldRow(PlmTypeAttributes plmTypeAttributes) {
        List<WebElement> ciConnectFieldColElements = null;
        for (WebElement webElement : getWriteFieldRows()) {
            ciConnectFieldColElements = webElement.findElements(getColumnSelector());
            WebElement ciConnectFieldColElement = ciConnectFieldColElements.get(0).findElement(By.cssSelector("div[class^='ss-single-selected'] span[class='placeholder']"));
            if (ciConnectFieldColElement.getText().equals(plmTypeAttributes.getCicGuiField())) {
                return ciConnectFieldColElements;
            }
        }
        return ciConnectFieldColElements;
    }

    /**
     * verify writing rule option in dropdown list
     *
     * @param writeRule - PublishResultsWriteRule enum
     */
    public Boolean verifyWritingRuleOption(PublishResultsWriteRule writeRule) {
        List<WebElement> ciStandardFieldFieldCols = getWriteFieldRows().get(getWriteFieldRows().size() - 1).findElements(getColumnSelector());
        return pageUtils.checkDropdownOptions(ciStandardFieldFieldCols.get(1).findElement(By.tagName("select")), writeRule.getWritingRule());
    }

    /**
     * verify data in field Value field based on selected connect field
     *
     * @param fieldValue
     */
    public Boolean verifyFieldValueOption(String fieldValue) {
        List<WebElement> ciStandardFieldFieldCols = getWriteFieldRows().get(getWriteFieldRows().size() - 1).findElements(getColumnSelector());
        return pageUtils.checkDropdownOptions(ciStandardFieldFieldCols.get(3).findElement(By.tagName("select")), fieldValue);
    }


    /**
     * get list of all multi field options
     *
     * @return List<String>
     */
    public List<String> getMultiFieldOptions() {
        List<String> multiValues = new ArrayList<>();
        List<WebElement> ciStandardFieldFieldCols = getWriteFieldRows().get(getWriteFieldRows().size() - 1).findElements(getColumnSelector());
        pageUtils.waitForElementToBeClickable(ciStandardFieldFieldCols.get(3));
        WebElement selectOption = ciStandardFieldFieldCols.get(3).findElement(By.tagName("select"));
        pageUtils.waitUntilDropdownOptionsLoaded(selectOption);
        for (WebElement element : new Select(selectOption).getOptions()) {
            multiValues.add(element.getAttribute("value"));
        }
        return multiValues;
    }

    /**
     * get the list of selected multi field options
     *
     * @return list of options
     */
    public List<WebElement> getSelectedMultiFieldOptions() {
        List<WebElement> ciStandardFieldFieldCols = getWriteFieldRows().get(getWriteFieldRows().size() - 1).findElements(getColumnSelector());
        pageUtils.waitUntilDropdownOptionsLoaded(ciStandardFieldFieldCols.get(3).findElement(By.tagName("select")));
        return ciStandardFieldFieldCols.get(3).findElements(By.cssSelector("div[class = 'ss-multi-selected'] div[class = 'ss-value']"));
    }

    /**
     * delete the selected multi field option
     *
     * @return WriteFieldsTab
     */
    public WriteFieldsTab deleteSelectedMultiFieldOptions(String optionToDelete) {
        List<WebElement> ciStandardFieldFieldCols = getWriteFieldRows().get(getWriteFieldRows().size() - 1).findElements(getColumnSelector());
        pageUtils.waitUntilDropdownOptionsLoaded(ciStandardFieldFieldCols.get(3).findElement(By.tagName("select")));
        List<WebElement> selectedElements = ciStandardFieldFieldCols.get(3).findElements(By.cssSelector("div[class = 'ss-multi-selected'] div[class = 'ss-values']"));
        WebElement selectedElement = selectedElements.stream().filter(webElement -> webElement.findElement(By.cssSelector("span.ss-value-text")).getText().equals(optionToDelete))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Failed to delete a Selected Value!" + optionToDelete));
        pageUtils.waitForElementAndClick(selectedElement.findElement(By.cssSelector("span.ss-value-delete")));
        return this;
    }


    /**
     * get writing rule element from current row
     *
     * @return WebElement
     */
    public WebElement getWritingRuleElement() {
        List<WebElement> ciStandardFieldFieldCols = getWriteFieldRows().get(getWriteFieldRows().size() - 1).findElements(getColumnSelector());
        return ciStandardFieldFieldCols.get(1).findElement(By.tagName("select"));
    }

    /**
     * Select Writing Rule
     *
     * @param writingRule - PublishResultsWriteRule enum
     * @return WriteFieldsTab
     */
    public WriteFieldsTab selectWritingRule(PublishResultsWriteRule writingRule) {
        WebElement writingRuleElement = getWriteFieldRows().get(getWriteFieldRows().size() - 1).findElements(getColumnSelector()).get(1);
        pageUtils.waitUntilDropdownOptionsLoaded(writingRuleElement.findElement(By.tagName("select")));
        pageUtils.waitForElementAndClick(writingRuleElement);
        pageUtils.waitForElementToBeClickable(this.driver.findElement(By.cssSelector(OPTIONS_CONTENT_OPEN_DROPDOWN_CSS)));
        this.selectValueFromDDL(writingRule.getGuiWritingRule());
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return this;
    }

    /**
     * select CI Connect field in Writing Fields row
     *
     * @param plmTypeAttributes - PlmTypeAttributes enum
     * @return WriteFieldsTab
     */
    public WriteFieldsTab selectCiConnectField(PlmTypeAttributes plmTypeAttributes) {
        WebElement connectFieldElement = getWriteFieldRows().get(getWriteFieldRows().size() - 1).findElements(getColumnSelector()).get(0);
        pageUtils.waitUntilDropdownOptionsLoaded(connectFieldElement.findElement(By.tagName("select")));
        pageUtils.waitForElementAndClick(connectFieldElement);
        this.selectValueFromDDL(plmTypeAttributes.getCicGuiField());
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return this;
    }

    /**
     * select Custom multi field in write fields rows
     *
     * @param customValues
     * @return WriteFieldsTab
     */
    public WriteFieldsTab selectMultiSelectField(String... customValues) {
        for (String multiValue : customValues) {
            WebElement connectFieldElement = getWriteFieldRows().get(getWriteFieldRows().size() - 1).findElements(getColumnSelector()).get(3);

            pageUtils.waitUntilDropdownOptionsLoaded(connectFieldElement.findElement(By.tagName("select")));
            pageUtils.waitForElementAndClick(connectFieldElement);
            this.selectValueFromDDL(multiValue);
            pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        }
        return this;
    }

    /**
     * get Field value element from current row
     *
     * @param cicFieldType - CICFieldType enum
     * @return WebElement
     */
    private WebElement getFieldValueElement(CICFieldType cicFieldType) {
        return pageUtils.waitForElementToAppear(getWriteFieldRows().get(getWriteFieldRows().size() - 1)
            .findElements(getColumnSelector())
            .stream()
            .filter(webElement1 -> webElement1.getAttribute("class").contains(cicFieldType.getFieldType()))
            .findFirst()
            .get());
    }

    /**
     * enter field value
     *
     * @param fieldValue   - connector field value
     * @param cicFieldType - CICFieldType
     */
    private void inputFieldValue(String fieldValue, CICFieldType cicFieldType) {
        if (cicFieldType.getFieldType().equals(CICFieldType.DROP_DOWN.getFieldType()) ||
            cicFieldType.getFieldType().equals(CICFieldType.DATE_TIME_PICKER.getFieldType())) {
            pageUtils.waitForElementAndClick(getFieldValueElement(cicFieldType));
            this.selectValueFromDDL(fieldValue);
        } else {
            pageUtils.waitForElementToBeClickable(getFieldValueElement(cicFieldType).findElement(By.tagName("input")));
            pageUtils.setValueOfElement(getFieldValueElement(cicFieldType).findElement(By.tagName("input")), fieldValue);
        }
    }

    /**
     * get the selector for Write Field Tab rows by fields
     *
     * @return By
     */
    private By getColumnSelector() {
        return By.cssSelector("div[class*='cic-input']");
    }
}
