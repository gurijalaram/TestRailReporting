package com.apriori.pageobjects.pages.view.reports;

import com.apriori.utils.PageUtils;

import com.apriori.utils.enums.reports.ListNameEnum;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ComponentCostReportPage extends GenericReportPage {

    private final Logger logger = LoggerFactory.getLogger(ComponentCostReportPage.class);

    @FindBy(xpath = "(//ul[@class='jr-mSelectlist jr'])[2]")
    private WebElement componentSelectDropdownList;

    @FindBy(xpath = "//div[@id='componentType']//a")
    private WebElement componentTypeDropdown;

    @FindBy(xpath = "//span[contains(text(), 'Lifetime Cost:')]/../following-sibling::td[1]/span")
    private WebElement lifetimeCost;

    @FindBy(xpath = "//span[contains(text(), 'Currency:')]/../following-sibling::td[1]/span")
    private WebElement currentCurrency;

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
     * @return String
     */
    public String getComponentListCount() {
        return componentSelectDropdownList.getAttribute("childElementCount");
    }

    /**
     * Gets count of Component Type elements
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
     * @param type - type to set
     * @return Instance of Component Cost Report Page
     */
    public ComponentCostReportPage setComponentType(String type) {
        pageUtils.waitForElementAndClick(componentTypeDropdown);
        By locator = By.xpath(String.format("//li[@title='%s']", type));
        pageUtils.waitForElementAndClick(locator);
        By typeSelectedDropdown = By.xpath(String.format("//a[@title='%s']", type));
        pageUtils.waitForElementToAppear(typeSelectedDropdown);
        return this;
    }

    /**
     * Gets values from Component Select dropdown
     * @return ArrayList<String>
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
     * Waits for Component Select filter to take effect
     */
    public void waitForComponentFilter(boolean isAssembly) {
        String dropdownTitle = isAssembly ? "SUB-ASSEMBLY (Initial)  [assembly]" : "3538968 (Initial)  [part]";
        By locator = By.xpath(String.format("//a[@title='%s']", dropdownTitle));
        pageUtils.waitForElementToAppear(locator);
    }

    /**
     * Gets lifetime cost
     * @return BigDecimal
     */
    public BigDecimal getLifetimeCost() {
        pageUtils.waitForElementToAppear(lifetimeCost);
        return new BigDecimal(lifetimeCost.getText().replace(",", ""));
    }

    /**
     * Gets current currency
     * @return String
     */
    public String getCurrentCurrency() {
        pageUtils.waitForElementToAppear(currentCurrency);
        return currentCurrency.getText();
    }
}
