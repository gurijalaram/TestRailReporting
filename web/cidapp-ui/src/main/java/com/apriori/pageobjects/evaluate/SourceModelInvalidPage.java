package com.apriori.pageobjects.evaluate;

import com.apriori.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class SourceModelInvalidPage extends LoadableComponent<SourceModelInvalidPage> {

    @FindBy(xpath = "//button[.='Ignore']")
    private WebElement ignoreButton;

    @FindBy(css = (".modal-title"))
    private WebElement sourceModel;

    @FindBy(xpath = "//button[.='Fix Source']")
    private WebElement fixSourceButton;

    @FindBy(css = ".close")
    private WebElement xButton;

    private PageUtils pageUtils;
    private WebDriver driver;

    public SourceModelInvalidPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(ignoreButton);
        pageUtils.waitForElementToAppear(fixSourceButton);
    }

    /**
     * Click Ignore button
     *
     * @return new page object
     */
    public EvaluatePage clickIgnore() {
        pageUtils.waitForElementAndClick(ignoreButton);
        return new EvaluatePage(driver);
    }

    /**
     * Get Source Model text on the page
     *
     * @return String
     */
    public String getSourceModelInvalidMsg() {
        return pageUtils.waitForElementToAppear(sourceModel).getAttribute("textContent");
    }

    /**
     * Click Fix Source button
     *
     * @return new page object
     */
    public EvaluatePage clickFixSource() {
        pageUtils.waitForElementAndClick(fixSourceButton);
        return new EvaluatePage(driver);
    }

    /**
     * Click on the X button
     *
     * @return new page object
     */
    public EvaluatePage closeSMInvalidPanel() {
        pageUtils.waitForElementAndClick(xButton);
        return new EvaluatePage(driver);
    }
}
