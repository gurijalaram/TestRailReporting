package com.apriori.qa.ach.ui.pageobjects;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.PageUtils;
import com.apriori.login.LoginService;
import com.apriori.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class CloudHomeLoginPage extends LoadableComponent<CloudHomeLoginPage> {

    private WebDriver driver;
    private PageUtils pageUtils;
    private LoginService aprioriLoginService;

    public CloudHomeLoginPage(WebDriver driver) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        this.aprioriLoginService = new LoginService(driver, "");
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));

        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue(aprioriLoginService.getLoginTitle().contains("Cloud Home"), "Cloud Home login page was not displayed");
    }

    /**
     * Login to Cloud Home
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public CloudHomePage login(final UserCredentials userCredentials) {
        return aprioriLoginService.login(userCredentials, CloudHomePage.class);
    }

}
