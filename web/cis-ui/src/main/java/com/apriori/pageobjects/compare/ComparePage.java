package com.apriori.pageobjects.compare;

import com.apriori.navtoolbars.CompareTabToolbar;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class ComparePage extends CompareTabToolbar {

    @FindBy(css = "[data-icon='pencil-alt']")
    private WebElement modifyButton;

    public ComparePage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(modifyButton);
    }
}
