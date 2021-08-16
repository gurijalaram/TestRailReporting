package com.myuser;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.navtoolbars.myuser.TermsOfUsePage;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class TermsOfUseTests extends TestBase {

    private EdcAppLoginPage loginPage;
    private TermsOfUsePage termsOfUsePage;

    @Test
    @TestRail(testCaseId = {"8923"})
    @Description("Validate Terms of use")
    public void testTermsOfUse() {
        loginPage = new EdcAppLoginPage(driver);
        termsOfUsePage = loginPage.login(UserUtil.getUser())
            .clickUserDropdown()
            .selectTermsOfUse();

        assertThat(termsOfUsePage.getTermsOfUseUrl(), containsString("https://edc.na-1.qa-21-1.apriori.net/terms-of-use"));
        assertThat(termsOfUsePage.getTermsOfUseText(), containsString(" application (”Application”), including the intellectual property rights and trade secrets contained therein"));
    }
}
