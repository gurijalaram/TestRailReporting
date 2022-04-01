package com.apriori.pageobjects.navtoolbars;

import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;

@Slf4j
public class CisHeaderBar extends EagerPageComponent<CisHeaderBar> {

    @FindBy(xpath = "//div[@class='MuiTypography-root MuiTypography-d1 css-5mobn1']")
    private WebElement welcomeText;

    public CisHeaderBar(WebDriver driver) {
        this(driver, log);
    }

    public CisHeaderBar(WebDriver driver, Logger logger) {
        super(driver, logger);

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Gets the welcome text on header bar
     *
     * @return String
     */
    public String getWelcomeText() {
        return getPageUtils().waitForElementToAppear(welcomeText).getText();
    }
}
