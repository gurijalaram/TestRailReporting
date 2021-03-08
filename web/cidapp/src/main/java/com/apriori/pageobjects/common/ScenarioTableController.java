package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return a new page object
     */
    public ScenarioTableController openScenario(String componentName, String scenarioName) {
        By scenario = By.xpath(String.format("//div[.='%s']/following-sibling::div[.='%s']/..//div[@class='scenario-thumbnail small']", componentName.toUpperCase().trim(), scenarioName.trim()));
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
        By scenario = By.xpath(String.format("//div[.='%s']/following-sibling::div[.='%s']", componentName.toUpperCase().trim(), scenarioName.trim()));
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
        By scenario = By.xpath(String.format("//div[.='%s']/following-sibling::div[.='%s']", componentName.toUpperCase().trim(), scenarioName.trim()));
        return driver.findElements(scenario).size();
    }

    public List<String> getRowText(String componentName, String scenarioName) {
        return driver.findElement(By.xpath(String.format("//div[.='%s']/following-sibling::div[.='%s']/parent::div", componentName.toUpperCase().trim(), scenarioName.trim())))
            .findElements(By.cssSelector("[class='cell-text']"))
            .stream()
            .map(attr -> attr.getAttribute("textContent"))
            .filter(text -> !text.equals(""))
            .collect(Collectors.toList());
    }

    public List<String> getRowIcon(String componentName, String scenarioName) {
        return driver.findElement(By.xpath(String.format("//div[.='%s']/following-sibling::div[.='%s']/parent::div", componentName.toUpperCase().trim(), scenarioName.trim())))
            .findElements(By.cssSelector("svg"))
            .stream()
            .map(icon -> icon.getAttribute("data-icon"))
            .collect(Collectors.toList());
    }
}
