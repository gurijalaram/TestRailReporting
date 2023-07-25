package com.apriori.pageobjects.pages.login;

import com.apriori.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class AccountsPage extends EagerPageComponent<AccountsPage> {

    @FindBy(css = ".btn-outline-primary [data-icon='folder-plus']")
    private WebElement uploadBom;

    public AccountsPage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(uploadBom);
    }
}
