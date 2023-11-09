package com.apriori.cic.ui.pageobjects.workflows.schedule.querydefinitions;

import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.ui.enums.CheckboxState;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.pageobjects.CICBasePage;
import com.apriori.cic.ui.pageobjects.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.details.DetailsPart;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Query Definitions part during workflow creation or modification process
 */
@Slf4j
public class QueryDefinitions extends CICBasePage {

    @FindBy(css = "div[tab-number='2'] div[class='rules-list']")
    private WebElement rulesList;

    @FindBy(css = "div[tab-number='2'] div[class^='rules-group-container'] div[class='rules-group-header']")
    private WebElement rulesGroupHeader;

    @FindBy(xpath = "//div[@tab-number='2']//button[.='Previous']")
    private WebElement qdPreviousButton;

    @FindBy(xpath = "//div[@tab-number='2']//button[.='Cancel']")
    private WebElement qdCancelButton;

    @FindBy(xpath = "//div[@tab-number='2']//button[.='Next']")
    private WebElement qdNextButton;

    public QueryDefinitions(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(revisionLatestCheckBoxLblElement);
    }


    /**
     * click Add Rule button
     *
     * @return QueryDefinitions
     */
    public QueryDefinitions clickAddRuleButton() {
        pageUtils.waitForElementAndClick(getAddRuleBtnElement());
        return this;
    }

    /**
     * click Add Rule button
     *
     * @return QueryDefinitions
     */
    public QueryDefinitions clickAddGroupButton() {
        pageUtils.waitForElementAndClick(getAddGroupBtnElement());
        return this;
    }

    /**
     * delete rule
     *
     * @return current class object
     */
    public QueryDefinitions deleteRule() {
        pageUtils.waitForElementAndClick(getDeleteRuleBtnElement());
        return this;
    }

    /**
     * select Rule name during addition of rule
     *
     * @param filterName fieldName
     * @return current Class object
     */
    public QueryDefinitions selectRuleFilter(PlmTypeAttributes filterName) {
        pageUtils.selectDropdownOption(getRuleFilterNameDdlElement(), filterName.getCicGuiField());
        //pageUtils.waitFor(Constants.DEFAULT_WAIT);
        return this;
    }

    /**
     * select rule operation
     *
     * @param ruleOperatorEnum - RuleOperatorEnum
     * @return current class object
     */
    public QueryDefinitions selectRuleOperator(RuleOperatorEnum ruleOperatorEnum) {
        pageUtils.selectDropdownOption(getRuleOperatorDdlElement(), ruleOperatorEnum.getRuleOperator());
        return this;
    }

    /**
     * select rule value
     *
     * @param ruleValue - value of rule name
     * @return current class object
     */
    public QueryDefinitions enterRuleValue(String ruleValue) {
        pageUtils.clearValueOfElement(getRuleValueTxtElement());
        getRuleValueTxtElement().sendKeys(ruleValue);
        return this;
    }

    /**
     * Add Rule in Query Definitions
     *
     * @param ruleFilterName - PlmTypeAttributes
     * @param ruleOperator   - RuleOperatorEnum
     * @param ruleValue      value of rule filter name
     * @return current Class object
     */
    public QueryDefinitions addRule(PlmTypeAttributes ruleFilterName, RuleOperatorEnum ruleOperator, String ruleValue) {
        this.selectRuleFilter(ruleFilterName);
        this.selectRuleOperator(ruleOperator);
        this.enterRuleValue(ruleValue + Keys.TAB);
        return this;
    }

    /**
     * Click New Workflow button
     *
     * @return WFQueryDefinitions page object
     */
    public CostingInputsPart clickWFQueryDefNextBtn() {
        pageUtils.waitForElementToBeClickable(qdNextButton);
        if (qdNextButton.isEnabled()) {
            pageUtils.waitForElementAndClick(qdNextButton);
            return new CostingInputsPart(this.driver);
        } else {
            throw new RuntimeException("Next Button in Query Definitions sections is in disabled mode");
        }
    }

