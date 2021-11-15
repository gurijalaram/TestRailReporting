package com.apriori.pageobjects.pages.compare;

import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class ComparePage extends EagerPageComponent<ComparePage> {

    @FindBy(css = "[data-icon='pencil-alt']")
    private WebElement modifyButton;

    public ComparePage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(modifyButton);
    }
}
