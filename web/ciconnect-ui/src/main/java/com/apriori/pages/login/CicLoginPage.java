package com.apriori.pages.login;

import static org.junit.Assert.assertTrue;

import com.apriori.pages.CICBasePage;
import com.apriori.pages.home.CIConnectHome;
import com.apriori.utils.PageUtils;
import com.apriori.utils.login.AprioriLoginPage;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import utils.TableUtils;

public class CicLoginPage extends CICBasePage {

    private AprioriLoginPage aprioriLoginPage;

    public CicLoginPage(WebDriver driver) {
        super(driver);
        this.aprioriLoginPage = new AprioriLoginPage(driver, "ci-connect");
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("CIC login page was not displayed", aprioriLoginPage.getLoginTitle(false).contains("Cost Insight Connect"));
    }

    /**
     * Login to cid
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public CIConnectHome login(final UserCredentials userCredentials) {
        return aprioriLoginPage.login(userCredentials, false, false, CIConnectHome.class);
    }
}