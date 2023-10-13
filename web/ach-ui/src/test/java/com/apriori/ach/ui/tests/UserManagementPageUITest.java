package com.apriori.ach.ui.tests;

import com.apriori.ach.utils.AchEnvironmentAPIUtil;
import com.apriori.login.LoginService;
import com.apriori.qa.ach.ui.pageobjects.CloudHomePage;
import com.apriori.qa.ach.ui.pageobjects.UserManagementPage;
import com.apriori.qa.ach.ui.utils.AchEnvironmentUIUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.testrail.TestRail;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UserManagementPageUITest extends AchEnvironmentUIUtil {

    private AchEnvironmentAPIUtil achEnvironmentAPIUtil = new AchEnvironmentAPIUtil();
    private final UserCredentials userCredentials = achEnvironmentAPIUtil.getAwsCustomerUserCredentials();
    private CloudHomePage cloudHomePage;
    private UserManagementPage userManagementPage;
    private LoginService aprioriLoginService;

    @Test
    @TestRail(id = {28492,28502})
    public void validateCustomerApplicationsByUI() {
        SoftAssertions softAssertions = new SoftAssertions();

        aprioriLoginService = new LoginService(driver, "");
        cloudHomePage = aprioriLoginService.login(userCredentials, CloudHomePage.class);

        userManagementPage =
        cloudHomePage.clickUserPanel()
                .clickUserManagementButton()
                    .clickAdduser();

        List<String> listOfRoles = userManagementPage.clickDropDownAndGetRoles();

        softAssertions.assertThat(listOfRoles).contains("APRIORI ANALYST","APRIORI CONTRIBUTOR","APRIORI DESIGNER",
            "APRIORI DEVELOPER","APRIORI EDC","APRIORI EXPERT");

        List<String> additionalProperties = userManagementPage.getAdditionalProperties();

        softAssertions.assertThat(additionalProperties).containsExactly("aP Connect Admin","aP User Admin","aP High Memory","aP Export Admin");
        softAssertions.assertAll();
    }
}
