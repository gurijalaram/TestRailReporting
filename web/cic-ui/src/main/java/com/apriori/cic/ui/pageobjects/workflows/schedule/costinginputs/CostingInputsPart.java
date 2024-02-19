package com.apriori.cic.ui.pageobjects.workflows.schedule.costinginputs;

import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.ui.pageobjects.CICBasePage;
import com.apriori.cic.ui.pageobjects.workflows.schedule.details.DetailsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.notifications.NotificationsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.cic.ui.utils.Constants;
import com.apriori.shared.util.http.utils.GenerateStringUtil;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Costing Inputs part in new workflow process or edit workflow process
 */
@Slf4j
public class CostingInputsPart extends CICBasePage {

    @FindBy(xpath = "//div[@tab-number='3']//button[.='Add Row']")
    private WebElement addRowBtn;

    @FindBy(xpath = "//div[@tab-number='3']//button[.='Previous']")
    private WebElement ciPreviousButton;

    @FindBy(xpath = "//div[@tab-number='3']//button[.='Cancel']")
    private WebElement ciCancelButton;

    @FindBy(xpath = "//div[@tab-number='3']//button[.='Next']")
    private WebElement ciNextButton;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_button-189'] > button")
    private WebElement ciAddRowButton;

    @FindBy(css = "div[class='BMCollectionViewCellWrapper BMCollectionViewCellEditing'] div[class='BMCollectionViewCell BMCollectionViewCellHoverable']")
    private WebElement ciConnectFieldInitialRowElement;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_checkbox-393'] > label > span")
    private WebElement workflowQDReturnOnlyCheckbox;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_textbox-148'] > table > tbody > tr > td > input")
    private WebElement worflowNameField;

    @FindBy(css = "div[id^='PLMC_NoCostingInputs_MU-'][id$='_label-3']")
    private WebElement NoCostingInputLabel;

    @FindBy(css = "#ui-datepicker-div > div.ui-datepicker-buttonpane.ui-widget-content > button.ui-datepicker-current.ui-state-default.ui-priority-secondary.ui-corner-all")
    private WebElement customDateCalenderTodayButton;

    @FindBy(css = "#ui-datepicker-div > div.ui-datepicker-buttonpane.ui-widget-content > button.ui-datepicker-close.ui-state-default.ui-priority-primary.ui-corner-all.ui-state-hover")
    private WebElement customDateCalenderDoneButton;

    private static WebElement customDateFieldValueElement = null;
    private String ciConnectFieldCss = "#CIC_CostingInputCell_MU-[ID]_DrowpdownWidget-3";
    private String operatorDDCss = "#CIC_CostingInputCell_MU-[ID]_DrowpdownWidget-4";
    private String valueDDCss = "#CIC_CostingInputCell_MU-[ID]_DrowpdownWidget-20";
    private String valueTxtCss = "#CIC_CostingInputCell_MU-[ID]" + "_textbox-5 > table > tbody > tr > td > input";
    private String connectFieldDDCss = "div[class^='ss-content ss-'][class$='ss-open'] div[class='ss-list']";
    private String deleteRowButtonCss = "#CIC_CostingInputCell_MU-[ID]_button-6";
    private String customDateValuePickerCss = "#CIC_CostingInputCell_MU-[ID]_datetimepicker-19";
    private String cssColumnSelector = "div[class*='cic-input']";
    private String cssTextboxSelector = "input[type='text']";
    private WebElement selectedRow;


