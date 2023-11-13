package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
class CdsUpdateUserTests {

    private final GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private final SoftAssertions soft = new SoftAssertions();
    private final CdsTestUtil cdsTestUtil = new CdsTestUtil();

    private final List<User> createdUsers = new ArrayList<>();

    User generateTargetUser() {

        String userName = generateStringUtil.generateUserName();

        Customer aprioriInternal = cdsTestUtil.getAprioriInternal();
        String pattern = aprioriInternal.getEmailRegexPatterns().stream().findFirst().orElseThrow();
        String domain = pattern.replace("\\S+@", "").replace(".com", "");

        ResponseWrapper<User> added = cdsTestUtil.addUser(
            aprioriInternal.getIdentity(),
            userName,
            domain
        );

        User created = added.getResponseEntity();
        createdUsers.add(created);

        return created;
    }

    ObjectNode createEnablementsNode() {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode enablements = mapper.createObjectNode();
        enablements.set("customerAssignedRole", new TextNode("APRIORI_EXPERT"));
        enablements.set("userAdminEnabled", BooleanNode.TRUE);
        return enablements;
    }

    ObjectNode createProfileNode() {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode userProfile = mapper.createObjectNode();
        userProfile.set("familyName", new TextNode("Allen"));
        userProfile.set("givenName", new TextNode("Barry"));
        return userProfile;
    }

    ObjectNode createUserNode(ObjectNode profile, ObjectNode enablements) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();

        if (profile != null) {
            user.set("userProfile", profile);
        }

        if (enablements != null) {
            user.set("enablements", enablements);
        }

        return user;
    }

    @BeforeEach()
    void setUp() {

        createdUsers.clear();
    }

    @AfterEach()
    void cleanup() {

        createdUsers.forEach(u -> cdsTestUtil.delete(
            CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS,
            u.getCustomerIdentity(),
            u.getIdentity()
        ));
    }

    @Test
    @TestRail(id = 29190)
    @Description("API updates user enablements")
    void patch_shouldSetEnablementsAlone() {

        ObjectNode enablements = createEnablementsNode();
        ObjectNode user = createUserNode(null, enablements);
        User current = generateTargetUser();

        User actual = cdsTestUtil.patchUser(
            current.getCustomerIdentity(),
            current.getIdentity(),
            user
        ).getResponseEntity();

        soft
            .assertThat(actual.getEnablements().getCustomerAssignedRole())
            .isEqualTo(enablements.get("customerAssignedRole").asText());
        soft
            .assertThat(actual.getEnablements().getUserAdminEnabled())
            .isTrue();
        soft.assertAll();
    }

    @Test
    @TestRail(id = 29189)
    @Description("API updates user profile")
    void patch_shouldSetProfileInformationAlone() {

        ObjectNode profile = createProfileNode();
        ObjectNode user = createUserNode(profile, null);
        User current = generateTargetUser();

        User actual = cdsTestUtil.patchUser(
            current.getCustomerIdentity(),
            current.getIdentity(),
            user
        ).getResponseEntity();

        soft.assertThat(actual.getUserProfile().getFamilyName()).isEqualTo(profile.get("familyName").asText());
        soft.assertThat(actual.getUserProfile().getGivenName()).isEqualTo(profile.get("givenName").asText());
        soft.assertAll();
    }

    @Test
    @TestRail(id = 29191)
    @Description("API updates user profile and enablements")
    void patch_ShouldUpdateProfileAndEnablements() {

        ObjectNode enablements = createEnablementsNode();
        ObjectNode profile = createProfileNode();
        ObjectNode user = createUserNode(profile, enablements);
        User current = generateTargetUser();

        User actual = cdsTestUtil.patchUser(
            current.getCustomerIdentity(),
            current.getIdentity(),
            user
        ).getResponseEntity();

        soft
            .assertThat(actual.getEnablements().getCustomerAssignedRole())
            .isEqualTo(enablements.get("customerAssignedRole").asText());
        soft
            .assertThat(actual.getEnablements().getUserAdminEnabled())
            .isTrue();
        soft.assertThat(actual.getUserProfile().getFamilyName()).isEqualTo(profile.get("familyName").asText());
        soft.assertThat(actual.getUserProfile().getGivenName()).isEqualTo(profile.get("givenName").asText());
        soft.assertAll();
    }
}
