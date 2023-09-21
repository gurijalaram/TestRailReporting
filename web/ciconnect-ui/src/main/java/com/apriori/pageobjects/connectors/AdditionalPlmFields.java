package com.apriori.pageobjects.connectors;

import com.apriori.cic.enums.PlmTypeAttributes;
import com.apriori.enums.ConnectorColumnFields;
import com.apriori.enums.FieldDataType;
import com.apriori.enums.UsageRule;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Slf4j
public class AdditionalPlmFields extends ConnectorMappings {

    @FindBy(xpath = "//div[@class='tabsv2-actual-tab-contents']//div[@tab-number='2']//button//span[text()='Add Row']")
    private WebElement addRowBtn;

    public AdditionalPlmFields(WebDriver driver) {
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
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        if (pageUtils.isElementDisplayed(statusMessageLbl)) {
            throw new RuntimeException(statusMessageLbl.getText());
        }
    }

    /**
     * click add row button in standard mappings tab
     */
    public AdditionalPlmFields clickAddRowBtn() {
        pageUtils.waitForElementAndClick(addRowBtn);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return this;
    }

    /**
     * get the list of standard mappings rows
     *
     * @return list of standard mappings rows
     */
    public List<WebElement> getAdditionalPlmFieldsRows() {
        return driver.findElements(By.cssSelector("div[class='tabsv2-actual-tab-contents'] div[tab-number='2'] div[class^='BMCollectionViewCellWrapper'] div[class$='tw-flex-row']"));
    }

    /**
     * Select current row
     *
     * @return current class object
     */
    public AdditionalPlmFields selectRow() {
        selectedRow = getAdditionalPlmFieldsRows().get(getAdditionalPlmFieldsRows().size() - 1);
        return this;
    }

    /**
     * Add row to a connector Mappings -> Standard Mappings Row
     *
     * @param ciConnectFieldName - PlmTypeAttributes
     * @param usageRule          - UsageRule Enum
     * @param plmFieldName
     * @return Current class object
     */
    public AdditionalPlmFields addRow(PlmTypeAttributes plmTypeAttributes, UsageRule usageRule, FieldDataType fieldDataType) {
        this.clickAddRowBtn();
        enterCiConnectField(plmTypeAttributes.getCicGuiField());
        selectUsageRule(usageRule);
        enterPlmField(plmTypeAttributes.getValue());
        selectDataType(fieldDataType);
        return this;
    }

    /**
     * enter connect field value
     *
     * @param ciConnectFieldName
     * @return current class object
     */
    private AdditionalPlmFields enterCiConnectField(String ciConnectFieldName) {
        pageUtils.clearValueOfElement(getColumnsFromSelectedRow()
            .get(ConnectorColumnFields.CI_CONNECT_FIELD.getColumnIndex())
            .findElement(By.cssSelector(cssTextboxSelector)));
        getColumnsFromSelectedRow()
            .get(ConnectorColumnFields.CI_CONNECT_FIELD.getColumnIndex())
            .findElement(By.cssSelector(cssTextboxSelector))
            .sendKeys(ciConnectFieldName);
        return this;
    }

    /**
     * Select Usage Rule
     *
     * @param usageRule - UsageRule
     */
    private void selectUsageRule(UsageRule usageRule) {
        pageUtils.waitForElementAndClick(getColumnsFromSelectedRow().get(ConnectorColumnFields.USAGE.getColumnIndex()));
        this.selectValueFromDDL(0, usageRule.getUsageRule());
    }

    /**
     * select data type
     *
     * @param fieldDataType
     */
    private void selectDataType(FieldDataType fieldDataType) {
        pageUtils.waitForElementAndClick(getColumnsFromSelectedRow().get(ConnectorColumnFields.DATA_TYPE.getColumnIndex()));
        this.selectValueFromDDL(0, fieldDataType.getDataType());
    }

    /**
     * enter PLM Field
     *
     * @param plmFieldName
     * @return Current class object
     */
    private AdditionalPlmFields enterPlmField(String plmFieldName) {
        pageUtils.clearValueOfElement(getColumnsFromSelectedRow()
            .get(ConnectorColumnFields.PLM_FIELD.getColumnIndex())
            .findElement(By.cssSelector(cssTextboxSelector)));
        getColumnsFromSelectedRow()
            .get(ConnectorColumnFields.PLM_FIELD.getColumnIndex())
            .findElement(By.cssSelector(cssTextboxSelector))
            .sendKeys(plmFieldName);
        return this;
    }

    /**
     * get rows from addtitional fields list
     *
     * @return List<WebElement>
     */
    private List<WebElement> getColumnsFromSelectedRow() {
        return getAdditionalPlmFieldsRows().get(getAdditionalPlmFieldsRows().size() - 1).findElements(By.cssSelector(cssColumnSelector));
    }
}
