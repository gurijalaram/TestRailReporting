package com.apriori.auth.ui.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.qa.ach.ui.pageobjects.CloudHomePage;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.web.app.util.login.LoginService;
import com.apriori.web.app.util.login.UserProfilePage;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class UserProfileTests extends TestBaseUI {

    private LoginService aprioriLoginService;
    private UserCredentials currentUser;

    public UserProfileTests() {
        super();
    }

    @BeforeEach
    public void setup() {
        aprioriLoginService = new LoginService(driver, "auth-ui");
    }

    @Test
    @TestRail(id = 17001)
    @Description("Verify that correct input fields are present on User Profile page")
    public void verifyInputFields() {

        List<String> expectedResults = Arrays.asList("Username", "Email", "Given Name", "Family Name", "Name prefix", "Name suffix", "Job title", "Department", "Town or City",
            "County", "Country", "Time Zone", "Office Phone Country Code", "Office Phone Number");
        currentUser = UserUtil.getUser();
        UserProfilePage userProfilePage = aprioriLoginService.login(currentUser, CloudHomePage.class)
            .goToProfilePage();
        assertThat(userProfilePage.getAllInputFieldsName(), is(expectedResults));
    }
}

