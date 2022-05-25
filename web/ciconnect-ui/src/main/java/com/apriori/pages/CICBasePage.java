package com.apriori.pages;

import com.apriori.pagedata.WorkFlowData;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.LoadableComponent;
import utils.TableUtils;
import utils.UIUtils;


public class CICBasePage extends LoadableComponent<CICBasePage> {

    protected static final String OPTIONS_CONTENT_OPEN_DROPDOWN_CSS = "div[class^='ss-content ss-'][class$='ss-open'] div[class='ss-list']";
    protected static final String PARENT_ELEMENT_CSS = "div[id^='root_pagemashupcontainer-1_navigation-']";
    protected static final long WAIT_TIME = 900;
    protected static WorkFlowData workFlowData;

    protected WebDriver driver;
    protected PageUtils pageUtils;
    protected TableUtils tableUtils;
    protected UIUtils uiUtils;


    public CICBasePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        tableUtils = new TableUtils(driver);
        uiUtils = new UIUtils();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {

    }

    protected void selectValueFromDDL(String ddlValue) {
        this.driver.findElements(By.cssSelector(OPTIONS_CONTENT_OPEN_DROPDOWN_CSS)).get(1)
            .findElements(By.cssSelector("div[class='ss-option']"))
            .stream()
            .filter(e -> e.getText().equals(ddlValue))
            .findFirst()
            .ifPresent(WebElement::click);
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
        pageUtils.waitForElementToAppear(webElement);
    }

    protected void waitUntilDropDownValuesAreLoaded(Integer index, String ddlValue) {
        WebElement webElement = this.driver.findElements(By.cssSelector(OPTIONS_CONTENT_OPEN_DROPDOWN_CSS)).get(index)
            .findElements(By.cssSelector("div[class='ss-option']"))
            .stream()
            .filter(e -> e.getText().equals(ddlValue))
            .findFirst()
            .get();
        pageUtils.waitForElementToAppear(webElement);
    }

}