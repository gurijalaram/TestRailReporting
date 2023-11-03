package com.apriori.edc.ui.pageobjects.login;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.web.app.util.PageUtils;
import com.apriori.web.app.util.login.LoginService;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class EdcAppLoginPage extends LoadableComponent<EdcAppLoginPage> {

    @FindBy(css = "input[name='email']")
    private WebElement email;

    @FindBy(css = "input[name='password']")
    private WebElement password;

    @FindBy(css = "button[type='submit']")
    private WebElement submitLogin;

    @FindBy(css = "div.auth0-global-message.auth0-global-message-error span")
    private WebElement loginErrorMsg;

    private WebDriver driver;
    private PageUtils pageUtils;
    private LoginService aprioriLoginService;

    public EdcAppLoginPage(WebDriver driver) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        this.aprioriLoginService = new LoginService(driver, "edc");
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue(aprioriLoginService.getLoginTitle().contains("Electronics Data Collection"), "EDC login page was not displayed");
    }

    /**
     * Login to EDC
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public ElectronicsDataCollectionPage login(final UserCredentials userCredentials) {
        return aprioriLoginService.login(userCredentials, ElectronicsDataCollectionPage.class);
    }
}
