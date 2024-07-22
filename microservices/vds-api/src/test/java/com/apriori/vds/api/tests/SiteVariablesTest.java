package com.apriori.vds.api.tests;

import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.ErrorMessage;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.vds.api.models.request.process.group.site.variable.SiteVariableRequest;
import com.apriori.vds.api.models.response.process.group.site.variable.SiteVariable;
import com.apriori.vds.api.models.response.process.group.site.variable.SiteVariablesItems;
import com.apriori.vds.api.tests.util.SiteVariableUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(TestRulesAPI.class)
public class SiteVariablesTest {
    protected static final Set<String> siteVariableNamesToDelete = new HashSet<>();
    private final SoftAssertions softAssertions = new SoftAssertions();
    private RequestEntityUtil requestEntityUtil;
    private SiteVariableUtil siteVariableUtil;

    @BeforeEach
    public void setup() {
        requestEntityUtil = TestHelper.initUser();
        siteVariableUtil = new SiteVariableUtil(requestEntityUtil);
    }

    @AfterEach
    public void deleteTestingData() {
        siteVariableNamesToDelete.forEach(siteVariable -> siteVariableUtil.deleteSiteVariables(siteVariable));
    }

    @Test
    @TestRail(id = {8294})
    @Description("Deletes a site variable by name.")
    public void deleteSiteVariablesByName() {
        String name = new GenerateStringUtil().generateAlphabeticString("Site", 5);

        SiteVariableRequest siteVariableRequest = SiteVariableRequest.builder()
            .name(name)
            .type("STRING")
            .value("bar")
            .notes("foo bar")
            .createdBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse = siteVariableUtil.putSiteVariables(siteVariableRequest, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableUtil.deleteSiteVariables(updatedSiteVariableResponse.getResponseEntity().getName());
    }

    @Test
    @TestRail(id = {30942})
    @Description("Deletes a SystemVariableMap site variable by identity.")
    public void deleteSystemVariableMapByIdentity() {
        String name = new GenerateStringUtil().generateAlphabeticString("Site", 5);

        SiteVariableRequest siteVariableRequest = SiteVariableRequest.builder()
            .name(name)
            .type("BOOLEAN")
            .value("true")
            .variableType("SystemVariableMap")
            .notes("foo bar")
            .createdBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse = siteVariableUtil.putSiteVariables(siteVariableRequest, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableUtil.deleteSiteVariables(updatedSiteVariableResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(id = {30943})
    @Description("Deletes a SystemConfigurationMap site variable by identity.")
    public void deleteSystemConfigurationMapByIdentity() {
        String name = new GenerateStringUtil().generateAlphabeticString("Site", 5);

        SiteVariableRequest siteVariableRequest = SiteVariableRequest.builder()
            .name(name)
            .type("DOUBLE")
            .value("1.999")
            .variableType("SystemConfigurationMap")
            .notes("foo bar")
            .createdBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse = siteVariableUtil.putSiteVariables(siteVariableRequest, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableUtil.deleteSiteVariables(updatedSiteVariableResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(id = {30944})
    @Description("Deletes a PrimitiveValueMap site variable by identity.")
    public void deletePrimitiveValueMapByIdentity() {
        String name = new GenerateStringUtil().generateAlphabeticString("Site", 5);

        SiteVariableRequest siteVariableRequest = SiteVariableRequest.builder()
            .name(name)
            .type("INTEGER")
            .value("30")
            .variableType("PrimitiveValueMap")
            .processGroupName("Forging")
            .notes("foo bar")
            .createdBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse = siteVariableUtil.putSiteVariables(siteVariableRequest, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableUtil.deleteSiteVariables(updatedSiteVariableResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(id = {30945})
    @Description("Deletes a ProcessModelDefaults site variable by identity.")
    public void deleteProcessModelDefaultsByIdentity() {
        String name = new GenerateStringUtil().generateAlphabeticString("Site", 5);

        SiteVariableRequest siteVariableRequest = SiteVariableRequest.builder()
            .name(name)
            .type("DOUBLE")
            .value("1.999")
            .variableType("ProcessModelDefaults")
            .processGroupName("Sheet Plastic")
            .notes("foo bar")
            .createdBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse = siteVariableUtil.putSiteVariables(siteVariableRequest, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableUtil.deleteSiteVariables(updatedSiteVariableResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(id = {30946})
    @Description("Deletes a ProcessModelOverrides site variable by identity.")
    public void deleteProcessModelOverridesByIdentity() {
        String name = new GenerateStringUtil().generateAlphabeticString("Site", 5);

        SiteVariableRequest siteVariableRequest = SiteVariableRequest.builder()
            .name(name)
            .type("FLOAT")
            .value("1.3")
            .variableType("ProcessModelOverrides")
            .processGroupName("Sheet Metal")
            .notes("foo bar")
            .createdBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse = siteVariableUtil.putSiteVariables(siteVariableRequest, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableUtil.deleteSiteVariables(updatedSiteVariableResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(id = {8292})
    @Description("Creates a site variable for a user with old ETL representation, then finds the created entry and updates data on it.")
    public void putSiteVariableForAUser() {
        String name = new GenerateStringUtil().generateAlphabeticString("Site", 5);

        SiteVariableRequest siteVariableRequest = SiteVariableRequest.builder()
            .name(name)
            .type("STRING")
            .value("bar")
            .notes("foo bar")
            .createdBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse = siteVariableUtil.putSiteVariables(siteVariableRequest, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableUtil.validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());

        SiteVariablesItems updatedSiteVariableResponseGet = siteVariableUtil.getSiteVariable(updatedSiteVariableResponse);

        softAssertions.assertThat(updatedSiteVariableResponseGet.getTotalItemCount()).isGreaterThanOrEqualTo(1);
        softAssertions.assertThat(updatedSiteVariableResponseGet.getItems().get(0));

        SiteVariableRequest siteVariableRequest2 = SiteVariableRequest.builder()
            .name(name)
            .type("STRING")
            .value("UpdatedValue")
            .variableType(null)
            .processGroupName(null)
            .notes("UpdatedNotes")
            .updatedBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse2 = siteVariableUtil.putSiteVariables(siteVariableRequest2, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableNamesToDelete.add(updatedSiteVariableResponse2.getResponseEntity().getIdentity());

        siteVariableUtil.validateUpdatedObject(updatedSiteVariableResponse2.getResponseEntity());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30933, 30686, 30966})
    @Description("Creates a SystemVariableMap site variable for a user with updated ETL representation, then finds the created entry and updates data on it.")
    public void createSystemVariableMapForAUser() {
        String name = new GenerateStringUtil().generateAlphabeticString("Site", 5);
        String variableType = "SystemVariableMap";

        SiteVariableRequest siteVariableRequest = SiteVariableRequest.builder()
            .name(name)
            .type("BOOLEAN")
            .value("true")
            .variableType(variableType)
            .notes("foo bar")
            .createdBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse = siteVariableUtil.putSiteVariables(siteVariableRequest, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableUtil.validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());

        SiteVariablesItems updatedSiteVariableResponseGet = siteVariableUtil.getSiteVariable(updatedSiteVariableResponse);

        softAssertions.assertThat(updatedSiteVariableResponseGet.getTotalItemCount()).isGreaterThanOrEqualTo(1);
        softAssertions.assertThat(updatedSiteVariableResponseGet.getItems().get(0));

        SiteVariableRequest siteVariableRequest2 = SiteVariableRequest.builder()
            .name(name)
            .type("STRING")
            .value("UpdatedValue")
            .variableType(variableType)
            .processGroupName(null)
            .notes("UpdatedNotes")
            .updatedBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse2 = siteVariableUtil.putSiteVariables(siteVariableRequest2, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableNamesToDelete.add(updatedSiteVariableResponse2.getResponseEntity().getIdentity());

        siteVariableUtil.validateUpdatedObject(updatedSiteVariableResponse2.getResponseEntity());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30934, 30687, 30967})
    @Description("Creates a SystemConfigurationMap site variable for a user with updated ETL representation, then finds the created entry and updates data on it.")
    public void createSystemConfigurationMapForAUser() {
        String name = new GenerateStringUtil().generateAlphabeticString("Site", 5);
        String variableType = "SystemConfigurationMap";

        SiteVariableRequest siteVariableRequest = SiteVariableRequest.builder()
            .name(name)
            .type("DOUBLE")
            .value("1.999")
            .variableType("SystemConfigurationMap")
            .notes("foo bar")
            .createdBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse = siteVariableUtil.putSiteVariables(siteVariableRequest, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableUtil.validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());

        SiteVariablesItems updatedSiteVariableResponseGet = siteVariableUtil.getSiteVariable(updatedSiteVariableResponse);

        softAssertions.assertThat(updatedSiteVariableResponseGet.getTotalItemCount()).isGreaterThanOrEqualTo(1);
        softAssertions.assertThat(updatedSiteVariableResponseGet.getItems().get(0));

        SiteVariableRequest siteVariableRequest2 = SiteVariableRequest.builder()
            .name(name)
            .type("STRING")
            .value("UpdatedValue")
            .variableType(variableType)
            .processGroupName(null)
            .notes("UpdatedNotes")
            .updatedBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse2 = siteVariableUtil.putSiteVariables(siteVariableRequest2, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableNamesToDelete.add(updatedSiteVariableResponse2.getResponseEntity().getIdentity());

        siteVariableUtil.validateUpdatedObject(updatedSiteVariableResponse2.getResponseEntity());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30935, 30688, 30968})
    @Description("Creates a PrimitiveValueMap site variable for a user with updated ETL representation, then finds the created entry and updates data on it.")
    public void createPrimitiveValueMapForAUser() {
        String name = new GenerateStringUtil().generateAlphabeticString("Site", 5);
        String variableType = "PrimitiveValueMap";
        String processGroup = "Forging";

        SiteVariableRequest siteVariableRequest = SiteVariableRequest.builder()
            .name(name)
            .type("INTEGER")
            .value("30")
            .variableType(variableType)
            .processGroupName(processGroup)
            .notes("foo bar")
            .createdBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse = siteVariableUtil.putSiteVariables(siteVariableRequest, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableUtil.validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());

        SiteVariablesItems updatedSiteVariableResponseGet = siteVariableUtil.getSiteVariable(updatedSiteVariableResponse);

        softAssertions.assertThat(updatedSiteVariableResponseGet.getTotalItemCount()).isGreaterThanOrEqualTo(1);
        softAssertions.assertThat(updatedSiteVariableResponseGet.getItems().get(0));

        SiteVariableRequest siteVariableRequest2 = SiteVariableRequest.builder()
            .name(name)
            .type("STRING")
            .value("UpdatedValue")
            .variableType(variableType)
            .processGroupName(processGroup)
            .notes("UpdatedNotes")
            .updatedBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse2 = siteVariableUtil.putSiteVariables(siteVariableRequest2, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableNamesToDelete.add(updatedSiteVariableResponse2.getResponseEntity().getIdentity());

        siteVariableUtil.validateUpdatedObject(updatedSiteVariableResponse2.getResponseEntity());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30936, 30689, 30969})
    @Description("Creates a ProcessModelDefaults site variable for a user with updated ETL representation, then finds the created entry and updates data on it.")
    public void createProcessModelDefaultsForAUser() {
        String name = new GenerateStringUtil().generateAlphabeticString("Site", 5);
        String variableType = "ProcessModelDefaults";
        String processGroup = "Sheet Plastic";

        SiteVariableRequest siteVariableRequest = SiteVariableRequest.builder()
            .name(name)
            .type("DOUBLE")
            .value("1.999")
            .variableType(variableType)
            .processGroupName(processGroup)
            .notes("foo bar")
            .createdBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse = siteVariableUtil.putSiteVariables(siteVariableRequest, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableUtil.validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());

        SiteVariablesItems updatedSiteVariableResponseGet = siteVariableUtil.getSiteVariable(updatedSiteVariableResponse);

        softAssertions.assertThat(updatedSiteVariableResponseGet.getTotalItemCount()).isGreaterThanOrEqualTo(1);
        softAssertions.assertThat(updatedSiteVariableResponseGet.getItems().get(0));

        SiteVariableRequest siteVariableRequest2 = SiteVariableRequest.builder()
            .name(name)
            .type("STRING")
            .value("UpdatedValue")
            .variableType(variableType)
            .processGroupName(processGroup)
            .notes("UpdatedNotes")
            .updatedBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse2 = siteVariableUtil.putSiteVariables(siteVariableRequest2, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableNamesToDelete.add(updatedSiteVariableResponse2.getResponseEntity().getIdentity());
        siteVariableUtil.validateUpdatedObject(updatedSiteVariableResponse2.getResponseEntity());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30937, 30690, 30970})
    @Description("Creates a ProcessModelOverrides site variable for a user with updated ETL representation, then finds the created entry and updates data on it.")
    public void createProcessModelOverridesForAUser() {
        String name = new GenerateStringUtil().generateAlphabeticString("Site", 5);
        String variableType = "ProcessModelOverrides";
        String processGroup = "Sheet Metal";

        SiteVariableRequest siteVariableRequest = SiteVariableRequest.builder()
            .name(name)
            .type("FLOAT")
            .value("1.3")
            .variableType(variableType)
            .processGroupName(processGroup)
            .notes("foo bar")
            .createdBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse = siteVariableUtil.putSiteVariables(siteVariableRequest, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableUtil.validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());

        SiteVariablesItems updatedSiteVariableResponseGet = siteVariableUtil.getSiteVariable(updatedSiteVariableResponse);

        softAssertions.assertThat(updatedSiteVariableResponseGet.getTotalItemCount()).isGreaterThanOrEqualTo(1);
        softAssertions.assertThat(updatedSiteVariableResponseGet.getItems().get(0));

        SiteVariableRequest siteVariableRequest2 = SiteVariableRequest.builder()
            .name(name)
            .type("STRING")
            .value("UpdatedValue")
            .variableType(variableType)
            .processGroupName(processGroup)
            .notes("UpdatedNotes")
            .updatedBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse2 = siteVariableUtil.putSiteVariables(siteVariableRequest2, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableNamesToDelete.add(updatedSiteVariableResponse2.getResponseEntity().getIdentity());
        siteVariableUtil.validateUpdatedObject(updatedSiteVariableResponse2.getResponseEntity());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30971})
    @Description("Attempt to update data on a site variable that is not changeable.")
    public void validateSiteVariableFieldsAreNotUpdated() {
        String name = new GenerateStringUtil().generateAlphabeticString("Site", 5);

        SiteVariableRequest siteVariableRequest = SiteVariableRequest.builder()
            .name(name)
            .type("BOOLEAN")
            .value("true")
            .variableType("SystemVariableMap")
            .notes("foo bar")
            .createdBy("#SYSTEM00000")
            .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse = siteVariableUtil.putSiteVariables(siteVariableRequest, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableUtil.validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());

        SiteVariableRequest updatedSiteVariableRequest =
            SiteVariableRequest.builder()
                .name(name)
                .type("STRING")
                .value("UpdatedValue")
                .variableType("SystemVariableMap")
                .notes("UpdatedNotes")
                .updatedBy("#SYSTEM00000")
                .updatedAt(LocalDateTime.parse("2007-12-03T10:15:30"))
                .createdBy("#NOTSYSTEM00")
                .createdAt(LocalDateTime.parse("2007-12-03T10:15:30"))
                .build();

        ResponseWrapper<SiteVariable> updatedSiteVariableResponse2 = siteVariableUtil.putSiteVariables(updatedSiteVariableRequest, HttpStatus.SC_CREATED, SiteVariable.class);

        siteVariableNamesToDelete.add(updatedSiteVariableResponse2.getResponseEntity().getIdentity());
        siteVariableUtil.validateUpdatedObject(updatedSiteVariableResponse2.getResponseEntity());

        softAssertions.assertThat(updatedSiteVariableResponse2.getResponseEntity().getUpdatedAt())
            .isEqualTo(updatedSiteVariableResponse.getResponseEntity().getUpdatedAt());
        softAssertions.assertThat(updatedSiteVariableResponse2.getResponseEntity().getCreatedBy())
            .isEqualTo(updatedSiteVariableResponse.getResponseEntity().getCreatedBy());
        softAssertions.assertThat(updatedSiteVariableResponse2.getResponseEntity().getCreatedAt())
            .isEqualTo(updatedSiteVariableResponse.getResponseEntity().getCreatedAt());
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30938})
    @Description("Attempt to create a site variable with empty name.")
    public void emptyNameSiteVariableError() {
        SiteVariableRequest siteVariableRequest = SiteVariableRequest.builder()
            .name("")
            .type("STRING")
            .value("yes")
            .variableType("PrimitiveValueMap")
            .processGroupName("Sheet Plastic")
            .notes("foo bar")
            .createdBy("#SYSTEM00000")
            .build();

        siteVariableUtil.putSiteVariables(siteVariableRequest, HttpStatus.SC_BAD_REQUEST, ErrorMessage.class);
    }

    @Test
    @TestRail(id = {30939})
    @Description("Attempt to create a site variable with invalid type.")
    public void invalidTypeSiteVariableError() {
        SiteVariableRequest siteVariableRequest = SiteVariableRequest.builder()
            .name(new GenerateStringUtil().generateAlphabeticString("Site", 5))
            .type("BLUE")
            .value("yes")
            .variableType("ProcessModelDefaults")
            .processGroupName("Sheet Plastic")
            .notes("foo bar")
            .createdBy("#SYSTEM00000")
            .build();

        siteVariableUtil.putSiteVariables(siteVariableRequest, HttpStatus.SC_BAD_REQUEST, ErrorMessage.class);
    }

    @Test
    @TestRail(id = {30939})
    @Description("Attempt to create a site variable with empty createdBy.")
    public void emptyCreatedBySiteVariableError() {
        SiteVariableRequest siteVariableRequest = SiteVariableRequest.builder()
            .name(new GenerateStringUtil().generateAlphabeticString("Site", 5))
            .type("STRING")
            .value("yes")
            .variableType("ProcessModelOverrides")
            .processGroupName("Sheet Plastic")
            .notes("foo bar")
            .createdBy("")
            .build();

        siteVariableUtil.putSiteVariables(siteVariableRequest, HttpStatus.SC_BAD_REQUEST, ErrorMessage.class);
    }
}
