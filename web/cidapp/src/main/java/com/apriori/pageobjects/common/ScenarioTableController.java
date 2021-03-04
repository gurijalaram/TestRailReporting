package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScenarioTableController extends LoadableComponent<ScenarioTableController> {

    private final Logger LOGGER = LoggerFactory.getLogger(ScenarioTableController.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    public ScenarioTableController(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
     * Opens the scenario
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return a new page object
     */
    public ScenarioTableController openScenario(String componentName, String scenarioName) {
        By scenario = By.xpath(String.format("//div[.='%s']/following-sibling::div[.='%s']/..//div[@class='scenario-thumbnail small']", componentName.toUpperCase(), scenarioName));
        pageUtils.waitForElementToAppear(scenario);
        pageUtils.scrollWithJavaScript(driver.findElement(scenario), true).click();
        return this;
    }

    /**
     * Highlights the scenario in the table
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ScenarioTableController highlightScenario(String componentName, String scenarioName) {
        By scenario = By.xpath(String.format("//div[.='%s']/following-sibling::div[.='%s']", componentName.toUpperCase(), scenarioName));
        pageUtils.waitForElementToAppear(scenario);
        pageUtils.scrollWithJavaScript(driver.findElement(scenario), true).click();
        return this;
    }

    /**
     * Checks if the component is present on the page by size > 0 or < 1
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return size of the element as int
     */
    public int getListOfScenarios(String componentName, String scenarioName) {
        By scenario = By.xpath(String.format("//div[.='%s']/following-sibling::div[.='%s']", componentName.toUpperCase(), scenarioName));
        return driver.findElements(scenario).size();
    }

    /**
     * Selects the scenario checkbox in the table
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ScenarioTableController selectScenario(String componentName, String scenarioName) {
        By scenario = By.xpath(String.format("//div[.='%s']/following-sibling::div[.='%s']/parent::div//div[@class='checkbox-icon']", componentName.toUpperCase(), scenarioName));
        pageUtils.waitForElementToAppear(scenario);
        pageUtils.scrollWithJavaScript(driver.findElement(scenario), true).click();
        return this;
    }
}
