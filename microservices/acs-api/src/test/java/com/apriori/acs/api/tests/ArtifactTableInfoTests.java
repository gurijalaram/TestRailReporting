package com.apriori.acs.api.tests;

import com.apriori.acs.api.models.response.acs.artifacttableinfo.ArtifactTableInfoResponse;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class ArtifactTableInfoTests extends TestUtil {
    private SoftAssertions softAssertions;
    private AcsResources acsResources;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        acsResources = new AcsResources(requestEntityUtil);
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = 12062)
    @Description("Test Get Artifact Table Info")
    public void testGetArtifactTableInfo() {
        ArtifactTableInfoResponse getArtifactTableInfoResponse = acsResources.getArtifactTableInfo();

        String expectedName = "SimpleHole";
        softAssertions.assertThat(getArtifactTableInfoResponse.getTypeKey().getSecond()).isEqualTo(expectedName);
        softAssertions.assertThat(getArtifactTableInfoResponse.getTypeKey().getName()).isEqualTo(expectedName);
        softAssertions.assertThat(getArtifactTableInfoResponse.getName()).isEqualTo(expectedName);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 12070)
    @Description("Test Get Artifact Table Info - Negative - Invalid Process Group")
    public void testGetArtifactTableInfoNegativeInvalidProcessGroup() {
        GenericErrorResponse genericErrorResponse = acsResources.getArtifactTableInfoInvalidProcessGroup();

        softAssertions.assertThat(genericErrorResponse.getErrorCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
        softAssertions.assertThat(genericErrorResponse.getErrorMessage()).isEqualTo("Unknown process group: Sheet Metals");
        softAssertions.assertAll();
    }
}
