package demo.impl;

import demo.LoginPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CommonLoginPageImpl implements LoginPage {

    @FindBy(css = "onePath")
    private WebElement cloudLoginTitle;

    @FindBy(xpath = "onePath")
    private WebElement reportsLoginTitle;

    @Override
    public void doLogin() {

    }

    @Override
    public void doLogout() {

    }
}
