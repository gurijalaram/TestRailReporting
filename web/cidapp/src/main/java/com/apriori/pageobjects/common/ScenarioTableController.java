package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

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
     * @param componentName - component name
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
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ScenarioTableController highlightScenario(String componentName, String scenarioName) {
        findScenario(componentName, scenarioName).click();
        return this;
    }

    /**
     * Checks if the component is present on the page by size > 0 or < 1
     *
     * @param componentName - component name
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
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ScenarioTableController selectScenario(String componentName, String scenarioName) {
        findScenario(componentName, scenarioName).click();
        return this;
    }

    /**
     * Highlights the scenario in the table using the keyboard control key
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ScenarioTableController controlHighlightScenario(String componentName, String scenarioName) {
        Actions controlHighlight = new Actions(driver);
        controlHighlight.moveToElement(findScenario(componentName, scenarioName)).sendKeys(Keys.CONTROL).click().build().perform();
        return this;
    }

    /**
     * Multi-highlight scenario
     * This method takes any number of arguments as string. A combination of component and scenario name needs to passed in the argument eg. {"PISTON, Initial", "Television, AutoScenario101"}
     *
     * @param componentAndScenarioName - component name and method name
     * @return current page object
     */
    public ScenarioTableController multiHighlightScenario(String... componentAndScenarioName) {
        Actions multiHighlight = new Actions(driver);

        Arrays.stream(componentAndScenarioName).map(csn -> csn.split(",")).collect(Collectors.toList())
            .forEach(componentScenario -> multiHighlight.keyDown(Keys.CONTROL)
                .click(findScenario(componentScenario[0].trim(), componentScenario[1].trim()))
                .build()
                .perform());
        return this;
    }

    /**
     * Multi-select scenario
     * This method takes any number of arguments as string. A combination of component and scenario name needs to passed in the argument eg. {"PISTON, Initial", "Television, AutoScenario101"}
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public ScenarioTableController multiSelectScenario(String... componentScenarioName) {
        Arrays.stream(componentScenarioName).forEach(componentScenario -> {
            String[] scenario = componentScenario.split(",");
            findScenarioCheckbox(scenario[0].trim(), scenario[1].trim()).click();
        });
        return this;
    }

    /**
     * Find scenario checkbox
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return webelement
     */
    private WebElement findScenarioCheckbox(String componentName, String scenarioName) {
        By scenario = By.xpath(String.format("//div[.='%s']/following-sibling::div[.='%s']/parent::div//div[@class='checkbox-icon']", componentName.toUpperCase(), scenarioName));
        pageUtils.waitForElementToAppear(scenario);
        return pageUtils.scrollWithJavaScript(driver.findElement(scenario), true);
    }

    /**
     * Private method to find the scenario
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return webelement
     */
    private WebElement findScenario(String componentName, String scenarioName) {
        By scenario = By.xpath(String.format("//div[.='%s']/following-sibling::div[.='%s']", componentName.toUpperCase(), scenarioName));
        pageUtils.waitForElementToAppear(scenario);
        return pageUtils.scrollWithJavaScript(driver.findElement(scenario), true);
    }
}
