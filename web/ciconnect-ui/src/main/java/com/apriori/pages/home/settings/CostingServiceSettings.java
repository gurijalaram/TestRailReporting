package com.apriori.pages.home.settings;

import com.apriori.pages.CICBasePage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Costing Service Settings page
 */
public class CostingServiceSettings extends CICBasePage {

    private static final Logger logger = LoggerFactory.getLogger(CostingServiceSettings.class);

    @FindBy(css = "div[id='root_navigation-40-popup_label-212'] > span")
    private WebElement costingServiceSettingsText;

    public CostingServiceSettings(WebDriver driver) {
        super(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
     *
     * @return String
     */
    public String getCostingServiceSettingsText() {
        return costingServiceSettingsText.getText();
    }
}
