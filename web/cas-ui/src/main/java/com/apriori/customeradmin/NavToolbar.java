package com.apriori.customeradmin;

import com.apriori.login.CasLoginPage;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class NavToolbar extends EagerPageComponent<NavToolbar> {

    @FindBy(css = "img[alt='Application Logo']")
    private WebElement aprioriLogo;

    @FindBy(xpath = "//button[.='Customers']")
    private WebElement customersButton;

    @FindBy(xpath = "//button[contains(@class,'btn-secondary')][.='Help']")
    private WebElement helpDropdown;

    @FindBy(xpath = "//button[contains(@class,'dropdown-item')][.='Help']")
    private WebElement helpLink;

    @FindBy(xpath = "//button[contains(@class,'dropdown-item')][.='About']")
    private WebElement aboutLink;

    @FindBy(css = "div[class='user-dropdown dropdown']")
    private WebElement userDropdown;

    @FindBy(xpath = "//button[.='My Profile']")
    private WebElement myProfileLink;

    @FindBy(xpath = "//button[.='Terms of Use']")
    private WebElement termsLink;

    @FindBy(xpath = "//button[.='Logout']")
    private WebElement logoutLink;

    public NavToolbar(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementAppear(aprioriLogo);
        getPageUtils().waitForElementAppear(customersButton);
    }

    /**
     * Logout the user
     *
     * @return new page object
     */
    public CasLoginPage logout() {
        getPageUtils().waitForElementAndClick(userDropdown);
        getPageUtils().waitForElementAndClick(logoutLink);
        return new CasLoginPage(getDriver());
    }

    public CustomerAdminPage clickCustomersButton() {
        getPageUtils().waitForElementAndClick(customersButton);
        return new CustomerAdminPage(getDriver());
    }
}
