package com.apriori.pageobjects.navtoolbars;

import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;

@Slf4j
public class CisHeaderBar extends EagerPageComponent<CisHeaderBar> {

    @FindBy(xpath = "//*[@data-testid='title']")
    private WebElement headerText;

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
     * Gets the header text on header bar
     *
     * @return String
     */
    public String getHeaderText() {
        return getPageUtils().waitForElementToAppear(headerText).getText();
    }
}