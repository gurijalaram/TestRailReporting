package com.apriori.pageobjects.pages.view.reports;

import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.reports.ListNameEnum;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class ScenarioComparisonReportPage extends GenericReportPage {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioComparisonReportPage.class);

    @FindBy(xpath = "//span[contains(text(), 'FULLY')]/../following-sibling::td[2]")
    private WebElement firstFbc;

    @FindBy(xpath = "//span[contains(text(), 'FULLY')]/../following-sibling::td[3]")
    private WebElement secondFbc;

    @FindBy(css = "div[id='scenarioToCompareIDs'] > div > div > div > div > div:nth-of-type(1) > span")
    private WebElement availableScenariosToCompareTab;

    @FindBy(css = "div[id='scenarioToCompareIDs'] > div > div > div > div > div:nth-of-type(2) > span")
    private WebElement selectedScenariosToCompareTab;

    @FindBy(xpath = "//div[@title='Scenarios to Compare']//li[@title='Select All']/a")
    private WebElement selectAllScenariosToCompare;

    @FindBy(xpath = "//div[@title='Scenarios to Compare']//li[@title='Deselect All']/a")
    private WebElement deselectAllScenariosToCompare;

    @FindBy(xpath = "//div[@title='Scenarios to Compare']//li[@title='Invert']/a")
    private WebElement invertScenariosToCompare;

    @FindBy(xpath = "(//div[contains(text(), 'No Items Selected')])[6]")
    private WebElement noItemsAvailable;

    @FindBy(xpath = "(//div[@title='Scenarios to Compare']//ul)[1]/li[1]")
    private WebElement firstScenarioToCompare;

    @FindBy(xpath = "//div[@id='partNumber']//input")
    private WebElement partNumberSearchCriteriaInput;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ScenarioComparisonReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Get first or second Fbc Value
     * @param getFirst boolean
     * @return BigDecimal
     */
    public BigDecimal getFbcValue(boolean getFirst) {
        WebElement elementToUse = getFirst ? firstFbc : secondFbc;
        pageUtils.waitForElementToAppear(elementToUse);
        return new BigDecimal(elementToUse.getText());
    }

    /**
     * Gets Scenarios to Compare Name
     * @param index int
     * @return String
     */
    public String getScenariosToCompareName(int index) {
        By locator = By.xpath(String.format("(//div[@title='Scenarios to Compare']//ul)[1]/li[%s]", index));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getAttribute("title");
    }

    /**
     * Waits for scenario filter to take effect
     */
    public void waitForScenarioFilter() {
        for (int i = 1; i < 21; i++) {
            By locator = By.xpath(
                    String.format(
                            "((//div[@title='Scenarios to Compare']//ul)[1]/li[contains(@title, 'Initial')])[%s]", i)
            );
            pageUtils.waitForElementToAppear(locator);
        }

        By filteredLocator = By.xpath("((//div[@title='Scenarios to Compare']//ul)[1]/li[contains(@title, '(Initial)')])[1]");
        pageUtils.waitForElementToAppear(filteredLocator);

        GenericReportPage genericReportPage = new GenericReportPage(driver);
        genericReportPage.waitForCorrectAvailableSelectedCount(ListNameEnum.SCENARIOS_TO_COMPARE.getListName(), "Available: ", "1,247");
    }

    /**
     * Selects all scenarios to compare
     */
    public void selectAllScenariosToCompare() {
        pageUtils.waitForElementAndClick(selectAllScenariosToCompare);
        String expectedCount = getCountOfAvailableScenariosToCompare();
        waitForCorrectAvailableSelectedCount(
                ListNameEnum.SCENARIOS_TO_COMPARE.getListName(), "Selected: ", expectedCount);
    }

    /**
     * Deselects all scenarios to compare
     */
    public void deselectAllScenariosToCompare() {
        pageUtils.waitForElementAndClick(deselectAllScenariosToCompare);
        waitForCorrectAvailableSelectedCount(
                ListNameEnum.SCENARIOS_TO_COMPARE.getListName(), "Selected: ", "0");
    }

    /**
     * Inverts scenarios to compare
     */
    public void invertScenariosToCompare() {
        pageUtils.waitForElementAndClick(invertScenariosToCompare);
        waitForCorrectAvailableSelectedCount(
                ListNameEnum.SCENARIOS_TO_COMPARE.getListName(),
                "Selected: ",
                getCountOfAvailableScenariosToCompare()
        );
    }

    /**
     * Checks if no items available text is displayed
     * @return boolean - displayed && enabled
     */
    public boolean isNoItemsAvailableTextDisplayed() {
        pageUtils.waitForElementToAppear(noItemsAvailable);
        return noItemsAvailable.isDisplayed() && noItemsAvailable.isEnabled();
    }

    /**
     * Gets available scenarios to compare count
     * @return String
     */
    public String getCountOfAvailableScenariosToCompare() {
        return availableScenariosToCompareTab.getAttribute("title").substring(11);
    }

    /**
     * Gets selected scenarios to compare count
     * @return String
     */
    public String getCountOfSelectedScenariosToCompare() {
        return selectedScenariosToCompareTab.getAttribute("title").substring(10);
    }

    /**
     * Selects first scenario to compare
     */
    public void selectFirstScenarioToCompare() {
        By locator = By.xpath("(//div[@id='scenarioToCompareIDs']//ul)[1]/li[1]/div/a");
        pageUtils.waitForElementAndClick(locator);
    }

    /**
     * Clicks selected tab then x beside selected scenario to compare
     */
    public void clickSelectedTabAndThenX() {
        pageUtils.waitForElementAndClick(selectedScenariosToCompareTab);
        By locator = By.xpath("(//a[@class='jr-mSelectlist-item-delete jr'])[2]");
        pageUtils.waitForElementAndClick(locator);
        waitForCorrectAvailableSelectedCount(
                ListNameEnum.SCENARIOS_TO_COMPARE.getListName(), "Selected: ", "0");
    }

    /**
     * Waits for Scenario to Compare filter to take effect
     */
    public void waitForScenarioToCompareFilter() {
        for (int i = 1; i < 11; i++) {
            By locator = By.xpath(String.format(
                    "(//div[@title='Scenarios to Compare']//ul)[1]/li[contains(@title, '(Initial)')][%s]", i));
            pageUtils.waitForElementToAppear(locator);
        }
    }

    /**
     * Gets name of first scenario to compare
     */
    public String getNameOfFirstScenarioToCompare(boolean substringName) {
        By locator = By.xpath("(//div[@title='Scenarios to Compare']//ul)[1]/li[@title='0200613 (Initial) [part]']");
        pageUtils.waitForElementToAppear(firstScenarioToCompare);
        String scenarioName = "";
        while (scenarioName.isEmpty()) {
            if (firstScenarioToCompare.getAttribute("title").contains("0200613")) {
                scenarioName = firstScenarioToCompare.getAttribute("title");
            }
            pageUtils.waitForElementToAppear(locator);
        }
        return substringName ? scenarioName.substring(0, 7) : scenarioName;
    }

    /**
     * Gets first scenario name (generic)
     * @return String
     */
    public String getFirstScenarioName() {
        return firstScenarioToCompare.getAttribute("title");
    }

    /**
     * Inputs part number search criteria
     * @param partNumberToInput String
     */
    public void inputPartNumberSearchCriteria(String partNumberToInput) {
        pageUtils.waitForElementAndClick(partNumberSearchCriteriaInput);
        pageUtils.clearInput(partNumberSearchCriteriaInput);
        partNumberSearchCriteriaInput.sendKeys(partNumberToInput);
        partNumberSearchCriteriaInput.sendKeys(Keys.ENTER);
        waitForCorrectAvailableSelectedCount(
                ListNameEnum.SCENARIOS_TO_COMPARE.getListName(), "Available: ", "2");
    }
}
