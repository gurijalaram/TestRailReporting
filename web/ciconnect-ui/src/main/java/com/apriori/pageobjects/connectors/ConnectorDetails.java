package com.apriori.pageobjects.connectors;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.enums.ConnectorType;
import com.apriori.pageobjects.CICBasePage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class ConnectorDetails extends CICBasePage {

    @FindBy(xpath = "//div[@class='apriori-stepper']//div[.='Details']")
    private WebElement connectorDetailsTab;

    @FindBy(xpath = "//div[@tab-number='1']//button[.='Next']")
    private WebElement connectorDetailsNextBtn;

    @FindBy(xpath = "//div[@tab-number='1']//button[.='Cancel']")
    private WebElement connectorDetailsCancelBtn;

    @FindBy(css = "div.ss-content.ss-open> div.ss-search > input")
    private WebElement searchConnectorTypeTxtElement;

    @FindBy(css = "div[class='step active']")
    private WebElement activeTabElement;

    @FindBy(css = "div[class$='modalTitleBar']")
    protected WebElement connectorPopUpTitleElement;

    public ConnectorDetails(WebDriver driver) {
        super(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        pageUtils.waitForElementToAppear(connectorPopUpTitleElement);
    }

    /**
     * enter connector name
     *
     * @param connectorName The name of the connector
     */
    public ConnectorDetails enterConnectorName(String connectorName) {
        pageUtils.waitForElementToAppear(getConnectorNameElement());
        if (!getConnectorNameElement().getAttribute("value").isEmpty()) {
            pageUtils.clearValueOfElement(getConnectorNameElement());
        }
        getConnectorNameElement().sendKeys(connectorName + Keys.TAB);
        return this;
    }

    /**
     * enter connector description
     *
     * @param connectorDescription - connector description
     */
    public ConnectorDetails enterConnectorDescription(String connectorDescription) {
        pageUtils.waitForElementToAppear(getDescriptionElement());
        if (!getDescriptionElement().getAttribute("value").isEmpty()) {
            pageUtils.clearValueOfElement(getDescriptionElement());
        }
        getDescriptionElement().sendKeys(connectorDescription + Keys.TAB);
        return this;
    }

    /**
     * Click New Workflow button
     *
     * @return NewEditWorkflow page object
     */
    public ConnectorMappings clickNextBtn() {
        pageUtils.waitForElementAndClick(connectorDetailsNextBtn);
        pageUtils.waitForElementAppear(activeTabElement);
        return new ConnectorMappings(driver);
    }

    /**
     * Select connector type
     *
     * @param connectorType ConnectoryType enum
     * @return current class object
     */
    public ConnectorDetails selectType(ConnectorType connectorType) {
        pageUtils.waitForElementAndClick(getConnectorTypeDropDownElement());
        pageUtils.waitForElementToAppear(searchConnectorTypeTxtElement);
        searchConnectorTypeTxtElement.sendKeys(connectorType.getConnectorType());
        this.selectValueFromDDL(0, connectorType.getConnectorType());
        pageUtils.waitForElementsToNotAppear(By.cssSelector("div[class$='ss-open']"));
        return this;
    }

    /**
     * Check for element is displayed
     *
     * @param fieldText - text to verify
     * @return Boolean
     */
    public Boolean isLabelElementDisplayed(String fieldText) {
        return pageUtils.isElementDisplayed(By.xpath(String.format("//span[contains(text(), '%s is required')]", fieldText)));
    }

    /**
     * Get Connection Info Text area web element
     *
     * @return WebElement
     */
    public WebElement getConnectionInfoElement() {
        return driver.findElement(with(By.xpath("//textarea")).below(By.xpath("//span[.='Connection Info']")));
    }

    private WebElement getConnectorTypeDropDownElement() {
        return driver.findElement(with(By.xpath("//div[@class='widget-content widget-dropdown']")).below(By.xpath("//span[.='Type']")));
    }

    private WebElement getConnectorNameElement() {
        return driver.findElement(with(By.xpath("//input")).below(By.xpath("//span[.='Name']")));
    }

    private WebElement getDescriptionElement() {
        return driver.findElement(with(By.xpath("//textarea")).below(By.xpath("//span[.='Description']")));
    }
}