    public CostingInputsPart(WebDriver driver) {
        super(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
    }

    /**
     * Add Costing Input fields
     *
     * @param numOfRows - Number of fields
     * @return - this
     */
    public CostingInputsPart addCostingInputFields(int numOfRows) {
        this.workFlowData = workFlowData;
        try {
            for (int row = 0; row < numOfRows; row++) {
                this.addRow(row);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return this;
    }


    /**
     * getter for Next button in Costing Inputs tab
     *
     * @return WebElement
     */
    public WebElement getCiNextButton() {
        return ciNextButton;
    }

    /**
     * getter for row delete button in Costing Inputs tab
     *
     * @return WebElement
     */
    public CostingInputsPart clickRowDeleteButton(int rowNum) {
        String initialFieldRow = ciConnectFieldInitialRowElement.getAttribute("id").split("-")[1].trim();
        Integer rowID = Integer.parseInt(initialFieldRow) + rowNum;
        pageUtils.waitForElementAndClick(driver.findElement(By.cssSelector(deleteRowButtonCss.replace("[ID]", rowID.toString()))));
        return this;
    }

    /**
     * getter for row delete button in Costing Inputs tab
     *
     * @return WebElement
     */
    public CostingInputsPart clickAddRowButton() {
        pageUtils.waitForElementAndClick(ciAddRowButton);
        pageUtils.waitForElementToAppear(ciConnectFieldInitialRowElement);
        return this;
    }

    /**
     * Click Previous Workflow button
     *
     * @return NewEditWorkflow page object
     */
    public NotificationsPart clickCINextBtn() {
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        pageUtils.waitForElementToBeClickable(ciNextButton);
        pageUtils.waitForElementAndClick(ciNextButton);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        return new NotificationsPart(this.driver);
    }

    /**
     * getter for No Costing Inputs defined label
     *
     * @return WebElement
     */
    public WebElement getNoCostingInputLabel() {
        pageUtils.waitForElementToAppear(NoCostingInputLabel);
        return NoCostingInputLabel;
    }

    /**
     * getter for No Costing Inputs Custom Date value element
     *
     * @return WebElement
     */
    public WebElement getCustomDateFieldValueElement() {
        return customDateFieldValueElement;
    }

    /**
     * getter for No Costing Inputs custom date picker element
     *
     * @return WebElement
     */
    public CostingInputsPart clickCustomDateCalenderTodayButton() {
        pageUtils.waitForElementToAppear(customDateCalenderTodayButton);
        pageUtils.waitForElementAndClick(customDateCalenderTodayButton);
        return this;
    }

    /**
     * getter for No Costing Inputs custom date picker element
     *
     * @return WebElement
     */
    public WebElement getCustomDateField() {
        pageUtils.waitForElementToAppear(customDateCalenderTodayButton);
        return customDateCalenderTodayButton;
    }

    /**
     * Click Previous Workflow button
     *
     * @return NewEditWorkflow page object
     */
    public Object clickPreviousBtn() {
        pageUtils.waitForElementToBeClickable(ciPreviousButton);
        pageUtils.waitForElementToAppear(ciPreviousButton);
        Object object = null;
        if (!ciPreviousButton.isEnabled()) {
            log.warn("Next button in Query Definitions Page is not enabled");
            return this;
        }
        pageUtils.waitForElementAndClick(ciPreviousButton);
        if (pageUtils.isElementDisplayed(workflowQDReturnOnlyCheckbox)) {
            object = new QueryDefinitions(driver);
        } else if (pageUtils.isElementDisplayed(worflowNameField)) {
            object = new DetailsPart(driver);
        }
        return object;
    }

    /**
     * verify fieldname in the list of scenario field names
     *
     * @return boolean
     */
    public boolean isCiConnectFieldExist(List<String> expectedFieldList) {
        pageUtils.waitForElementAndClick(ciAddRowButton);
        pageUtils.waitForElementToAppear(ciConnectFieldInitialRowElement);
        String initialFieldRow = ciConnectFieldInitialRowElement.getAttribute("id").split("-")[1].trim();
        Integer rowID = Integer.parseInt(initialFieldRow) + 0;
        WebElement ciConnectFieldDropdownElement = driver.findElement(By.cssSelector(ciConnectFieldCss.replace("[ID]", rowID.toString())));
        pageUtils.waitForElementAndClick(ciConnectFieldDropdownElement);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        List<WebElement> fieldNameListElement = this.driver.findElements(By.cssSelector(OPTIONS_CONTENT_OPEN_DROPDOWN_CSS)).get(1)
            .findElements(By.cssSelector("div[class='ss-option']"));
        List<String> fieldValues = new ArrayList<String>();
        fieldNameListElement.stream().forEach(element -> {
            fieldValues.add(element.getText());
        });
        this.selectValueFromDDL(workFlowData.getCostingInputsData().get(0).getFieldName());
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        return expectedFieldList.stream().allMatch(fieldValues::contains);
    }

    /**
     * verify fieldname in the list of scenario field names
     *
     * @return boolean
     */
    public boolean isMappingRuleFieldExist(List<String> expectedMappingFields) {
        String initialFieldRow = ciConnectFieldInitialRowElement.getAttribute("id").split("-")[1].trim();
        Integer rowID = Integer.parseInt(initialFieldRow) + 0;
        WebElement ciConnectOperatorDropdownElement = driver.findElement(By.cssSelector(operatorDDCss.replace("[ID]", rowID.toString())));
        pageUtils.waitForElementAndClick(ciConnectOperatorDropdownElement);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        List<WebElement> fieldNameListElement = this.driver.findElements(By.cssSelector(OPTIONS_CONTENT_OPEN_DROPDOWN_CSS)).get(1)
            .findElements(By.cssSelector("div[class='ss-option']"));
        List<String> fieldValues = new ArrayList<String>();
        fieldNameListElement.stream().forEach(element -> {
            fieldValues.add(element.getText());
        });
        return expectedMappingFields.stream().allMatch(fieldValues::contains);
    }

    /**
     * verify Field
     *
     * @param expectedMappingFields - list of field values
     * @return boolean
     */
    public Boolean isValuesExists(List<String> expectedMappingFieldValues) {
        List<WebElement> ciStandardFieldFieldCols = getCostingInputRows().get(getCostingInputRows().size() - 1).findElements(By.cssSelector(cssColumnSelector));
        pageUtils.waitForElementAndClick(ciStandardFieldFieldCols.get(3));
        List<WebElement> fieldValueListElement = this.driver.findElements(By.cssSelector(OPTIONS_CONTENT_OPEN_DROPDOWN_CSS)).get(1)
            .findElements(By.cssSelector("div[class='ss-option']"));
        List<String> fieldValues = new ArrayList<String>();
        fieldValueListElement.stream().forEach(element -> {
            fieldValues.add(element.getText());
        });
        return expectedMappingFieldValues.stream().allMatch(fieldValues::contains);
    }

    /**
     * verify Mapping field value
     *
     * @param mappingRule - MappingRule
     * @return boolean
     */
    public Boolean isMappingRuleFieldEnabled() {
        List<WebElement> ciStandardFieldFieldCols = getCostingInputRows().get(getCostingInputRows().size() - 1).findElements(By.cssSelector(cssColumnSelector));
        return (ciStandardFieldFieldCols.get(1).findElement(By.tagName("select")).isEnabled()) ? true : false;
    }

    /**
     * get Default selected value from Mapping Rule field
     *
     * @return default selected string
     */
    public String getMappingRuleField() {
        List<WebElement> ciStandardFieldFieldCols = getCostingInputRows().get(getCostingInputRows().size() - 1).findElements(By.cssSelector(cssColumnSelector));
        return ciStandardFieldFieldCols.get(1).getText();
    }

    /**
     * Add Costing Inputs single field
     *
     * @param rowNum - field count
     */
    public void addRow(int rowNum) {
        pageUtils.waitForElementAndClick(ciAddRowButton);
        pageUtils.waitForElementToAppear(ciConnectFieldInitialRowElement);
        String initialFieldRow = ciConnectFieldInitialRowElement.getAttribute("id").split("-")[1].trim();
        Integer rowID = Integer.parseInt(initialFieldRow) + rowNum;
        WebElement ciConnectFieldDropdownElement = driver.findElement(By.cssSelector(ciConnectFieldCss.replace("[ID]", rowID.toString())));
        pageUtils.waitForElementAndClick(ciConnectFieldDropdownElement);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);

        switch (workFlowData.getCostingInputsData().get(rowNum).getFieldName()) {
            case "Material":
            case "Scenario Name":
                selectCostingInputRow(rowNum, rowID, "Textbox");
                break;
            case "Custom Date":
                selectCostingInputRow(rowNum, rowID, "DatePicker");
                break;
            default:
                selectCostingInputRow(rowNum, rowID, "Dropdown");
        }
    }

    /**
     * add Costing Input Row
     *
     * @param plmTypeAttributes - Costing input field
     * @param mappingRule       - MappingRuleEnum
     * @param fieldValue        - field value
     * @return CostingInputsPart
     */
    public CostingInputsPart addCostingInputRow(PlmTypeAttributes plmTypeAttributes, MappingRule mappingRule, String fieldValue) {
        this.clickAddRowBtn();
        List<WebElement> ciStandardFieldFieldCols = getCostingInputRows().get(getCostingInputRows().size() - 1).findElements(By.cssSelector(cssColumnSelector));
        selectCiConnectField(ciStandardFieldFieldCols.get(0), plmTypeAttributes);
        if (plmTypeAttributes.getCicGuiField().equals("Process Group") ||
            plmTypeAttributes.getCicGuiField().equals("Digital Factory")) {
            selectMappingRule(ciStandardFieldFieldCols.get(1), mappingRule);
            selectFieldValue(ciStandardFieldFieldCols.get(3), fieldValue);
        } else if (plmTypeAttributes.getCicGuiField().equals("Machining Mode")) {
            pageUtils.checkDropdownOptions(ciStandardFieldFieldCols.get(3).findElement(By.tagName("select")), fieldValue);
            pageUtils.waitForElementAndClick(ciStandardFieldFieldCols.get(3));
            this.selectValueFromDDL(fieldValue);
        } else {
            selectMappingRule(ciStandardFieldFieldCols.get(1), mappingRule);
            enterRuleValue(ciStandardFieldFieldCols.get(2).findElement(By.cssSelector(cssTextboxSelector)), fieldValue);
        }
        if (plmTypeAttributes.getCicGuiField().contains("Date")) {
            selectDateForRuleValue(ciStandardFieldFieldCols.get(2));
        }
        return this;
    }

    /**
     * get the list of standard mappings rows
     *
     * @return list of standard mappings rows
     */
    public List<WebElement> getCostingInputRows() {
        return driver.findElements(By.xpath("//div[@tab-number='3']//div[contains(@class, 'BMCollectionViewCellEditing')]//div[contains(@class, 'tw-flex-row')]"));
    }


    /**
     * click add row button in standard mappings tab
     */
    public CostingInputsPart clickAddRowBtn() {
        pageUtils.waitForElementAndClick(addRowBtn);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        waitUntilRowsLoaded();
        return this;
    }

    /**
     * select CI Connect field in Standard mappings rows
     *
     * @param plmTypeAttributes - PlmTypeAttributes enum
     */
    public CostingInputsPart selectCiConnectField(PlmTypeAttributes plmTypeAttributes) {
        List<WebElement> ciStandardFieldFieldCols = getCostingInputRows().get(getCostingInputRows().size() - 1).findElements(By.cssSelector(cssColumnSelector));
        selectCiConnectField(ciStandardFieldFieldCols.get(0), plmTypeAttributes);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return this;
    }

    /**
     * select CI Connect field in Standard mappings rows
     *
     * @param webElement        - selected row element
     * @param plmTypeAttributes - PlmTypeAttributes enum
     */
    private void selectCiConnectField(WebElement webElement, PlmTypeAttributes plmTypeAttributes) {
        pageUtils.waitUntilDropdownOptionsLoaded(webElement.findElement(By.tagName("select")));
        pageUtils.waitForElementAndClick(webElement);
        this.selectValueFromDDL(plmTypeAttributes.getCicGuiField());
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
    }

    /**
     * Select Usage Rule
     *
     * @param webElement  - selected row element
     * @param mappingRule - MappingRule
     */
    private void selectMappingRule(WebElement webElement, MappingRule mappingRule) {
        pageUtils.waitUntilDropdownOptionsLoaded(webElement.findElement(By.tagName("select")));
        WebElement mappingRuleElement = webElement.findElement(By.tagName("div")).findElement(By.tagName("div")).findElement(By.tagName("div"));
        pageUtils.waitForElementAttributeToAppear(mappingRuleElement, "class", "ss-single-selected");
        pageUtils.waitForElementAndClick(webElement);
        this.selectValueFromDDL(mappingRule.getMappingRule());
    }

    /**
     * Select Usage Rule
     *
     * @param webElement - selected row element
     * @param fieldValue - connector field value
     */
    private void selectFieldValue(WebElement webElement, String fieldValue) {
        pageUtils.waitUntilDropdownOptionsLoaded(webElement.findElement(By.tagName("select")));
        WebElement mappingRuleElement = webElement.findElement(By.tagName("div")).findElement(By.tagName("div")).findElement(By.tagName("div"));
        pageUtils.waitForElementAttributeToAppear(mappingRuleElement, "class", "ss-single-selected");
        pageUtils.waitForElementAndClick(webElement);
        this.selectValueFromDDL(fieldValue);
    }

    /**
     * Select Date
     *
     * @param ruleValueElement
     */
    private void selectDateForRuleValue(WebElement ruleValueElement) {
        pageUtils.waitForElementAndClick(ruleValueElement);
        pageUtils.waitForElementToAppear(customDateCalenderTodayButton);
        pageUtils.waitForElementAndClick(customDateCalenderDoneButton);
    }

    /**
     * enter Rule Value
     *
     * @param webElement - selected row element
     * @param ruleValue  - MappingRule
     */
    private void enterRuleValue(WebElement webElement, String ruleValue) {
        pageUtils.waitForElementToBeClickable(webElement);
        pageUtils.clearValueOfElement(webElement);
        pageUtils.setValueOfElement(webElement, ruleValue);
        pageUtils.waitForElementToBeClickable(webElement);
    }

    /**
     * wait until all the rows are loaded in standard mappings
     */
    @SneakyThrows
    private void waitUntilRowsLoaded() {
        int retries = 0;
        int maxRetries = 12;
        Exception ex = null;

        do {
            TimeUnit.SECONDS.sleep(DEFAULT_WAIT_TIME);
            if (getCostingInputRows().size() >= 1) {
                log.info("Costing Input rows are loaded!!");
                break;
            }
            if (retries == maxRetries) {
                throw new RuntimeException(String.format("Costing Input rows are not loaded !! : %s", ex.getMessage()));
            }
            retries++;
        } while (retries < maxRetries);
    }

    private void selectCostingInputRow(int dataRowNum, Integer cssRowNum, String valueFieldType) {
        this.selectValueFromDDL(workFlowData.getCostingInputsData().get(dataRowNum).getFieldName());
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        // Select Operator
        WebElement ciConnectOperatorDropdownElement = driver.findElement(By.cssSelector(operatorDDCss.replace("[ID]", cssRowNum.toString())));
        if (!ciConnectOperatorDropdownElement.findElement(By.tagName("div")).findElement(By.tagName("div")).getAttribute("class").contains("ss-disabled")) {
            pageUtils.waitForElementAndClick(ciConnectOperatorDropdownElement);
            pageUtils.waitFor(Constants.DEFAULT_WAIT);
            this.selectValueFromDDL(workFlowData.getCostingInputsData().get(dataRowNum).getOperatorName());
            pageUtils.waitFor(Constants.DEFAULT_WAIT);
        }

        switch (valueFieldType) {
            case "Dropdown":
                //Select Field Value
                WebElement ciConnectFieldValueDropDownElement = driver.findElement(By.cssSelector(valueDDCss.replace("[ID]", cssRowNum.toString())));
                pageUtils.waitForElementAndClick(ciConnectFieldValueDropDownElement);
                pageUtils.waitFor(Constants.DEFAULT_WAIT);
                this.selectValueFromDDL(workFlowData.getCostingInputsData().get(dataRowNum).getFieldValue());
                break;
            case "Textbox":
                WebElement ciConnectFieldValueTxtElement = driver.findElement(By.cssSelector(valueTxtCss.replace("[ID]", cssRowNum.toString())));
                workFlowData.getCostingInputsData().get(dataRowNum).setFieldValue(GenerateStringUtil.saltString(workFlowData.getCostingInputsData().get(dataRowNum).getFieldValue()));
                pageUtils.waitForElementAndClick(ciConnectFieldValueTxtElement);
                pageUtils.waitFor(Constants.DEFAULT_WAIT);
                ciConnectFieldValueTxtElement.sendKeys(workFlowData.getCostingInputsData().get(dataRowNum).getFieldValue() + Keys.TAB);
                break;
            case "DatePicker":
                WebElement ciConnectFieldValueDtElement = driver.findElement(By.cssSelector(customDateValuePickerCss.replace("[ID]", cssRowNum.toString())));
                this.customDateFieldValueElement = ciConnectFieldValueDtElement;
                workFlowData.getCostingInputsData().get(dataRowNum).setFieldValue(GenerateStringUtil.saltString(workFlowData.getCostingInputsData().get(dataRowNum).getFieldValue()));
                pageUtils.waitForElementAndClick(ciConnectFieldValueDtElement);
                pageUtils.waitForElementToAppear(customDateCalenderTodayButton);
                pageUtils.waitForElementAndClick(customDateCalenderDoneButton);
                break;
        }
    }

}
