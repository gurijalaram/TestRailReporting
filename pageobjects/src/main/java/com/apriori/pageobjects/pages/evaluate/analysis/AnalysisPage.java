package com.apriori.pageobjects.pages.evaluate.analysis;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalysisPage extends LoadableComponent<AnalysisPage> {

    private final Logger logger = LoggerFactory.getLogger(AnalysisPage.class);

    @FindBy(css = "//li[.='Properties']")
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
    }

    public PropertiesDialogPage selectProperties() {
        pageUtils.waitForElementAndClick(propertiesButton);
        return new PropertiesDialogPage(driver);
    }
}
