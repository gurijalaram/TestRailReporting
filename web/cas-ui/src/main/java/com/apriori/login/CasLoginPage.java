package com.apriori.login;

import static org.junit.Assert.assertTrue;

import com.apriori.customeradmin.CustomerAdminPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.login.AprioriLoginPage;
import com.apriori.utils.reader.file.user.UserCredentials;

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
    private AprioriLoginPage aprioriLoginPage;

    public CasLoginPage(WebDriver driver) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        this.aprioriLoginPage = new AprioriLoginPage(driver, "cas");
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("CAS login page was not displayed", aprioriLoginPage.getLoginTitle().contains("Customer Admin"));
    }

    /**
     * Login to CAS
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public CustomerAdminPage login(final UserCredentials userCredentials) {
        return aprioriLoginPage.login(userCredentials, CustomerAdminPage.class);
    }
}
