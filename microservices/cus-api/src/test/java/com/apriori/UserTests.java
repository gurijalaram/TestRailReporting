package com.apriori;

import com.apriori.cus.models.request.UpdateUserRequest;
import com.apriori.cus.models.request.UserProfile;
import com.apriori.cus.models.response.ErrorResponse;
import com.apriori.cus.models.response.User;
import com.apriori.cus.utils.PeopleUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class UserTests {
    private static UserCredentials currentUser;
    private final PeopleUtil peopleUtil = new PeopleUtil();
    private final SoftAssertions softAssertions = new SoftAssertions();
    private final GenerateStringUtil generator = new GenerateStringUtil();
    private static final String badRequest = "Bad Request";
    private static final String expectedMessageFamilyName = "'familyName' should not be null.";
    private static final String expectedMessageGivenName = "'givenName' should not be null.";

    @BeforeEach
    public void setupUser() {
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(id = 16822)
    @Description("Verify GET current user endpoint test")
    public void verifyCurrentUserTest() {
        User user = peopleUtil.getCurrentUser(currentUser);
        softAssertions.assertThat(user.getEmail()).isEqualTo(currentUser.getEmail());
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17064)
    @Description("Update valid fields via CUS /users/current endpoint")
    public void updateCurrentUserValidFieldsTest() {
        String newPrefix = generator.getRandomStringSpecLength(8);
        String newSuffix = generator.getRandomStringSpecLength(8);
        String newJobTitle = generator.getRandomStringSpecLength(8);
        String newDepartment = generator.getRandomStringSpecLength(8);
        String newTownCity = generator.getRandomStringSpecLength(8);
        String newCounty = generator.getRandomStringSpecLength(8);
        String newOfficePhoneCountryCode = generator.getRandomNumbersSpecLength(3);
        String newOfficePhoneNumber = generator.getRandomNumbers();
        String newTimezone = generator.getRandomStringSpecLength(3);

        User user = peopleUtil.getCurrentUser(currentUser);

        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
            .identity(user.getIdentity())
            .username(user.getUsername())
            .email(user.getEmail())
            .active(user.getActive())
            .createdBy(user.getCreatedBy())
            .userProfile(UserProfile.builder()
                .givenName(user.getUserProfile().getGivenName())
                .familyName(user.getUserProfile().getFamilyName())
                .createdBy(user.getUserProfile().getCreatedBy())
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

        User response = peopleUtil.updateCurrentUser(currentUser, updateUserRequest);

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
    @TestRail(id = 17065)
    @Disabled("It is better to not run this test till issue will be fixed, because it will update Auto user with new name and email")
    @Issue("CFIRST-414")
    @Description("Try to update SAML fields via CUS /users/current endpoint should be not possible")
    public void updateSamlFieldsShouldBeNotPossibleTest() {
        String newUsername = generator.getRandomStringSpecLength(8);
        String newEmail = generator.generateEmail();

        User user = peopleUtil.getCurrentUser(currentUser);

        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
            .identity(user.getIdentity())
            .username(newUsername)
            .email(newEmail)
            .active(user.getActive())
            .createdBy(user.getCreatedBy())
            .userProfile(UserProfile.builder()
                .givenName(user.getUserProfile().getGivenName())
                .familyName(user.getUserProfile().getFamilyName())
                .createdBy(user.getUserProfile().getCreatedBy())
                .build())
            .build();

        User response = peopleUtil.updateCurrentUser(currentUser, updateUserRequest);

        softAssertions.assertThat(response.getEmail()).isEqualTo(currentUser.getEmail());
        softAssertions.assertThat(response.getUsername()).isEqualTo(currentUser.getUsername());
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17066)
    @Description("Trying to update givenName or FamilyName as empty should give an error")
    public void updateGivenNameOrFamilyNameAsEmptyShouldGiveErrorTest() {
        User user = peopleUtil.getCurrentUser(currentUser);

        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
            .identity(user.getIdentity())
            .username(user.getUsername())
            .email(user.getEmail())
            .active(user.getActive())
            .createdBy(user.getCreatedBy())
            .userProfile(UserProfile.builder()
                .givenName(user.getUserProfile().getGivenName())
                .familyName("")
                .createdBy(user.getUserProfile().getCreatedBy())

                .build())
            .build();

        ErrorResponse response = peopleUtil.updateCurrentUserBadRequest(currentUser, updateUserRequest);

        softAssertions.assertThat(response.getMessage()).isEqualTo(expectedMessageFamilyName);
        softAssertions.assertThat(response.getError()).isEqualTo(badRequest);
        softAssertions.assertAll();

        UpdateUserRequest updateUserRequest2 = UpdateUserRequest.builder()
            .identity(user.getIdentity())
            .username(user.getUsername())
            .email(user.getEmail())
            .active(user.getActive())
            .createdBy(user.getCreatedBy())
            .userProfile(UserProfile.builder()
                .givenName("")
                .familyName(user.getUserProfile().getFamilyName())
                .createdBy(user.getUserProfile().getCreatedBy())
                .build())
            .build();

        ErrorResponse response2 = peopleUtil.updateCurrentUserBadRequest(currentUser, updateUserRequest2);

        softAssertions.assertThat(response2.getMessage()).isEqualTo(expectedMessageGivenName);
        softAssertions.assertThat(response2.getError()).isEqualTo(badRequest);
        softAssertions.assertAll();
    }
}
