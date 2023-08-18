package com.apriori.pageobjects.evaluate;

import com.apriori.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class EvaluatePage extends LoadableComponent<EvaluatePage> {

    @FindBy(id = "qa-scenario-select-field")
    @CacheLookup
    private WebElement scenarioName;

    private  WebDriver driver;
    private  PageUtils pageUtils;

    public EvaluatePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.pageUtils = new PageUtils(driver);
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.windowHandler(1);
        pageUtils.waitForElementToAppear(scenarioName);
    }

    /**
     * Switches from parent to child tab
     *
     * @return page object
     */
    public EvaluatePage switchTab() {
        pageUtils.windowHandler(1);
        return this;
    }

    /**
     * Checks scenario name appears
     *
     * @return true/false
     */
    public boolean isCurrentScenarioNameDisplayed(String scenarioName) {
        By byCurrentScenario = By.xpath(String.format("//div[@class='costing-inputs']//div[text()='%s']", scenarioName));
        return pageUtils.waitForElementToAppear(byCurrentScenario).isDisplayed();
    }
}