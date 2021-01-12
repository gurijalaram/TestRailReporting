package com.apriori.header;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CostingServiceSettings extends LoadableComponent<CostingServiceSettings> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CostingServiceSettings.class);

    @FindBy(css = "div[id='root_navigation-40-popup_label-212'] > span")
    private WebElement costingServiceSettingsText;

    private WebDriver driver;
    private PageUtils pageUtils;

    public CostingServiceSettings(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(costingServiceSettingsText);
    }

    /**
     * Get Costing Service Settings modal title text
     * @return String
     */
    public String getCostingServiceSettingsText() {
        return costingServiceSettingsText.getText();
    }
}
