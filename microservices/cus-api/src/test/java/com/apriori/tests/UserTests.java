package com.apriori.tests;

import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cusapi.entity.request.UpdateUserRequest;
import com.apriori.cusapi.entity.request.UserProfile;
import com.apriori.cusapi.entity.response.ErrorResponse;
import com.apriori.cusapi.entity.response.User;
import com.apriori.cusapi.utils.PeopleUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

public class UserTests {
    private static UserCredentials currentUser;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private PeopleUtil peopleUtil = new PeopleUtil();
    private SoftAssertions softAssertions = new SoftAssertions();

    @Before
    public void setupUser() {
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(testCaseId = "16822")
    @Description("Verify GET current user endpoint test")
    public void verifyCurrentUserTest() {
        User user = new PeopleUtil().getCurrentUser(currentUser);
        softAssertions.assertThat(user.getUserType()).isEqualTo("AP_STAFF_USER");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17064")
    @Description("Update valid fields via CUS /users/current endpoint")
    public void updateCurrentUserValidFieldsTest() {
        String newPrefix = new GenerateStringUtil().getRandomStringSpecLength(8);
        String newSuffix = new GenerateStringUtil().getRandomStringSpecLength(8);
        String newJobTitle = new GenerateStringUtil().getRandomStringSpecLength(8);
        String newDepartment = new GenerateStringUtil().getRandomStringSpecLength(8);
        String newTownCity = new GenerateStringUtil().getRandomStringSpecLength(8);
        String newCounty = new GenerateStringUtil().getRandomStringSpecLength(8);
        String newOfficePhoneCountryCode = new GenerateStringUtil().getRandomNumbersSpecLength(3);
        String newOfficePhoneNumber = new GenerateStringUtil().getRandomNumbers();
        String newTimezone = new GenerateStringUtil().getRandomStringSpecLength(3);

        UpdateUserRequest  updateUserRequest = UpdateUserRequest.builder()
            .userProfile(UserProfile.builder()
                .prefix(newPrefix)
                .suffix(newSuffix)
                .jobTitle(newJobTitle)
                .department(newDepartment)
                .townCity(newTownCity)
                .county(newCounty)
                .officePhoneCountryCode(newOfficePhoneCountryCode)
                .officePhoneNumber(newOfficePhoneNumber)
                .timezone(newTimezone)
                .build())
            .build();

        User response = peopleUtil.updateCurrentUser(currentUser,updateUserRequest);

        softAssertions.assertThat(response.getUserProfile().getPrefix()).isEqualTo(newPrefix);
        softAssertions.assertThat(response.getUserProfile().getSuffix()).isEqualTo(newSuffix);
        softAssertions.assertThat(response.getUserProfile().getJobTitle()).isEqualTo(newJobTitle);
        softAssertions.assertThat(response.getUserProfile().getDepartment()).isEqualTo(newDepartment);
        softAssertions.assertThat(response.getUserProfile().getTownCity()).isEqualTo(newTownCity);
        softAssertions.assertThat(response.getUserProfile().getCounty()).isEqualTo(newCounty);
        softAssertions.assertThat(response.getUserProfile().getOfficePhoneCountryCode()).isEqualTo(newOfficePhoneCountryCode);
        softAssertions.assertThat(response.getUserProfile().getOfficePhoneNumber()).isEqualTo(newOfficePhoneNumber);
        softAssertions.assertThat(response.getUserProfile().getTimezone()).isEqualTo(newTimezone);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17065")
    @Description("Try to update SAML fields via CUS /users/current endpoint should be not possible")
    public void updateSamlFieldsShouldBeNotPossibleTest() {
        String newUsername = new GenerateStringUtil().getRandomStringSpecLength(8);
        String newEmail = new GenerateStringUtil().generateEmail();

        UpdateUserRequest  updateUserRequest = UpdateUserRequest.builder()
            .username(newUsername)
            .email(newEmail)
            .userProfile(UserProfile.builder().build())
            .build();

        User response = peopleUtil.updateCurrentUser(currentUser,updateUserRequest);

        softAssertions.assertThat(response.getEmail()).isEqualTo(currentUser.getEmail());
        softAssertions.assertThat(response.getUsername()).isEqualTo(currentUser.getUsername());
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17109")
    @Description("Trying to update givenName or FamilyName should give an error")
    public void updateGivenNameOrFamilyNameShouldGiveErrorTest() {
        String newGivenName = new GenerateStringUtil().getRandomStringSpecLength(8);
        String newFamilyName = new GenerateStringUtil().getRandomStringSpecLength(8);

        UpdateUserRequest  updateUserRequest = UpdateUserRequest.builder()
            .userProfile(UserProfile.builder()
                .familyName(newFamilyName)
                .build())
            .build();

        ErrorResponse response = peopleUtil.updateCurrentUserBadRequest(currentUser,updateUserRequest);

        softAssertions.assertThat(response.getMessage()).isEqualTo("'familyName' can not be changed");
        softAssertions.assertThat(response.getError()).isEqualTo("Bad Request");
        softAssertions.assertThat(response.getStatus()).isEqualTo(400);
        softAssertions.assertAll();

        UpdateUserRequest  updateUserRequest2 = UpdateUserRequest.builder()
            .userProfile(UserProfile.builder()
                .givenName(newGivenName)
                .build())
            .build();

        ErrorResponse response2 = peopleUtil.updateCurrentUserBadRequest(currentUser,updateUserRequest2);

        softAssertions.assertThat(response2.getMessage()).isEqualTo("'givenName' can not be changed");
        softAssertions.assertThat(response2.getError()).isEqualTo("Bad Request");
        softAssertions.assertThat(response2.getStatus()).isEqualTo(400);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "17066")
    @Description("Trying to update givenName or FamilyName as empty should give an error")
    public void updateGivenNameOrFamilyNameAsEmptyShouldGiveErrorTest() {
        UpdateUserRequest  updateUserRequest = UpdateUserRequest.builder()
            .userProfile(UserProfile.builder()
                .familyName("")
                .build())
            .build();

        ErrorResponse response = peopleUtil.updateCurrentUserBadRequest(currentUser,updateUserRequest);

        softAssertions.assertThat(response.getMessage()).isEqualTo("'familyName' can not be changed");
        softAssertions.assertThat(response.getError()).isEqualTo("Bad Request");
        softAssertions.assertThat(response.getStatus()).isEqualTo(400);
        softAssertions.assertAll();

        UpdateUserRequest  updateUserRequest2 = UpdateUserRequest.builder()
            .userProfile(UserProfile.builder()
                .givenName("")
                .build())
            .build();

        ErrorResponse response2 = peopleUtil.updateCurrentUserBadRequest(currentUser,updateUserRequest2);

        softAssertions.assertThat(response2.getMessage()).isEqualTo("'givenName' can not be changed");
        softAssertions.assertThat(response2.getError()).isEqualTo("Bad Request");
        softAssertions.assertThat(response2.getStatus()).isEqualTo(400);
        softAssertions.assertAll();
    }
}
