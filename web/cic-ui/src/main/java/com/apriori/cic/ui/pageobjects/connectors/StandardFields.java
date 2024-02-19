package com.apriori.cic.ui.pageobjects.connectors;

import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.ui.enums.ConnectorColumnFields;
import com.apriori.cic.ui.enums.UsageRule;
import com.apriori.cic.ui.utils.Constants;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class StandardFields extends ConnectorMappings {

    @FindBy(xpath = "//div[@class='tabsv2-actual-tab-contents']//div[@tab-number='1']//button//span[text()='Add Row']")
    private WebElement addRowBtn;

    private String standardFieldsTabTable = "div[class='tabsv2-actual-tab-contents'] div[tab-number='1'] div[class^='BMCollectionViewCellWrapper'] div[class$='tw-flex-row']";

    public StandardFields(WebDriver driver) {
        super(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
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
        waitUntilRowsLoaded();
    }

    /**
     * get Matching column from connector list table
     *
     * @param plmTypeAttributes - PlmTypeAttributes enum
     * @param colIndex          - column index to search for in connector table
     * @return WebElement
     */
    public WebElement getMatchedConnectFieldColumn(PlmTypeAttributes plmTypeAttributes, Integer colIndex) {
        WebElement matchedElement = getStandardFieldsRows().stream().map(webElement -> webElement.findElements(By.cssSelector(cssColumnSelector))
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
        for (WebElement webElement : getStandardFieldsRows()) {
            ciConnectFieldColElements = webElement.findElements(By.cssSelector(cssColumnSelector));
            WebElement ciConnectFieldColElement = ciConnectFieldColElements.get(0).findElement(By.cssSelector("div[class^='ss-single-selected'] span[class='placeholder']"));
            if (ciConnectFieldColElement.getText().equals(plmTypeAttributes.getCicGuiField())) {
                return ciConnectFieldColElements;
            }
        }
        return ciConnectFieldColElements;
    }

    /**
     * Add row to a connector Mappings -> Standard Mappings Row
     *
     * @param plmTypeAttributes - PlmTypeAttributes
     * @param usageRule         - UsageRule Enum
     * @return Current class object
     */
    public StandardFields addRow(PlmTypeAttributes plmTypeAttributes, UsageRule usageRule) {
        this.clickAddRowBtn();
        List<WebElement> ciStandardFieldFieldCols = getStandardFieldsRows().get(getStandardFieldsRows().size() - 1).findElements(By.cssSelector(cssColumnSelector));
        selectCiConnectField(ciStandardFieldFieldCols.get(ConnectorColumnFields.CI_CONNECT_FIELD.getColumnIndex()), plmTypeAttributes);
        selectUsageRule(ciStandardFieldFieldCols.get(ConnectorColumnFields.USAGE.getColumnIndex() - 1), usageRule);
        pageUtils.clearValueOfElement(ciStandardFieldFieldCols.get(ConnectorColumnFields.PLM_FIELD.getColumnIndex() - 1).findElement(By.cssSelector(cssTextboxSelector)));
        ciStandardFieldFieldCols.get(ConnectorColumnFields.PLM_FIELD.getColumnIndex() - 1).findElement(By.cssSelector(cssTextboxSelector)).sendKeys(plmTypeAttributes.getValue());
        pageUtils.clearValueOfElement(ciStandardFieldFieldCols.get(ConnectorColumnFields.PLM_FIELD.getColumnIndex() - 1).findElement(By.cssSelector(cssTextboxSelector)));
        ciStandardFieldFieldCols.get(ConnectorColumnFields.PLM_FIELD.getColumnIndex() - 1).findElement(By.cssSelector(cssTextboxSelector)).sendKeys(plmTypeAttributes.getValue());
        return this;
    }

    /**
     * remove row to a connector Mappings -> Standard Mappings Row
     *
     * @return Current class object
     */
    public StandardFields deleteRow() {
        WebElement webElement = getCurrentRow().findElement(By.cssSelector("button"));
        pageUtils.waitForElementAndClick(webElement);
        waitUntilRowsLoaded();
        pageUtils.waitForElementNotVisible(webElement, 30);
        return this;
    }

    /**
     * verify Remove Row image button is displayed
     *
     * @return Boolean
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
     * Select CI Connect Field
     *
     * @param plmTypeAttributes - PlmTypeAttributes enum
     * @return current class object
     */
    public StandardFields selectCiConnectField(PlmTypeAttributes plmTypeAttributes) {
        List<WebElement> ciStandardFieldFieldCols = getCurrentRow().findElements(By.cssSelector(cssColumnSelector));
        selectCiConnectField(ciStandardFieldFieldCols.get(ConnectorColumnFields.CI_CONNECT_FIELD.getColumnIndex()), plmTypeAttributes);
        return this;
    }

    /**
     * verify CI Connect Field is Enabled
     *
     * @param plmTypeAttributes - PlmTypeAttributes enum
     * @return boolean
     */
    public Boolean isCiConnectFieldEnabled(PlmTypeAttributes plmTypeAttributes) {
        List<WebElement> ciStandardFieldFieldCols = getCurrentRow().findElements(By.cssSelector(cssColumnSelector));
        return pageUtils.isElementEnabled(ciStandardFieldFieldCols.get(ConnectorColumnFields.CI_CONNECT_FIELD.getColumnIndex()).findElement(By.tagName("select")));
    }

    public String getFieldDataType() {
        return getCurrentRow().findElements(By.cssSelector(cssColumnSelector)).get(ConnectorColumnFields.DATA_TYPE.getColumnIndex() - 1).getText();
    }

    /**
     * Enter the value for standard mappings for pre- loaded mappings.
     *
     * @param plmTypeAttributes PlmTypeAttributes enum
     * @return Current class Object
     */
    public StandardFields enterPlmField(PlmTypeAttributes plmTypeAttributes) {
        pageUtils.clearValueOfElement(getMatchedConnectFieldRow(plmTypeAttributes).get(ConnectorColumnFields.PLM_FIELD.getColumnIndex() - 1).findElement(By.cssSelector(cssTextboxSelector)));
        getMatchedConnectFieldRow(plmTypeAttributes).get(ConnectorColumnFields.PLM_FIELD.getColumnIndex() - 1).findElement(By.cssSelector(cssTextboxSelector)).sendKeys(plmTypeAttributes.getValue());
        return this;
    }

    /**
     * Enter the value for standard mappings for pre- loaded mappings.
     *
     * @param plmTypeAttributes PlmTypeAttributes enum
     * @param attributeValue    String
     * @return Current class Object
     */
    public StandardFields enterPlmField(PlmTypeAttributes plmTypeAttributes, String attributeValue) {
        pageUtils.clearValueOfElement(getMatchedConnectFieldRow(plmTypeAttributes)
            .get(ConnectorColumnFields.PLM_FIELD.getColumnIndex() - 1)
            .findElement(By.cssSelector(cssTextboxSelector)));
        getMatchedConnectFieldRow(plmTypeAttributes)
            .get(ConnectorColumnFields.PLM_FIELD.getColumnIndex() - 1)
            .findElement(By.cssSelector(cssTextboxSelector)).sendKeys(attributeValue);
        return this;
    }

    /**
     * Enter the value for standard mappings for pre- loaded mappings.
     *
     * @param plmTypeAttributes PlmTypeAttributes enum
     * @return String
     */
    public String getPlmFieldValue(PlmTypeAttributes plmTypeAttributes) {
        return getMatchedConnectFieldRow(plmTypeAttributes)
            .get(ConnectorColumnFields.PLM_FIELD.getColumnIndex() - 1)
            .findElement(By.cssSelector(cssTextboxSelector))
            .getAttribute("value");
    }

    /**
     * click add row button in standard mappings tab
     */
    public StandardFields clickAddRowBtn() {
        pageUtils.waitForSteadinessOfElement(By.xpath("//div[@class='tabsv2-actual-tab-contents']//div[@tab-number='1']//button//span[text()='Add Row']"));
        pageUtils.waitForElementAndClick(addRowBtn);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return this;
    }

    /**
     * wait until all the rows are loaded in standard mappings
     */
    @SneakyThrows
    private void waitUntilRowsLoaded() {
        int retries = 0;
        int maxRetries = 12;
        Exception ex = null;

        while (retries < maxRetries) {
            if (getStandardFieldsRows().size() >= 3) {
                log.info("Standard Fields Tab Rows loaded!!");
                break;
            }
            TimeUnit.SECONDS.sleep(DEFAULT_WAIT_TIME);
            retries++;
            if (retries == maxRetries) {
                throw new RuntimeException(String.format("Standard Fields Tab rows are not loaded !! : %s", ex.getMessage()));
            }
        }
    }

    /**
     * get the list of standard mappings rows
     *
     * @return list of standard mappings rows
     */
    public List<WebElement> getStandardFieldsRows() {
        String cssSelector = "div[class='tabsv2-actual-tab-contents'] div[tab-number='1'] div[class^='BMCollectionViewCellWrapper'] div[class$='tw-flex-row']";
        pageUtils.waitForElementsToAppear(By.cssSelector(cssSelector));
        return driver.findElements(By.cssSelector(cssSelector));
    }

    /**
     * select CI Connect field in Standard mappings rows
     */
    private StandardFields clickCiConnectField() {
        List<WebElement> ciStandardFieldFieldCols = getStandardFieldsRows().get(getStandardFieldsRows().size() - 1).findElements(By.cssSelector("div[class*='cic-input']"));
        pageUtils.waitForElementAndClick(ciStandardFieldFieldCols.get(ConnectorColumnFields.CI_CONNECT_FIELD.getColumnIndex()));
        pageUtils.waitForElementAppear(driver.findElement(By.cssSelector("div.ss-content.ss-open> div.ss-search > input")));
        return this;
    }

    /**
     * select CI Connect field in Standard mappings rows
     *
     * @param plmTypeAttributes - PlmTypeAttributes enum
     */
    public Boolean isCiConnectFieldExists(PlmTypeAttributes plmTypeAttributes) {
        return this.driver.findElements(By.cssSelector(OPTIONS_CONTENT_OPEN_DROPDOWN_CSS)).get(0)
            .findElements(By.cssSelector("div[class='ss-option']"))
            .stream()
            .filter(e -> e.getText().equals(plmTypeAttributes.getCicGuiField()))
            .findFirst()
            .isPresent();
    }

    /**
     * select CI Connect field in Standard mappings rows
     *
     * @param webElement        - selected row element
     * @param plmTypeAttributes - PlmTypeAttributes enum
     */
    private void selectCiConnectField(WebElement webElement, PlmTypeAttributes plmTypeAttributes) {
        pageUtils.waitForElementAndClick(webElement);
        pageUtils.waitForElementAppear(driver.findElement(By.cssSelector("div.ss-content.ss-open> div.ss-search > input")));
        driver.findElement(By.cssSelector("div.ss-content.ss-open> div.ss-search > input")).sendKeys(plmTypeAttributes.getCicGuiField());
        this.selectValueFromDDL(0, plmTypeAttributes.getCicGuiField());
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
    }

    /**
     * Select Usage Rule
     *
     * @param webElement - selected row element
     * @param usageRule  - UsageRule
     */
    private void selectUsageRule(WebElement webElement, UsageRule usageRule) {
        pageUtils.waitForElementAndClick(webElement);
        this.selectValueFromDDL(0, usageRule.getUsageRule());
    }

    /**
     * get current row
     *
     * @return WebElement
     */
    private WebElement getCurrentRow() {
        return getStandardFieldsRows().get(getStandardFieldsRows().size() - 1);
    }
}
