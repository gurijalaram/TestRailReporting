package com.apriori.pages.workflows.schedule.costinginputs;

import com.apriori.pages.CICBasePage;
import com.apriori.pages.workflows.schedule.details.DetailsPart;
import com.apriori.pages.workflows.schedule.notifications.NotificationsPart;
import com.apriori.pages.workflows.schedule.querydefinitions.QueryDefinitions;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;
import utils.UIUtils;

import java.util.List;


/**
 * Costing Inputs part in new workflow process or edit workflow process
 */
public class CostingInputsPart extends CICBasePage {

    private static final Logger logger = LoggerFactory.getLogger(CostingInputsPart.class);

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_button-189'] > button")
    private WebElement queryAddRowButton;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_button-108'] > button > span.widget-button-text")
    private WebElement ciNextButton;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_button-107'] > button > span.widget-button-text")
    private WebElement ciPreviousButton;

    @FindBy(css = "div[class='BMCollectionViewCellWrapper BMCollectionViewCellEditing'] div[class='BMCollectionViewCell BMCollectionViewCellHoverable']")
    private WebElement ciConnectFieldInitialRowElement;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_checkbox-393'] > label > span")
    private WebElement workflowQDReturnOnlyCheckbox;

    @FindBy(css = PARENT_ELEMENT_CSS + "[id$='-popup_textbox-148'] > table > tbody > tr > td > input")
    private WebElement worflowNameField;

    private String ciConnectFieldCss = "#CIC_CostingInputCell_MU-[ID]_DrowpdownWidget-3";
    private String valueDDCss = "#CIC_CostingInputCell_MU-[ID]_DrowpdownWidget-20";
    private String valueTxtCss = "#CIC_CostingInputCell_MU-[ID]" + "_textbox-5 > table > tbody > tr > td > input";
    private String connectFieldDDCss = "div[class^='ss-content ss-'][class$='ss-open'] div[class='ss-list']";

    public CostingInputsPart(WebDriver driver) {
        super(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
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
            logger.error(e.getMessage());
        }
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
     * Click Previous Workflow button
     *
     * @return NewEditWorkflow page object
     */
    public Object clickPreviousBtn() {
        pageUtils.waitForElementToBeClickable(ciPreviousButton);
        pageUtils.waitForElementToAppear(ciPreviousButton);
        Object object = null;
        if (!ciPreviousButton.isEnabled()) {
            logger.warn("Next button in Query Definitions Page is not enabled");
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
     * Add Costing Inputs single field
     *
     * @param rowNum - field count
     */
    public void addRow(int rowNum) {
        pageUtils.waitForElementAndClick(queryAddRowButton);
        pageUtils.waitForElementToAppear(ciConnectFieldInitialRowElement);
        String initialFieldRow = ciConnectFieldInitialRowElement.getAttribute("id").split("-")[1].trim();
        Integer rowID = Integer.parseInt(initialFieldRow) + rowNum;
        WebElement ciConnectFieldDropdownElement = driver.findElement(By.cssSelector(ciConnectFieldCss.replace("[ID]", rowID.toString())));
        pageUtils.waitForElementAndClick(ciConnectFieldDropdownElement);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        List<WebElement> listOfFieldNameOptions = this.driver.findElements(By.cssSelector(connectFieldDDCss)).get(1)
            .findElements(By.cssSelector("div[class='ss-option']"));

        if (workFlowData.getCostingInputsData().get(rowNum).getFieldName().equals("Scenario Name")) {
            this.selectValueFromDDL(workFlowData.getCostingInputsData().get(rowNum).getFieldName());
            pageUtils.waitFor(Constants.DEFAULT_WAIT);
            WebElement ciConnectFieldValueTxtElement = driver.findElement(By.cssSelector(valueTxtCss.replace("[ID]", rowID.toString())));
            workFlowData.getCostingInputsData().get(rowNum).setFieldValue(UIUtils.saltString(workFlowData.getCostingInputsData().get(rowNum).getFieldValue()));
            pageUtils.waitForElementAndClick(ciConnectFieldValueTxtElement);
            pageUtils.waitFor(Constants.DEFAULT_WAIT);
            ciConnectFieldValueTxtElement.sendKeys(workFlowData.getCostingInputsData().get(rowNum).getFieldValue() + Keys.TAB);
        }
        if (workFlowData.getCostingInputsData().get(rowNum).getFieldName().equals("Process Group")
            || workFlowData.getCostingInputsData().get(rowNum).getFieldName().equals("Digital Factory")) {
            this.selectValueFromDDL(workFlowData.getCostingInputsData().get(rowNum).getFieldName());
            pageUtils.waitFor(Constants.DEFAULT_WAIT);
            WebElement ciConnectFieldValueDropDownElement = driver.findElement(By.cssSelector(valueDDCss.replace("[ID]", rowID.toString())));
            pageUtils.waitForElementAndClick(ciConnectFieldValueDropDownElement);
            pageUtils.waitFor(Constants.DEFAULT_WAIT);
            this.selectValueFromDDL(workFlowData.getCostingInputsData().get(rowNum).getFieldValue());

        } else {
            this.selectValueFromDDL(workFlowData.getCostingInputsData().get(rowNum).getFieldName());
            pageUtils.waitFor(Constants.DEFAULT_WAIT);
            WebElement ciConnectFieldValueTextboxElement = driver.findElement(By.cssSelector(valueTxtCss.replace("[ID]", rowID.toString())));
            pageUtils.waitForElementAndClick(ciConnectFieldValueTextboxElement);
            ciConnectFieldValueTextboxElement.sendKeys(workFlowData.getCostingInputsData().get(rowNum).getFieldValue());
        }
    }
}
