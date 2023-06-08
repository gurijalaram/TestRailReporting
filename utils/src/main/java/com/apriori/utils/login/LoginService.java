package com.apriori.utils.login;

import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import org.openqa.selenium.WebDriver;

public class LoginService {
    private WebDriver driver;
    private String application;
    private LoginPage loginPage;

    /**
     * LoginService generic constructor
     *
     * @param driver - driver instance to use
     * @param application - application to log in to
     */
    public LoginService(WebDriver driver, String application) {
        this.driver = driver;
        this.application = application;
        this.loginPage = PropertiesContext.get("${env}").equals("onprem") ? new OnPremLoginPageImplementation(driver, application) : new CommonLoginPageImplementation(driver, application);
    }

    /**
     * Generic login method
     *
     * @param userCredentials - user to log in with
     * @param klass - class to return
     * @return - return instance of class specified
     * @param <T> - specified class to return
     */
    public <T> T login(final UserCredentials userCredentials, Class<T> klass) {
        return loginPage.performLogin(userCredentials, klass);
    }
}
