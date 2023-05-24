package com.apriori.pages.workflows.schedule.querydefinitions;

import com.apriori.enums.CheckboxState;
import com.apriori.pagedata.WorkFlowData;
import com.apriori.pages.CICBasePage;
import com.apriori.pages.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.pages.workflows.schedule.details.DetailsPart;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Query Definitions part during workflow creation or modification process
 */
@Slf4j
public class QueryDefinitions extends CICBasePage {

    private static final Logger logger = LoggerFactory.getLogger(QueryDefinitions.class);

    @FindBy(css = "div[class='rules-list'] div[class='rule-container']")
    private WebElement ciQDRulesRowAttrib;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_QueryBuilder-110']")
    private WebElement queryOperatorDropdown;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_QueryBuilder-110_rule_[ID]'] > div.rule-value-container > input")
    private WebElement queryValueField;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_QueryBuilder-110_rule_[ID]'] > div.rule-header > div > button")
    private WebElement deleteRuleButton;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_button-104'] > button")
    private WebElement queryNextButton;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_checkbox-393'] > label > span")
    public WebElement workflowQDReturnOnlyCheckbox;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_QueryBuilder-110_group_0'] > div.rules-group-header > div.btn-group.pull-right.group-actions > button[data-add='rule']")
    private WebElement addRuleButton;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_QueryBuilder-110_group_0'] > div.rules-group-body > div")
    private WebElement rulesList;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id*='-popup_QueryBuilder-110_group'] > div.rules-group-body > div")
    private WebElement groupsList;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_button-103'] > button > span.widget-button-text")
    private WebElement qdPreviousButton;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_textbox-148'] > table > tbody > tr > td > input")
    private WebElement worflowNameField;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_QueryBuilder-110_group_0'] > div.rules-group-header > div.btn-group.pull-right.group-actions > button[data-add='group']")
    private WebElement addGroupButton;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id*='-popup_QueryBuilder-110_group_'] > div.rules-group-header")
    private WebElement addGroupRuleHeader;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_QueryBuilder-110_rule_0'] > div.rules-group-header > div.btn-group.pull-right.group-actions > button[data-delete='rule']")
    private WebElement deleteRuleLineButton;

    private String queryDropdownCss = PARENT_ELEMENT_CSS + "[id$='-popup_QueryBuilder-110_rule_[ID]'] > div.rule-filter-container > select";
    private String queryOperatorDropdownCss = PARENT_ELEMENT_CSS + "[id$='-popup_QueryBuilder-110_rule_[ID]'] > div.rule-operator-container > select";
    private String queryValueTxtCss = PARENT_ELEMENT_CSS + "[id$='-popup_QueryBuilder-110_rule_[ID]'] > div.rule-value-container > input";
    private String deleteRuleButtonCss = PARENT_ELEMENT_CSS + "[id$='-popup_QueryBuilder-110_rule_[ID]'] > div.rule-header > div > button";
    private String queryGroupButtonCss = PARENT_ELEMENT_CSS + "[id$='-popup_QueryBuilder-110_group_[ID]'] > div.rules-group-header > div > button";
    private String deleteRuleLineButtonCss = PARENT_ELEMENT_CSS + "[id$='-popup_QueryBuilder-110_rule_[ID]'] > div.rule-header > div > button[data-delete='rule']";

    public QueryDefinitions(WebDriver driver) {
        super(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() {
        pageUtils.waitForElementToAppear(workflowQDReturnOnlyCheckbox);
    }


    /**
     * Add default Rule
     *
     * @param workFlowData
     * @param numOfRules
     * @return
     */


    public QueryDefinitions addRule(WorkFlowData workFlowData, int numOfRules) {
        String str = ciQDRulesRowAttrib.getAttribute("id");
        String initialFieldRow = str.substring(str.length() - 1).trim();
        for (int row = 0; row < numOfRules; row++) {
            Integer rowID = Integer.parseInt(initialFieldRow) + row;
            WebElement ciRuleDropdownElement = driver.findElement(By.cssSelector(queryDropdownCss.replace("[ID]", rowID.toString())));
            pageUtils.waitForElementToBeClickable(ciRuleDropdownElement);
            Select optFilter = new Select(ciRuleDropdownElement);
            optFilter.selectByValue(workFlowData.getQueryDefinitionsData().get(row).getFieldName());
            if (workFlowData.getQueryDefinitionsData().get(row).getFieldOperator() != "") {
                WebElement ciRuleOperatorDropdownElement = driver.findElement(By.cssSelector(queryOperatorDropdownCss.replace("[ID]", rowID.toString())));
                pageUtils.waitForElementToBeClickable(ciRuleOperatorDropdownElement);
                Select optOperator = new Select(ciRuleOperatorDropdownElement);
                optOperator.selectByValue(workFlowData.getQueryDefinitionsData().get(row).getFieldOperator());
            }
            if (workFlowData.getQueryDefinitionsData().get(row).getFieldValue() != "") {
                WebElement ciRuleValueTxtElement = driver.findElement(By.cssSelector(queryValueTxtCss.replace("[ID]", rowID.toString())));
                pageUtils.waitForElementAndClick(ciRuleValueTxtElement);
                ciRuleValueTxtElement.sendKeys(workFlowData.getQueryDefinitionsData().get(row).getFieldValue() + Keys.TAB);
            }
            if (row != numOfRules - 1) {
                pageUtils.waitForElementAndClick(addRuleButton);
            }
        }
        return this;
    }

    /**
     * Click Previous Workflow button
     *
     * @return NewEditWorkflow page object
     */
    public DetailsPart clickPreviousBtn() {
        pageUtils.waitForElementToBeClickable(qdPreviousButton);
        pageUtils.waitForElementToAppear(qdPreviousButton);
        if (!qdPreviousButton.isEnabled()) {
            logger.warn("Next button in Query Definitions Page is not enabled");
            return null;
        }
        pageUtils.waitForElementAndClick(qdPreviousButton);
        if (!pageUtils.isElementDisplayed(worflowNameField)) {
            return null;
        }
        return new DetailsPart(driver);
    }

    /**
     * Click New Workflow button
     *
     * @return WFQueryDefinitions page object
     */
    public CostingInputsPart clickWFQueryDefNextBtn() {
        pageUtils.waitForElementToAppear(queryNextButton);
        if (queryNextButton.isEnabled()) {
            pageUtils.waitForElementAndClick(queryNextButton);
            return new CostingInputsPart(this.driver);
        }
        return null;
    }

    /**
     * Delete the last rule
     *
     * @return Number of rules
     */
    public int deleteRule() {
        Integer ruleNumber = getNumberOfRules() - 1;
        String deleteRule = deleteRuleButtonCss.replace("[ID]", ruleNumber.toString());
        WebElement deleteButton = driver.findElement(By.cssSelector(deleteRule));
        pageUtils.waitForElementAndClick(deleteButton);
        return getNumberOfRules();
    }


    /**
     * Get the number of rules displayed on the Query Definition tab
     *
     * @return Number of Rules
     */
    public int getNumberOfRules() {
        pageUtils.waitForElementToBeClickable(rulesList);
        return rulesList.findElements(By.cssSelector("div.rule-container")).size();
    }

    /**
     * Get the number of groups displayed on the Query Definition tab
     *
     * @return Number of Groups
     */
    public int getNumberOfGroups() {
        pageUtils.waitForElementToBeClickable(groupsList);
        return groupsList.findElements(By.cssSelector("div.rules-group-container")).size();
    }

    /**
     * Verify add group button is displayed
     *
     * @return boolean
     */
    public boolean isGroupButtonDisplayed() {
        return pageUtils.waitForWebElement(addGroupButton);
    }

    public WebElement getQueryDefinitionRuleNameDdl(Integer index) {
        return driver.findElement(By.cssSelector(queryDropdownCss.replace("[ID]", index.toString())));
    }

    public WebElement getQueryDefinitionRuleOperatorDdl(Integer index) {
        return driver.findElement(By.cssSelector(queryOperatorDropdownCss.replace("[ID]", index.toString())));
    }

    public WebElement getQueryDefinitionRuleValueTxt(Integer index) {
        return driver.findElement(By.cssSelector(queryValueTxtCss.replace("[ID]", index.toString())));
    }

    /**
     * select rule name
     *
     * @param fieldName field name
     * @return QueryDefinitions
     */
    public QueryDefinitions selectRuleNameFromDdl(String fieldName) {
        WebElement ciRuleDropdownElement = this.getQueryDefinitionRuleNameDdl(0);
        pageUtils.waitForElementToBeClickable(ciRuleDropdownElement);
        Select optFilter = new Select(ciRuleDropdownElement);
        optFilter.selectByValue(fieldName);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        return this;
    }

    /**
     * click Add Rule button
     *
     * @return QueryDefinitions
     */
    public QueryDefinitions clickAddRuleButton() {
        pageUtils.waitForElementAndClick(addRuleButton);
        return this;
    }

    /**
     * click delete rule button
     *
     * @return QueryDefinitions
     */
    public QueryDefinitions clickDeleteRuleButton() {
        pageUtils.waitForElementAndClick(deleteRuleButton);
        return this;
    }

    /**
     * click add group button
     *
     * @return QueryDefinitions
     */
    public QueryDefinitions clickAddGroupButton() {
        pageUtils.waitForElementAndClick(addGroupButton);
        return this;
    }

    /**
     * verify operator rule value drop down
     *
     * @param index
     * @return
     */
    public Boolean verifyQueryDefinitionRuleOperatorDdl(Integer index) {
        try {
            return pageUtils.waitForWebElement(driver.findElement(By.cssSelector(queryOperatorDropdownCss.replace("[ID]", index.toString()))));
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * verify operator rule value text field
     *
     * @param index
     * @return
     */
    public Boolean verifyQueryDefinitionRuleValueTxt(Integer index) {
        try {
            return pageUtils.waitForWebElement(driver.findElement(By.cssSelector(queryValueTxtCss.replace("[ID]", index.toString()))));
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * select Return latest revision checkbox
     *
     * @param checkboxState
     * @return QueryDefinitions
     */
    public QueryDefinitions selectReturnLatestRevisionOnlyCheckbox(CheckboxState checkboxState) {
        if (checkboxState.toString().equals("on")) {
            if (!getQDReturnOnlyCheckboxElement().isSelected()) {
                pageUtils.waitForElementAndClick(revisionLatestCheckBoxLblElement);
                log.info("Return only the latest revision of each part from the PLM system Checkbox is selected");
            }
        }

        if (checkboxState.toString().equals("off")) {
            if (getQDReturnOnlyCheckboxElement().isSelected()) {
                pageUtils.waitForElementAndClick(revisionLatestCheckBoxLblElement);
                log.info("Return only the latest revision of each part from the PLM system Checkbox is not selected");
            }
        }
        return this;
    }

    /**
     * return page objects based on rule number
     *
     * @param ruleNumber     The row number of the rule, if new rule then last rule number + 1
     * @param filterSelected True if a filter for this rule number has been selected
     * @return Filter, Operator and Value elements
     */
    private Map<String, WebElement> getQueryElements(Integer ruleNumber, boolean filterSelected) {
        Map<String, WebElement> elements = new HashMap<>();
        String filterSelectString = queryDropdownCss.replace("[ID]", ruleNumber.toString());
        String operatorSelectString = queryOperatorDropdownCss.replace("[ID]", ruleNumber.toString());
        String valueFieldString = queryValueTxtCss.replace("[ID]", ruleNumber.toString());

        WebElement filterSelectEle = driver.findElement(By.cssSelector(filterSelectString));

        WebElement operatorSelectEle = null;
        WebElement valueFieldEle = null;
        if (filterSelected) {
            operatorSelectEle = driver.findElement(By.cssSelector(operatorSelectString));
            valueFieldEle = driver.findElement(By.cssSelector(valueFieldString));
        }

        elements.put("filter", filterSelectEle);
        elements.put("operator", operatorSelectEle);
        elements.put("value", valueFieldEle);
        return elements;
    }
}
