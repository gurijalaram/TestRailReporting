package com.apriori.edc.ui.tests.myuser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.edc.ui.pageobjects.login.EdcAppLoginPage;
import com.apriori.edc.ui.pageobjects.navtoolbars.TermsOfUsePage;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

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
