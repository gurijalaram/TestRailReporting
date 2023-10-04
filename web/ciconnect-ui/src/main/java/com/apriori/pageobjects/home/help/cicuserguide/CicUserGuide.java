package com.apriori.pageobjects.home.help.cicuserguide;

import com.apriori.pageobjects.CICBasePage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CIC user Guide page
 */
public class CicUserGuide extends CICBasePage {
    private static final Logger logger = LoggerFactory.getLogger(CicUserGuide.class);

    @FindBy(css = "div[id='wwpID0ELHA']")
    private WebElement userGuideTitle;

    public CicUserGuide(WebDriver driver) {
        super(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    protected void load() {
    }

    protected void isLoaded() throws Error {

    }

    /**
     * Switch to second tab
     *
     * @return CicUserGuide Page
     */
    public CicUserGuide switchTab() {
        pageUtils.switchToWindow(1);
        return new CicUserGuide(driver);
    }


    /**
     * Get user guide title text
     *
     * @return String, title text
     */
    public String getUserGuideTitle() {
        return userGuideTitle.getText();
    }

    /**
     * Get tab two URL
     *
     * @return String
     */
    public String getURL() {
        return pageUtils.getTabTwoUrl();
    }
}
