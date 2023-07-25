package com.apriori.pageobjects.common;

import com.apriori.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class PartsAndAssemblyDetailsController extends EagerPageComponent<PartsAndAssemblyDetailsController> {

    @FindBy(xpath = "//div[@data-testid='Process']")
    private WebElement processList;

    @FindBy(xpath = "//div[@role='row']")
    private WebElement tableHeaders;

    @FindBy(xpath = "//div[@data-testid='Issues']")
    private WebElement issueList;

    @FindBy(xpath = "//div[@data-testid='Investigation']")
    private WebElement topicsList;

    @FindBy(xpath = "//div[@data-testid='Threads']")
    private WebElement threadsItemsList;

    @FindBy(xpath = "//ul[@role='listbox']")
    private WebElement attributeList;

    public PartsAndAssemblyDetailsController(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
    }

    /**
     * Gets scenario result card fields
     *
     * @return list of string
     */

    public List<String> getScenarioResultCardFields(String cardName) {
        return Stream.of(getDriver().findElement(By.xpath("//h2[text()='" + cardName + "']//following::div[starts-with(@class,'MuiCollapse-root MuiCollapse-vertical')]")).getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets items on each detail section
     *
     * @return list of string
     */
    public List<String> getItemsOfSections(String section) {
        return Stream.of(getDriver().findElement(By.xpath("//div[@data-testid='" + section + "']")).getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets process routing details
     *
     * @return list of string
     */
    public List<String> getProcessRoutingDetails() {
        return Stream.of(processList.getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets table headers
     *
     * @return list of string
     */
    public List<String> getTableHeaders() {
        return Stream.of(tableHeaders.getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets design guidance details
     *
     * @return list of string
     */
    public List<String> getDesignGuidanceDetails() {
        return Stream.of(issueList.getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets investigation topics
     *
     * @return list of string
     */
    public List<String> getInvestigationDetails() {
        return Stream.of(topicsList.getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets threads items details
     *
     * @return list of string
     */
    public List<String> getThreadsDetails() {
        return Stream.of(threadsItemsList.getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets attribute list
     *
     * @return list of string
     */
    public List<String> getAttributeList() {
        return Stream.of(attributeList.getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }
}