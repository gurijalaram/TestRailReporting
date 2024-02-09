package com.apriori.cic.ui.pageobjects;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.cic.ui.pagedata.WorkFlowData;
import com.apriori.cic.ui.utils.TableUtils;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class CICBasePage extends LoadableComponent<CICBasePage> {

    protected static final String OPTIONS_CONTENT_OPEN_DROPDOWN_CSS = "div[class^='ss-content ss-'][class$='ss-open'] div[class='ss-list']";
    protected static final String PARENT_ELEMENT_CSS = "div[id^='root_pagemashupcontainer-1_navigation-']";
    protected static final long WAIT_TIME = 30;
    protected static final long DEFAULT_WAIT_TIME = 3;
    protected static WorkFlowData workFlowData;

    protected WebDriver driver;
    protected PageUtils pageUtils;
    protected TableUtils tableUtils;

    @FindBy(css = "div[class='apriori-stepper'] > div[class='step active'] > div[class='number']")
    protected WebElement workflowPopUpActiveTabElement;

    @FindBy(xpath = "//span[text()='Edit Workflow']")
    protected WebElement popUpEditWorkflowLbl;

    @FindBy(xpath = "//div[contains(@class, 'widget-checkbox')]//span[.='Return only the latest revision of each part from the PLM system']")
    protected WebElement revisionLatestCheckBoxLblElement;

    @FindBy(css = "div[class$='modalTitle']")
    protected WebElement workflowPopUpTitleElement;

    @FindBy(css = "input[name='email']")
    protected WebElement emailInputCloud;

    @FindBy(css = "span[title='Users']")
    protected WebElement usersMenuBtn;

    @FindBy(css = "div.tw-status-msg-box")
    protected WebElement statusMessagePopUpElement;

    @FindBy(css = "div.tw-status-msg-box > div.status-msg-container > div.status-msg > div[id='status-msg-text']")
    protected WebElement statusMessageLbl;

    @FindBy(css = "div.tw-status-msg-box > div.close-sticky")
    protected WebElement statusMessageCloseBtn;

    public CICBasePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        tableUtils = new TableUtils(driver);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    protected void selectValueFromDDL(String ddlValue) {
        pageUtils.waitForElementAndClick(getDropDownValueElement(ddlValue));
    }

    protected void selectValueFromDDL(Integer index, String ddlValue) {
        this.driver.findElements(By.cssSelector(OPTIONS_CONTENT_OPEN_DROPDOWN_CSS)).get(index)
            .findElements(By.cssSelector("div[class='ss-option']"))
            .stream()
            .filter(e -> e.getText().equals(ddlValue))
            .findFirst()
            .ifPresent(WebElement::click);
    }

    protected void waitUntilDropDownValuesAreLoaded(String ddlValue) {
        WebElement webElement = this.driver.findElements(By.cssSelector(OPTIONS_CONTENT_OPEN_DROPDOWN_CSS)).get(1)
            .findElements(By.cssSelector("div[class='ss-option']"))
            .stream()
            .filter(e -> e.getText().equals(ddlValue))
            .findFirst()
            .get();
        pageUtils.waitForElementToBeClickable(webElement);
    }

    protected WebElement getDropDownValueElement(String ddlValue) {
        WebElement webElement = null;
        try {
            webElement = this.driver.findElements(By.cssSelector(OPTIONS_CONTENT_OPEN_DROPDOWN_CSS)).get(1)
                .findElements(By.cssSelector("div[class='ss-option']"))
                .stream()
                .filter(e -> e.getText().equals(ddlValue))
                .findFirst()
                .get();
        } catch (Exception e) {
            log.debug("Value not found in drop down field!!");
        }
        return webElement;
    }

    protected WebElement getNextButtonElement() {
        pageUtils.waitForElementToAppear(workflowPopUpActiveTabElement);
        return driver.findElement(By.xpath(String.format("//div[@sub-widget-container-id='tabsv2-79'][@tab-number='%s']//button[.='Next']", workflowPopUpActiveTabElement.getText())));
    }

    public WebElement getNameTextFieldElement() {
        return driver.findElement(with(By.xpath("//input")).below(By.xpath("//span[.='Name']")));
    }

    protected WebElement getQDReturnOnlyCheckboxElement() {
        pageUtils.waitForElementToAppear(workflowPopUpActiveTabElement);
        return driver.findElement(By.xpath(String.format("//div[@tab-number='%s']//input[@type='checkbox']", workflowPopUpActiveTabElement.getText())));
    }

    /**
     * capture the message from confirmation pop up window
     *
     * @return message
     */
    public String getStatusMessage() {
        pageUtils.waitForElementToAppear(statusMessagePopUpElement);
        return statusMessageLbl.getText();
    }

    /**
     * click close button on status message alert box
     */
    public void closeMessageAlert() {
        pageUtils.waitForElementAndClick(statusMessageCloseBtn);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
    }
}
