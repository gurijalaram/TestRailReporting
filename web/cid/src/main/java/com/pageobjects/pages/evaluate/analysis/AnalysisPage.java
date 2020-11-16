package com.pageobjects.pages.evaluate.analysis;

import com.apriori.utils.PageUtils;

import com.pageobjects.pages.evaluate.EvaluatePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalysisPage extends LoadableComponent<AnalysisPage> {

    private final Logger logger = LoggerFactory.getLogger(AnalysisPage.class);

    @FindBy(xpath = "//li[.='Properties']")
    private WebElement propertiesButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public AnalysisPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(propertiesButton);
    }

    /**
     * Selects the properties button
     *
     * @return new page object
     */
    public PropertiesDialogPage selectProperties() {
        pageUtils.waitForElementAndClick(propertiesButton);
        return new PropertiesDialogPage(driver);
    }

    /**
     * Selects the properties button to close
     *
     * @return new page object
     */
    public EvaluatePage closeProperties() {
        pageUtils.waitForElementAndClick(propertiesButton);
        return new EvaluatePage(driver);
    }
}
