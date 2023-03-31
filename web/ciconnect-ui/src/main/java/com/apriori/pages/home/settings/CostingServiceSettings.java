package com.apriori.pages.home.settings;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.pages.CICBasePage;
import com.apriori.pages.home.CIConnectHome;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class CostingServiceSettings extends CICBasePage {

    @FindBy(css = "div[class^='ss-content ss-'][class$='ss-open'] div[class='ss-search'] > input")
    private WebElement searchTextField;

    @FindBy(css = "[selected-value=manual]")
    private WebElement batchSizeManualButton;

    @FindBy(css = "[selected-value=automatic]")
    private WebElement batchSizeAutomaticButton;

    @FindBy(xpath = "//button[.='Save']")
    private WebElement settingsSaveButton;

    public CostingServiceSettings(WebDriver driver) {
        super(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        pageUtils.waitForElementToBeClickable(settingsSaveButton);
    }

    /**
     * Get Costing Service Settings modal title text
     *
     * @return String
     */
    public String getCostingServiceSettingsText() {
        return getCostingServiceSettingsLabelElement().getText();
    }

    /**
     * Select process group
     *
     * @param processGroup
     */
    public CostingServiceSettings selectProcessGroup(String processGroup) {
        if (!getProcessGroup().equals(processGroup)) {
            pageUtils.waitForElementAndClick(getProcessGroupDropDownElement());
            pageUtils.waitForElementToAppear(searchTextField);
            searchTextField.sendKeys(processGroup);
            try {
                this.selectValueFromDDL(0, processGroup);
            } catch (StaleElementReferenceException e) {
                searchTextField.sendKeys(processGroup);
                this.selectValueFromDDL(0, processGroup);
            }
        }
        return this;
    }

    /**
     * Select digital factory
     *
     * @param digitalFactory
     */
    public CostingServiceSettings selectDigitalFactory(String digitalFactory) {
        if (!getDigitalFactory().equals(digitalFactory)) {
            pageUtils.waitForElementAndClick(getDigitalFactoryDropDownElement());
            pageUtils.waitForElementToAppear(searchTextField);
            searchTextField.sendKeys(digitalFactory);
            try {
                this.selectValueFromDDL(0, digitalFactory);
            } catch (StaleElementReferenceException e) {
                searchTextField.sendKeys(digitalFactory);
                this.selectValueFromDDL(0, digitalFactory);
            }
        }
        return this;
    }

    /**
     * Enter batch size
     *
     * @param batchSize
     */
    public CostingServiceSettings enterBatchSize(String batchSize) {
        pageUtils.waitForElementAndClick(batchSizeManualButton);
        pageUtils.clearValueOfElement(getBatchSizeElement());
        getBatchSizeElement().sendKeys(batchSize + Keys.TAB);
        return this;
    }

    /**
     * Enter annual volume
     *
     * @param annualVolume
     */
    public CostingServiceSettings enterAnnualVolume(String annualVolume) {
        pageUtils.clearValueOfElement(getAnnualVolumeElement());
        getAnnualVolumeElement().sendKeys(annualVolume + Keys.TAB);
        return this;
    }

    /**
     * Enter production life value
     *
     * @param productionLife
     */
    public CostingServiceSettings enterProductionLife(String productionLife) {
        pageUtils.clearValueOfElement(getProductionLifeElement());
        getProductionLifeElement().sendKeys(productionLife + Keys.TAB);
        return this;
    }

    /**
     * Enter scenario Name
     *
     * @param scenarioName
     */
    public CostingServiceSettings enterScenarioName(String scenarioName) {
        pageUtils.clearValueOfElement(getScenarioNameElement());
        getScenarioNameElement().sendKeys(scenarioName + Keys.TAB);
        return this;
    }

    /**
     * click save button in costing service settings pop up page
     *
     * @return WorkflowHome
     */
    public CIConnectHome clickSaveButton() {
        pageUtils.waitForElementAndClick(settingsSaveButton);
        return new CIConnectHome(driver);
    }

    /**
     * click save button in costing service settings pop up page
     *
     * @return WorkflowHome
     */
    public CIConnectHome clickCancelButton() {
        pageUtils.waitForElementAndClick(getCancelButtonElement());
        return new CIConnectHome(driver);
    }

    /**
     * get entered scenario name
     *
     * @return string
     */
    public String getScenarioName() {
        return getScenarioNameElement().getAttribute("value");
    }

    /**
     * get Process Group from settings page
     *
     * @return string
     */
    public String getProcessGroup() {
        return getProcessGroupDropDownElement().getText();
    }

    /**
     * Get digital factory from settings page
     *
     * @return string
     */
    public String getDigitalFactory() {
        return getDigitalFactoryDropDownElement().getText();
    }

    /**
     * Get annual volume from settings pagae
     *
     * @return String
     */
    public String getAnnualVolume() {
        return getAnnualVolumeElement().getAttribute("value");
    }

    /**
     * Get batch Size from settings page
     *
     * @return string
     */
    public String getBatchSize() {
        return getBatchSizeElement().getAttribute("value");
    }

    /**
     * Get production life from settings page
     *
     * @return string
     */
    public String getProductionLife() {
        return getProductionLifeElement().getAttribute("value");
    }

    private WebElement getScenarioNameElement() {
        return driver.findElement(with(By.xpath("//input")).below(By.xpath("//span[.='Scenario Name']")));
    }

    private WebElement getAnnualVolumeElement() {
        return driver.findElement(with(By.xpath("//input")).below(By.xpath("//span[.='Annual Volume']")));
    }

    private WebElement getProductionLifeElement() {
        return driver.findElement(with(By.xpath("//input")).below(By.xpath("//span[.='Production Life']")));
    }

    private WebElement getBatchSizeElement() {
        return driver.findElement(with(By.xpath("//input")).below(By.xpath("//span[.='Batch Size']")));
    }

    private WebElement getProcessGroupDropDownElement() {
        return driver.findElement(with(By.xpath("//div")).below(By.xpath("//span[.='Process Group']")));
    }

    private WebElement getDigitalFactoryDropDownElement() {
        return driver.findElement(with(By.xpath("//div")).below(By.xpath("//span[.='Digital Factory']")));
    }

    private WebElement getCostingServiceSettingsLabelElement() {
        return driver.findElement(By.xpath("//span[.='Costing Service Settings']"));
    }

    private WebElement getCancelButtonElement() {
        return driver.findElement(with(By.xpath("//button[.='Cancel']")).toLeftOf(By.xpath("//button[.='Save']")));
    }

}
