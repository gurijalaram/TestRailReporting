package com.apriori.cis.ui.common;

import com.apriori.cis.ui.utils.CisGuiTestUtil;
import com.apriori.web.app.util.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

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
        return CisGuiTestUtil.getAttributeDetails(getDriver().findElement(By.xpath("//h2[text()='" + cardName + "']//following::div[starts-with(@class,'MuiCollapse-root MuiCollapse-vertical')]")));
    }

    /**
     * Gets items on each detail section
     *
     * @return list of string
     */
    public List<String> getItemsOfSections(String section) {
        return CisGuiTestUtil.getAttributeDetails(getDriver().findElement(By.xpath("//div[@data-testid='" + section + "']")));
    }

    /**
     * Gets process routing details
     *
     * @return list of string
     */
    public List<String> getProcessRoutingDetails() {
        return CisGuiTestUtil.getAttributeDetails(processList);
    }

    /**
     * Gets table headers
     *
     * @return list of string
     */
    public List<String> getTableHeaders() {
        return CisGuiTestUtil.getAttributeDetails(tableHeaders);
    }

    /**
     * Gets design guidance details
     *
     * @return list of string
     */
    public List<String> getDesignGuidanceDetails() {
        return CisGuiTestUtil.getAttributeDetails(issueList);
    }

    /**
     * Gets investigation topics
     *
     * @return list of string
     */
    public List<String> getInvestigationDetails() {
        return CisGuiTestUtil.getAttributeDetails(topicsList);
    }

    /**
     * Gets threads items details
     *
     * @return list of string
     */
    public List<String> getThreadsDetails() {
        return CisGuiTestUtil.getAttributeDetails(threadsItemsList);
    }

    /**
     * Gets attribute list
     *
     * @return list of string
     */
    public List<String> getAttributeList() {
        return CisGuiTestUtil.getAttributeDetails(attributeList);
    }
}