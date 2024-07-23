package com.apriori.acs.api.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class ArtifactTableInfoTests extends TestUtil {
    private AcsResources acsResources;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        acsResources = new AcsResources(requestEntityUtil);
    }

    @Test
    @TestRail(id = 12062)
    @Description("Test Get Artifact Table Info")
    public void testGetArtifactTableInfo() {
        ArtifactTableInfoResponse getArtifactTableInfoResponse = acsResources.getArtifactTableInfo();

        String expectedName = "SimpleHole";
        assertThat(getArtifactTableInfoResponse.getTypeKey().getSecond(), is(equalTo(expectedName)));
        assertThat(getArtifactTableInfoResponse.getTypeKey().getName(), is(equalTo(expectedName)));
        assertThat(getArtifactTableInfoResponse.getName(), is(equalTo(expectedName)));
    }

    @Test
    @TestRail(id = 12070)
    @Description("Test Get Artifact Table Info - Negative - Invalid Process Group")
    public void testGetArtifactTableInfoNegativeInvalidProcessGroup() {
        GenericErrorResponse genericErrorResponse = acsResources.getArtifactTableInfoInvalidProcessGroup();

        assertThat(genericErrorResponse.getErrorCode(), is(equalTo(HttpStatus.SC_NOT_FOUND)));
        assertThat(genericErrorResponse.getErrorMessage(), is(equalTo("Unknown process group: Sheet Metals")));
    }
}
