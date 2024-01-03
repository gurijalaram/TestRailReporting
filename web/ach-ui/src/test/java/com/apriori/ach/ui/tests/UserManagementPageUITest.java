package com.apriori.ach.ui.tests;

import com.apriori.ach.api.utils.AchEnvironmentAPIUtil;
import com.apriori.ats.api.utils.AtsTestUtil;
import com.apriori.ats.api.utils.enums.ATSAPIEnum;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.qa.ach.ui.pageobjects.CloudHomePage;
import com.apriori.qa.ach.ui.pageobjects.UserManagementPage;
import com.apriori.qa.ach.ui.utils.AchEnvironmentUIUtil;
import com.apriori.qa.ach.ui.utils.enums.AdditionalProperties;
import com.apriori.qa.ach.ui.utils.enums.Roles;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.Users;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.web.app.util.login.LoginService;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class UserManagementPageUITest extends AchEnvironmentUIUtil {

    private AchEnvironmentAPIUtil achEnvironmentAPIUtil = new AchEnvironmentAPIUtil();
    private final UserCredentials userCredentials = achEnvironmentAPIUtil.getAwsCustomerUserCredentials();
    private CloudHomePage cloudHomePage;
    private UserManagementPage userManagementPage;
    private LoginService aprioriLoginService;
    private AtsTestUtil atsTestUtil = new AtsTestUtil();


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

        userManagementPage.findUser(username);

        softAssertions.assertThat(userManagementPage.getUsernameFromSearching()).isEqualTo(username);
        softAssertions.assertAll();
        deleteCreatedUser(email);
    }

    @Test
    @TestRail(id = {29299})
    public void validateInactivateUser() {
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

        userManagementPage.findUser(username);

        softAssertions.assertThat(userManagementPage.getUsernameFromSearching()).isEqualTo(username);;

        userManagementPage.clickOnThreeDotsUserRowAndHitDelete();

        softAssertions.assertThat(userManagementPage.isUserInactive()).isTrue();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28930})
    public void validateEditUserWindow() {
        SoftAssertions softAssertions = new SoftAssertions();
        aprioriLoginService = new LoginService(driver, "");
        cloudHomePage = aprioriLoginService.login(userCredentials, CloudHomePage.class);
        List<String> properties = new ArrayList<>();
        List<String> values = new ArrayList<>();
        properties.add("Name prefix");
        properties.add("Name suffix");
        values.add("prefix");
        values.add("suffix");

        userManagementPage =
            cloudHomePage.clickUserPanel()
                .clickUserManagementButton()
                .findUserAndClickEdit("qa-automation-28");

        softAssertions.assertThat(userManagementPage.editUserHeaderIsDisplayed()).isTrue();

        userManagementPage
            .clickNext()
            .secondPageEditUserChangeDataClickFinish(properties, values)
            .clickFinishButton();

        softAssertions.assertThat(userManagementPage.ifOnUserManagementPage()).isTrue();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29342})
    public void userAdminCannotUpdateTheirEnablements() {
        SoftAssertions softAssertions = new SoftAssertions();
        aprioriLoginService = new LoginService(driver, "");
        cloudHomePage = aprioriLoginService.login(userCredentials, CloudHomePage.class);

        String userName = StringUtils.substringBefore(userCredentials.getEmail(), "@");

        userManagementPage =
            cloudHomePage.clickUserPanel()
                .clickUserManagementButton()
                .findUserAndClickEdit(userName);

        softAssertions.assertThat(userManagementPage.isInfoCannotEditIsVisible()).isTrue();
        softAssertions.assertThat(userManagementPage.isDisabledToEditEnablements()).isTrue();
        softAssertions.assertAll();
    }

    private void deleteCreatedUser(String email) {
        CdsTestUtil cdsTestUtil = new CdsTestUtil();
        ResponseWrapper<User> response = atsTestUtil.getCommonRequest(ATSAPIEnum.USER_BY_EMAIL, User.class, HttpStatus.SC_OK, email);

        String customerIdentity = response.getResponseEntity().getCustomerIdentity();
        String userIdentity = response.getResponseEntity().getIdentity();
        cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
    }
}
