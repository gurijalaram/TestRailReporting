package com.apriori.pageobjects.view.reports;

import com.apriori.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ComponentCostReportPage extends GenericReportPage {

    private static final Logger logger = LoggerFactory.getLogger(ComponentCostReportPage.class);

    @FindBy(xpath = "(//ul[@class='jr-mSelectlist jr'])[2]")
    private WebElement componentSelectDropdownList;

    @FindBy(xpath = "//div[@id='componentType']//a")
    private WebElement componentTypeDropdown;

    @FindBy(xpath = "//label[@title='Component Select']//a")
    private WebElement componentSelectDropdown;

    @FindBy(xpath = "//span[contains(text(), 'Lifetime Cost:')]/../following-sibling::td[1]/span")
    private WebElement lifetimeCost;

    @FindBy(xpath = "//span[contains(text(), 'Currency:')]/../following-sibling::td[1]/span")
    private WebElement currentCurrency;

    @FindBy(xpath = "//label[contains(@title, 'Latest Export Date')]/input")
    private WebElement latestExportDateInput;

    @FindBy(xpath = "//label[@title='Component Select']/span[@class='warning']")
    private WebElement warningSpan;

    @FindBy(xpath = "//table[contains(@class, 'jrPage')]/tbody/tr[3]//span")
    private WebElement componentCostReportTitle;

    @FindBy(xpath = "//span[contains(text(), 'Part Number:')]/../following-sibling::td[1]/span")
    private WebElement componentCostReportPartNumber;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ComponentCostReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets count of available Components in the dropdown
     *
     * @return String
     */
    public String getComponentListCount() {
        return componentSelectDropdownList.getAttribute("childElementCount");
    }

    /**
     * Gets count of Component Type elements
     *
     * @param componentType types to find
     * @return int
     */
    public int getCountOfComponentTypeElements(String componentType) {
        By locator = By.xpath(String.format(
                "(//ul[@class='jr-mSelectlist jr'])[2]/li[contains(@title, '[%s]')]", componentType));
        List<WebElement> elements = driver.findElements(locator);
        return elements.size();
    }

    /**
     * Selects Component Type dropdown
     *
     * @param type - type to set
     */
    public void setComponentType(String type) {
        pageUtils.waitForElementAndClick(componentTypeDropdown);
        By locator = By.xpath(String.format("//li[@title='%s']", type));
        pageUtils.waitForElementAndClick(locator);
        By typeSelectedDropdown = By.xpath(String.format("//a[@title='%s']", type));
        pageUtils.waitForElementToAppear(typeSelectedDropdown);
    }

    /**
     * Gets values from Component Select dropdown
     *
     * @return ArrayList - String
     */
    public ArrayList<String> getComponentSelectNames() {
        By locator = By.xpath("(//ul[@class='jr-mSelectlist jr'])[2]/li");
        List<WebElement> componentElements = driver.findElements(locator);
        ArrayList<String> componentNames = new ArrayList<>();
        for (WebElement element : componentElements) {
            componentNames.add(element.getAttribute("title"));
        }
        return componentNames;
    }

    /**
     * Gets lifetime cost
     *
     * @return BigDecimal
     */
    public BigDecimal getLifetimeCost() {
        pageUtils.waitForElementToAppear(lifetimeCost);
        return new BigDecimal(lifetimeCost.getText().replace(",", ""));
    }

    /**
     * Gets current currency
     *
     * @return String
     */
    public String getCurrentCurrency() {
        pageUtils.waitForElementToAppear(currentCurrency);
        return currentCurrency.getText();
    }

    /**
     * Clicks dropdown twice to remove focus from date element to effect filter
     *
     * @return instance of Component Cost Report Page
     */
    public ComponentCostReportPage clickComponentTypeDropdownTwice() {
        pageUtils.waitForElementAndClick(componentTypeDropdown);
        componentTypeDropdown.click();
        return this;
    }

    /**
     * Sets latest export date to today two years ago
     *
     * @return instance of Component cost Report Page
     */
    public ComponentCostReportPage setLatestExportDateToTodayMinusTwoYears() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(LocalDateTime.now(ZoneOffset.UTC).minusYears(2).withNano(0));
        pageUtils.waitForElementToAppear(latestExportDateInput);
        latestExportDateInput.clear();
        latestExportDateInput.click();
        latestExportDateInput.sendKeys(date);
        return this;
    }

    /**
     * Gets Component Select dropdown text
     *
     * @return String
     */
    public String getComponentSelectDropdownText() {
        pageUtils.waitForElementToAppear(componentSelectDropdown);
        return componentSelectDropdown.getText();
    }

    /**
     * Gets warning message text
     *
     * @return String
     */
    public String getWarningMessageText() {
        pageUtils.waitForElementToAppear(warningSpan);
        return warningSpan.getText();
    }

    /**
     * Checks if warning is displayed and enabled
     *
     * @return boolean
     */
    public boolean isWarningDisplayedAndEnabled() {
        return warningSpan.isDisplayed() && warningSpan.isEnabled();
    }

    /**
     * Gets Component Cost report title
     *
     * @return String
     */
    public String getComponentCostReportTitle() {
        pageUtils.waitForElementToAppear(componentCostReportTitle);
        return componentCostReportTitle.getText();
    }

    /**
     * Gets Part Number from report
     *
     * @return String
     */
    public String getPartNumber() {
        pageUtils.waitForElementToAppear(componentCostReportPartNumber);
        return componentCostReportPartNumber.getText();
    }

    /**
     * Waits for Component dropdown filter to take effect
     *
     * @return ComponentCostReportPage instance
     */
    public ComponentCostReportPage waitForComponentFilter() {
        pageUtils.waitForElementToAppear(By.xpath("//a[@title='SUB-SUB-ASM (Initial)  [assembly]']"));
        return new ComponentCostReportPage(driver);
    }

    /**
     * Waits for Component Select filter to take effect
     *
     * @param isAssembly - boolean to determine which locator to use
     */
    public void waitForComponentFilter(boolean isAssembly) {
        String dropdownTitle = isAssembly ? "SUB-ASSEMBLY (Initial)  [assembly]" : "3538968 (Initial)  [part]";
        By locator = By.xpath(String.format("//a[@title='%s']", dropdownTitle));
        pageUtils.waitForElementToAppear(locator);
    }
}
