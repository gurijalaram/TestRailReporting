package com.apriori.pageobjects.common;

import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Arrays;

@Slf4j
public class CisScenarioTableController extends EagerPageComponent<CisScenarioTableController> {

    @FindBy(css = ".apriori-table.scenario-iteration-table")
    private WebElement componentTable;

    @FindBy(xpath = "(//div[@class='table-body']/div)[1]//div[@class='scenario-thumbnail small']")
    private WebElement scenarioLocator;

    public CisScenarioTableController(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(componentTable);

    }

    /**
     * Find scenario checkbox
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return webElement
     */
    private WebElement findScenarioCheckbox(String componentName, String scenarioName) {
        By scenario = By.xpath(String.format("//span[contains(text(),'%s')]/ancestor::div[@role='row']//div[.='%s']/ancestor::div[@role='row']//div[@class='checkbox-icon']",
            componentName.toUpperCase().trim(), scenarioName.trim()));
        getPageUtils().waitForElementToAppear(scenario);
        return getPageUtils().scrollWithJavaScript(getDriver().findElement(scenario), true);
    }

    /**
     * Selects the scenario by checkbox
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public CisScenarioTableController selectScenario(String componentName, String scenarioName) {
        findScenarioCheckbox(componentName, scenarioName).click();
        return this;
    }

    /**
     * Multi-select scenario
     * This method takes any number of arguments as string. A combination of component and scenario name needs to passed in the argument eg. {"PISTON, Initial", "Television, AutoScenario101"}
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public CisScenarioTableController multiSelectScenario(String... componentScenarioName) {
        Arrays.stream(componentScenarioName).forEach(componentScenario -> {
            String[] scenario = componentScenario.split(",");
            findScenarioCheckbox(scenario[0], scenario[1]).click();
        });
        return this;
    }

    /**
     * Opens the first scenario
     *
     * @return current page object
     */
    public CisScenarioTableController openFirstScenario() {
        getPageUtils().waitForElementAndClick(scenarioLocator);
        return this;
    }

    /**
     * Gets the scenario 'webelement' locator
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return webElement
     */
    private WebElement elementScenarioName(String componentName, String scenarioName) {
        return getPageUtils().waitForElementToAppear(byScenarioName(componentName, scenarioName));
    }

    /**
     * Gets the scenario 'by' locator
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return by
     */
    private By byScenarioName(String componentName, String scenarioName) {
        By byScenario = By.xpath(String.format("//span[contains(text(),'%s')]/ancestor::div[@role='row']//a[@class]//div[.='%s']",
            componentName.toUpperCase().trim(),
            scenarioName.trim()));
        return byScenario;
    }

    /**
     * Finds the scenario by component name
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return by
     */
    private By byComponentName(String componentName, String scenarioName) {
        return By.xpath(String.format("//div[.='%s']/ancestor::div[@role='row']//span[contains(text(),'%s')]",
            scenarioName.trim(),
            componentName.toUpperCase().trim()));
    }

    /**
     * Hovers over the scenario
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    private CisScenarioTableController moveToScenario(String componentName, String scenarioName) {
        getPageUtils().scrollWithJavaScript(elementScenarioName(componentName, scenarioName), true);
        getPageUtils().mouseMove(elementScenarioName(componentName, scenarioName));
        return this;
    }

    /**
     * Highlights the scenario in the table
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public CisScenarioTableController highlightScenario(String componentName, String scenarioName) {
        moveToScenario(componentName, scenarioName);
        getPageUtils().waitForElementAndClick(byComponentName(componentName, scenarioName));
        return this;
    }
}
