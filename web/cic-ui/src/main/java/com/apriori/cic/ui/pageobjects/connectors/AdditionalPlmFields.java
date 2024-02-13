package com.apriori.cic.ui.pageobjects.connectors;

import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.ui.enums.ConnectorColumnFields;
import com.apriori.cic.ui.enums.FieldDataType;
import com.apriori.cic.ui.enums.UsageRule;

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
    public WebElement getCurrentRow() {
        return getAdditionalPlmFieldsRows().get(getAdditionalPlmFieldsRows().size() - 1);
    }

    /**
     * Add row to a connector Mappings -> Standard Mappings Row
     *
     * @param plmTypeAttributes - PlmTypeAttributes
     * @param usageRule         - UsageRule Enum
     * @param fieldDataType     - FieldDataTypeenum
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
     * is remove button displayed
     * @return boolean
     */
    public Boolean isRemoveRowBtnDisplayed() {
        return pageUtils.isElementDisplayed(getCurrentRow().findElements(By.cssSelector("button")).get(0));
    }

    /**
     * Verify PLM Field text box is enabled
     *
     * @return Boolean
     */
    public Boolean isPlmFieldEnabled(PlmTypeAttributes plmTypeAttributes) {
        WebElement element = getCurrentRow().findElements(By.cssSelector(cssTextboxSelector)).get(0);
        return pageUtils.isElementEnabled(element);
    }

    /**
     * verify CI Connect Field is Enabled
     *
     * @param plmTypeAttributes - PlmTypeAttributes enum
     * @return boolean
     */
    public Boolean isCiConnectFieldEnabled(PlmTypeAttributes plmTypeAttributes) {
        List<WebElement> ciStandardFieldFieldCols = getCurrentRow().findElements(By.cssSelector(cssColumnSelector));
        return pageUtils.isElementEnabled(ciStandardFieldFieldCols.get(ConnectorColumnFields.CI_CONNECT_FIELD.getColumnIndex() + 1)
            .findElement(By.cssSelector(cssTextboxSelector)));
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
