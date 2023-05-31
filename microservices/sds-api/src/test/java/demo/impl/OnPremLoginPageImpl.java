package demo.impl;

import demo.LoginPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class OnPremLoginPageImpl implements LoginPage {

    @FindBy(css = "secondPath")
    private WebElement cloudLoginTitle;

    @FindBy(xpath = "secondPath")
    private WebElement reportsLoginTitle;

    @Override
    public void doLogin() {
        return;
    }

    @Override
    public void doLogout() {
        return;
    }
}
