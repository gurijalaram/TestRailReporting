package com.apriori.pageobjects.connectors;

import com.apriori.cic.enums.PlmTypeAttributes;
import com.apriori.enums.UsageRule;

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

    @FindBy(xpath = "//div[@tab-number='1']//button//span[text()='Add Row']")
    private WebElement addRowBtn;

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
        WebElement matchedElement = getStandardFieldsRows().stream().map(webElement -> webElement.findElements(By.cssSelector("div[class*='cic-input']"))
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
            ciConnectFieldColElements = webElement.findElements(By.cssSelector("div[class*='cic-input']"));
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
        List<WebElement> ciStandardFieldFieldCols = getStandardFieldsRows().get(getStandardFieldsRows().size() - 1).findElements(By.cssSelector("div[class*='cic-input']"));
        selectCiConnectField(ciStandardFieldFieldCols.get(0), plmTypeAttributes);
        selectUsageRule(ciStandardFieldFieldCols.get(1), usageRule);
        pageUtils.clearValueOfElement(ciStandardFieldFieldCols.get(2).findElement(By.cssSelector("input[type='text']")));
        ciStandardFieldFieldCols.get(2).findElement(By.cssSelector("input[type='text']")).sendKeys(plmTypeAttributes.getValue());
        pageUtils.clearValueOfElement(ciStandardFieldFieldCols.get(2).findElement(By.cssSelector("input[type='text']")));
        ciStandardFieldFieldCols.get(2).findElement(By.cssSelector("input[type='text']")).sendKeys(plmTypeAttributes.getValue());
        return this;
    }

    /**
     * Enter the value for standard mappings for pre- loaded mappings.
     *
     * @param plmTypeAttributes PlmTypeAttributes enum
     * @return Current class Object
     */
    public StandardFields enterPlmField(PlmTypeAttributes plmTypeAttributes) {
        pageUtils.clearValueOfElement(getMatchedConnectFieldRow(plmTypeAttributes).get(2).findElement(By.cssSelector("input[type='text']")));
        getMatchedConnectFieldRow(plmTypeAttributes).get(2).findElement(By.cssSelector("input[type='text']")).sendKeys(plmTypeAttributes.getValue());
        return this;
    }

    /**
     * click add row button in standard mappings tab
     */
    private void clickAddRowBtn() {
        pageUtils.waitForElementAndClick(addRowBtn);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
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
    private List<WebElement> getStandardFieldsRows() {
        return driver.findElements(By.cssSelector("div[tab-number='1'] div[class^='BMCollectionViewCellWrapper'] div[class$='tw-flex-row']"));
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
}
