package com.apriori.login;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.PageUtils;
import com.apriori.customeradmin.CustomerAdminPage;
import com.apriori.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

/**
 * @author cfrith
 */

@Slf4j
public class CasLoginPage extends LoadableComponent<CasLoginPage> {

    private WebDriver driver;
    private PageUtils pageUtils;
    private LoginService aprioriLoginService;

    public CasLoginPage(WebDriver driver) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        this.aprioriLoginService = new LoginService(driver, "cas");
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue(aprioriLoginService.getLoginTitle().contains("Customer Admin"), "CAS login page was not displayed");
    }

    /**
     * Login to CAS
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public CustomerAdminPage login(final UserCredentials userCredentials) {
        return aprioriLoginService.login(userCredentials, CustomerAdminPage.class);
    }
}
