package com.apriori.pageobjects.connectors;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.enums.ConnectorType;
import com.apriori.pageobjects.CICBasePage;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.function.Function;

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

    @FindBy(xpath = "//span[.='Name']")
    private WebElement nameLblElement;

    @FindBy(css = "div[id$='popup_label-151'] span")
    private WebElement nameLblErrorMsgElement;

    @FindBy(css = "div[id$='popup_label-223'] span")
    private WebElement descriptionLblErrorMsgElement;

    @FindBy(css = "div[id$='popup_label-220'] span")
    private WebElement typeLblErrorMsgElement;

    @FindBy(css = "div[class='step active']")
    private WebElement activeTabElement;

    @FindBy(css = "div[class$='modalTitleBar']")
    protected WebElement connectorPopUpTitleElement;

    public ConnectorDetails(WebDriver driver) {
        super(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        pageUtils.waitForElementToAppear(nameLblElement);
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

    public String getConnectorName() {
        pageUtils.waitForElementToAppear(getConnectorNameElement());
        return getConnectorNameElement().getAttribute("value");
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
     * Click Next button
     *
     * @return ConnectorMappings page object
     */
    public ConnectorMappings clickNextBtn() {
        pageUtils.waitForElementAndClick(connectorDetailsNextBtn);
        pageUtils.waitForElementAppear(activeTabElement);
        return new ConnectorMappings(driver);
    }

    /**
     * Click Cancel button
     *
     * @return ConnectorPage page object
     */
    public ConnectorsPage clickCancelBtn() {
        pageUtils.waitForElementAndClick(connectorDetailsCancelBtn);
        return new ConnectorsPage(driver);
    }

    /**
     * get Next button
     *
     * @return WebElement
     */
    @SneakyThrows
    public Boolean isNextBtnEnabled(Boolean expectedStatus) {
        pageUtils.waitForElementAppear(connectorDetailsNextBtn);
        FluentWait wait = new FluentWait<WebDriver>(driver)
            .pollingEvery(Duration.ofMillis(200))
            .withTimeout(Duration.ofSeconds(5))
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class);

        Function<WebDriver, Boolean> notEnabled = new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                return (connectorDetailsNextBtn.isEnabled() == expectedStatus);
            }
        };
        return (Boolean) wait.until(notEnabled);
    }

    /**
     * Select connector type
     *
     * @param connectorType ConnectoryType enum
     * @return current class object
     */
    public ConnectorDetails selectType(ConnectorType connectorType) {
        pageUtils.waitForElementAndClick(getConnectorTypeDropDownElement());
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
     * get mandatory validation message for Name field
     *
     * @return String
     */
    public String getNameErrorMsg() {
        pageUtils.waitForElementAppear(nameLblErrorMsgElement);
        return nameLblErrorMsgElement.getText();
    }

    /**
     * get mandatory validation message for type field
     *
     * @return String
     */
    public String getTypeErrorMsg() {
        pageUtils.waitForElementAppear(nameLblErrorMsgElement);
        return nameLblErrorMsgElement.getText();
    }

    /**
     * get mandatory validation message for Name field
     *
     * @return String
     */
    public String getDescriptionErrorMsg() {
        pageUtils.waitForElementAppear(descriptionLblErrorMsgElement);
        return descriptionLblErrorMsgElement.getText();
    }

    /**
     * Get Connection Info Text area web element
     *
     * @return WebElement
     */
    public WebElement getConnectionInfoElement() {
        return driver.findElement(with(By.xpath("//textarea")).below(By.xpath("//span[.='Connection Info']")));
    }

    /**
     * get webelement for connector type
     *
     * @return WebElement
     */
    private WebElement getConnectorTypeDropDownElement() {
        return driver.findElement(with(By.xpath("//div[@class='widget-content widget-dropdown']")).below(By.xpath("//span[.='Type']")));
    }

    /**
     * get webelement for connector Name
     *
     * @return WebElement
     */
    private WebElement getConnectorNameElement() {
        return driver.findElement(with(By.xpath("//input")).below(By.xpath("//span[.='Name']")));
    }

    /**
     * get webelement for connector Description
     *
     * @return WebElement
     */
    private WebElement getDescriptionElement() {
        return driver.findElement(with(By.xpath("//textarea")).below(By.xpath("//span[.='Description']")));
    }
}
