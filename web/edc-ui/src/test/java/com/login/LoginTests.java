package com.login;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.pageobjects.pages.login.ElectronicsDataCollectionPage;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import java.io.File;

@Slf4j
public class LoginTests extends TestBase{

    public LoginTests() {
        super();
    }

    private File resourceFile;
    private UserCredentials currentUser;
    private EdcAppLoginPage loginPage;
    private ElectronicsDataCollectionPage edcPage;

    @Test
//    @TestRail(testCaseId = {""})
    @Description("Test successful login")
    public void testLogin() {

        currentUser = UserUtil.getUser();
        loginPage = new EdcAppLoginPage(driver);
        edcPage = loginPage.login(currentUser);

        assertThat(edcPage.isUploadedBillOfMaterials("Uploaded Bill of Materials"), is(true));
    }
}
