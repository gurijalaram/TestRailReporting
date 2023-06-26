package com.apriori.pageobjects.common;

import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ProjectPartsAndAssemblyTableController extends EagerPageComponent<ProjectPartsAndAssemblyTableController> {

    @FindBy(css = "div.MuiDataGrid-columnHeaders.css-qw65j7")
    private WebElement tableHeaders;

    @FindBy(css = "div.MuiDataGrid-pinnedColumnHeaders")
    private WebElement pinnedTableHeaders;

    public ProjectPartsAndAssemblyTableController(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
    }

    /**
     * Gets the scenario 'by' locator
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return by
     */
    private By byScenarioName(String componentName, String scenarioName) {
        By byScenario = By.xpath(String.format("//div[@data-field='scenarioName']//p[text()='%s']/ancestor::div[@role='row']//div[@data-field='componentName']//p[text()='%s']", scenarioName.trim(), componentName.trim()));
        return byScenario;
    }

    /**
     * Checks if the component is present on the page by size == 0 or > 0
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return size of the element as int
     */
    public int getListOfScenarios(String componentName, String scenarioName) {
        return getPageUtils().waitForElementsToAppear(byScenarioName(componentName, scenarioName)).size();
    }
}