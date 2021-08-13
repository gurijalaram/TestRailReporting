package com.myuser;

import com.apriori.pageobjects.navtoolbars.myuser.MyProfilePage;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import org.junit.Test;

public class TermsOfUseTests extends TestBase {

    private EdcAppLoginPage loginPage;

    @Test
    public void testTermsOfUse() {
        loginPage = new EdcAppLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .clickUserDropdown()
            .selectTermsOfUse();
    }
}