    /**
     * Click Previous Workflow button
     *
     * @return NewEditWorkflow page object
     */
    public DetailsPart clickPreviousBtn() {
        pageUtils.waitForElementToBeClickable(qdPreviousButton);
        if (!qdPreviousButton.isEnabled()) {
            log.warn("Next button in Query Definitions Page is not enabled");
        }
        pageUtils.waitForElementAndClick(qdPreviousButton);
        if (!pageUtils.isElementDisplayed(getNameTextFieldElement())) {
            log.warn("Next button in Query Definitions Page is not enabled");
        }
        return new DetailsPart(driver);
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
     * get rule filter name dropdown element
     *
     * @return WebElement
     */
    public WebElement getRuleFilterNameDdlElement() {
        return getRulesList()
            .get(getRulesList().size() - 1)
            .findElement(By.cssSelector("div[class='rule-filter-container'] select"));
    }

    /**
     * get rule operator element
     *
     * @return Current Class object
     */
    public WebElement getRuleOperatorDdlElement() {
        return getRulesList()
            .get(getRulesList().size() - 1)
            .findElement(By.cssSelector("div[class='rule-operator-container'] select"));
    }

    /**
     * get rule value text element
     *
     * @return WebElement
     */
    public WebElement getRuleValueTxtElement() {
        return getRulesList()
            .get(getRulesList().size() - 1)
            .findElement(By.cssSelector("div[class='rule-value-container'] input"));
    }

    /**
     * wait until all the rows are loaded in standard mappings
     */
    @SneakyThrows
    private void waitUntilRulesLoaded() {
        int retries = 0;
        int maxRetries = 12;
        Exception ex = null;

        while (retries < maxRetries) {
            if (getRules().size() >= 1) {
                log.info("Query Definition Rules are loaded!!");
                break;
            }
            TimeUnit.SECONDS.sleep(DEFAULT_WAIT_TIME);
            retries++;
            if (retries == maxRetries) {
                throw new RuntimeException(String.format("Query Definition Rules are not loaded !! : %s", ex.getMessage()));
            }
        }
    }

    /**
     * get GroupList element
     *
     * @return list of groups
     */
    private List<WebElement> getGroupsList() {
        return driver.findElements(By.cssSelector("div[tab-number='2'] div[class^='rules-group-container']"));
    }

    /**
     * get rules list
     *
     * @return get list of rules
     */
    private List<WebElement> getRules() {
        return getGroupsList().get(getGroupsList().size() - 1).findElements(By.cssSelector("div[class='rules-list']"));
    }

    /**
     * get header names of group
     *
     * @return get the list of group header for each group
     */
    private List<WebElement> getGroupHeaders() {
        return getGroupsList().get(getGroupsList().size() - 1).findElements(By.cssSelector("div[class='rules-group-header']"));
    }

    /**
     * get list of rules for each group of rules
     *
     * @return list of rules under each group-rule section
     */
    public List<WebElement> getRulesList() {
        return getRules().get(getRules().size() - 1).findElements(By.cssSelector("div[class^='rule-container']"));
    }

    /**
     * get Add rule button element
     *
     * @return WebElement
     */
    private WebElement getAddRuleBtnElement() {
        return getGroupHeaders()
            .get(getGroupHeaders().size() - 1)
            .findElement(By.cssSelector("button[data-add='rule']"));
    }

    /**
     * get Add Group button Element
     *
     * @return WebElement
     */
    private WebElement getAddGroupBtnElement() {
        return getGroupHeaders()
            .get(getGroupHeaders().size() - 1)
            .findElement(By.cssSelector("button[data-add='group']"));
    }

    /**
     * get delete rule button element
     *
     * @return WebElement
     */
    private WebElement getDeleteRuleBtnElement() {
        return getRulesList()
            .get(getRulesList().size() - 1)
            .findElement(By.cssSelector("div[class='rule-header'] button"));
    }

    /**
     * Verify add group button is displayed
     *
     * @return boolean
     */
    public Boolean isGroupButtonDisplayed() {
        try {
            return pageUtils.waitForWebElement(getAddGroupBtnElement());
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * verify operator rule value drop down
     *
     * @return
     */
    public Boolean verifyQueryDefinitionRuleOperatorDdl() {
        try {
            return pageUtils.waitForWebElement(getRuleOperatorDdlElement());
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * verify operator rule value text field
     *
     * @return
     */
    public Boolean verifyQueryDefinitionRuleValueTxt() {
        try {
            return pageUtils.waitForWebElement(getRuleValueTxtElement());
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}