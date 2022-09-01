package com.myuser;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.navtoolbars.TermsOfUsePage;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class TermsOfUseTests extends TestBase {

    private EdcAppLoginPage loginPage;
    private TermsOfUsePage termsOfUsePage;
    private UserCredentials currentUser;

    public TermsOfUseTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"8923"})
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
