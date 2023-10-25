package com.apriori.ach.ui.tests;

import com.apriori.ach.utils.AchEnvironmentAPIUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.login.LoginService;
import com.apriori.qa.ach.ui.pageobjects.CloudHomePage;
import com.apriori.qa.ach.ui.pageobjects.UserManagementPage;
import com.apriori.qa.ach.ui.utils.AchEnvironmentUIUtil;
import com.apriori.qa.ach.ui.utils.enums.AdditionalProperties;
import com.apriori.qa.ach.ui.utils.enums.Roles;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.testrail.TestRail;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

public class UserManagementPageUITest extends AchEnvironmentUIUtil {

    private AchEnvironmentAPIUtil achEnvironmentAPIUtil = new AchEnvironmentAPIUtil();
    private final UserCredentials userCredentials = achEnvironmentAPIUtil.getAwsCustomerUserCredentials();
    private CloudHomePage cloudHomePage;
    private UserManagementPage userManagementPage;
    private LoginService aprioriLoginService;

    @Test
    @TestRail(id = {28492, 28502})
    public void validateRolesAndAdditionalPropertiesTest() {
        SoftAssertions softAssertions = new SoftAssertions();

        aprioriLoginService = new LoginService(driver, "");
        cloudHomePage = aprioriLoginService.login(userCredentials, CloudHomePage.class);

        userManagementPage =
            cloudHomePage.clickUserPanel()
                .clickUserManagementButton()
                .clickAdduser();

        List<String> listOfRoles = userManagementPage.clickDropDownAndGetRoles();

        softAssertions.assertThat(listOfRoles).contains(Roles.APRIORI_ANALYST.getRole(), Roles.APRIORI_CONTRIBUTOR.getRole(), Roles.APRIORI_DESIGNER.getRole(),
            Roles.APRIORI_DEVELOPER.getRole(), Roles.APRIORI_EDC.getRole(), Roles.APRIORI_EXPERT.getRole());

        List<String> additionalProperties = userManagementPage.getAdditionalProperties();

        softAssertions.assertThat(additionalProperties).containsExactly(AdditionalProperties.AP_CONNECT_ADMIN.getProperties(), AdditionalProperties.AP_USER_ADMIN.getProperties(),
            AdditionalProperties.AP_HIGH_MEMORY.getProperties(), AdditionalProperties.AP_EXPORT_ADMIN.getProperties());
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28924})
    public void validateAddingUser() {
        SoftAssertions softAssertions = new SoftAssertions();
        String username = new GenerateStringUtil().generateUserName();
        String email = new GenerateStringUtil().generateEmail();

        aprioriLoginService = new LoginService(driver, "");
        cloudHomePage = aprioriLoginService.login(userCredentials, CloudHomePage.class);

        userManagementPage =
            cloudHomePage.clickUserPanel()
                .clickUserManagementButton()
                .clickAdduser()
                .clickDropDownAndChooseRole(Roles.APRIORI_ANALYST.getRole())
                .clickNext()
                .fillInAllRequiredInfo(username, email)
                .clickFinishButton();

        softAssertions.assertThat(userManagementPage.ifOnUserManagementPage()).isTrue();
    }
}
