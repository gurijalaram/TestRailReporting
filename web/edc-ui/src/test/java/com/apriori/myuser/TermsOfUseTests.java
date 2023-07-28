package com.apriori.myuser;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.TestBaseUI;
import com.apriori.pageobjects.navtoolbars.TermsOfUsePage;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;

public class TermsOfUseTests extends TestBaseUI {

    private EdcAppLoginPage loginPage;
    private TermsOfUsePage termsOfUsePage;
    private UserCredentials currentUser;

    public TermsOfUseTests() {
        super();
    }

    @Test
    @TestRail(id = {8923})
    @Description("Validate Terms of use")
    public void testTermsOfUse() {
        currentUser = UserUtil.getUser();

        loginPage = new EdcAppLoginPage(driver);
        termsOfUsePage = loginPage.login(currentUser)
            .clickTermsOfUse();

        assertThat(termsOfUsePage.getTermsOfUseUrl(), containsString("https://edc.na-1.qa-test.apriori.net/terms-of-use"));
        assertThat(termsOfUsePage.getTermsOfUseText(), containsString(" application (”Application”), including the intellectual property rights and trade secrets contained therein"));
    }
}
