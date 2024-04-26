package com.apriori.vds.api.tests;

import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.ErrorMessage;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.vds.api.enums.VDSAPIEnum;
import com.apriori.vds.api.models.request.process.group.site.variable.SiteVariableRequest;
import com.apriori.vds.api.models.response.process.group.site.variable.SiteVariable;
import com.apriori.vds.api.models.response.process.group.site.variable.SiteVariablesItems;
import com.apriori.vds.api.tests.util.SiteVariableUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(TestRulesAPI.class)
public class SiteVariablesTest extends SiteVariableUtil {
    protected static final Set<String> siteVariableNamesToDelete = new HashSet<>();
    private final SoftAssertions softAssertions = new SoftAssertions();

    @AfterAll
    public static void deleteTestingData() {
        siteVariableNamesToDelete.forEach(SiteVariablesTest::deleteSiteVariablesByName);
    }

    @Test
    @TestRail(id = {8294})
    @Description("DELETEs a site variable by name.")
    public void deleteSiteVariablesByName() {
        deleteSiteVariablesByName(this.postSiteVariables().getName());
    }

    @Test
    @TestRail(id = {30942})
    @Description("DELETEs a SystemVariableMap site variable by identity.")
    public void deleteSystemVariableMapByIdentity() {
        deleteSiteVariablesByName(this.postSystemVariableMapSiteVariable().getIdentity());
    }

    @Test
    @TestRail(id = {30943})
    @Description("DELETEs a SystemConfigurationMap site variable by identity.")
    public void deleteSystemConfigurationMapByIdentity() {
        deleteSiteVariablesByName(this.postSystemConfigurationMapSiteVariable().getIdentity());
    }

    @Test
    @TestRail(id = {30944})
    @Description("DELETEs a PrimitiveValueMap site variable by identity.")
    public void deletePrimitiveValueMapByIdentity() {
        deleteSiteVariablesByName(this.postPrimitiveValueMapSiteVariable().getIdentity());
    }

    @Test
    @TestRail(id = {30945})
    @Description("DELETEs a ProcessModelDefaults site variable by identity.")
    public void deleteProcessModelDefaultsByIdentity() {
        deleteSiteVariablesByName(this.postProcessModelDefaultsSiteVariable().getIdentity());
    }

    @Test
    @TestRail(id = {30946})
    @Description("DELETEs a ProcessModelOverrides site variable by identity.")
    public void deleteProcessModelOverridesByIdentity() {
        deleteSiteVariablesByName(this.postProcessModelOverridesSiteVariable().getIdentity());
    }

    @Test
    @TestRail(id = {8292})
    @Description("Adds or Replaces a SiteVariable for a user with old ETL representation.")
    public void putSiteVariableForAUser() {
        SiteVariable siteVariable = postSiteVariables();

        siteVariableNamesToDelete.add(siteVariable.getName());

        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_SITE_VARIABLES, SiteVariable.class)
                .body(initUpdateRequestBody(siteVariable))
                .expectedResponseCode(HttpStatus.SC_CREATED);

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTPRequest.build(requestEntity).put();

        SiteVariableUtil.validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());
    }

    @Test
    @TestRail(id = {30933, 30686})
    @Description("Creates a SystemVariableMap SiteVariable for a user with updated ETL representation, and then finds the created entry.")
    public void createSystemVariableMapForAUser() {
        SiteVariable siteVariable = postSystemVariableMapSiteVariable();

        siteVariableNamesToDelete.add(siteVariable.getIdentity());

        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_SITE_VARIABLES, SiteVariable.class)
                .body(initUpdateRequestBody(siteVariable))
                .expectedResponseCode(HttpStatus.SC_CREATED);

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTPRequest.build(requestEntity).put();

        SiteVariableUtil.validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());

        RequestEntity requestEntityGet =
            requestEntityUtil.init(VDSAPIEnum.GET_SITE_VARIABLES, SiteVariablesItems.class)
                .queryParams(new QueryParams().use("identity[EQ]", updatedSiteVariableResponse.getResponseEntity().getIdentity()))
                .expectedResponseCode(HttpStatus.SC_OK);

        final ResponseWrapper<SiteVariablesItems> updatedSiteVariableResponseGet = HTTPRequest.build(requestEntityGet).get();

        softAssertions.assertThat(updatedSiteVariableResponseGet.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        softAssertions.assertThat(updatedSiteVariableResponseGet.getResponseEntity().getItems().get(0));
    }

    @Test
    @TestRail(id = {30934, 30687})
    @Description("Creates a SystemConfigurationMap SiteVariable for a user with updated ETL representation, and then finds the created entry.")
    public void createSystemConfigurationMapForAUser() {
        SiteVariable siteVariable = postSystemConfigurationMapSiteVariable();

        siteVariableNamesToDelete.add(siteVariable.getIdentity());

        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_SITE_VARIABLES, SiteVariable.class)
                .body(initUpdateRequestBody(siteVariable))
                .expectedResponseCode(HttpStatus.SC_CREATED);

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTPRequest.build(requestEntity).put();

        SiteVariableUtil.validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());

        RequestEntity requestEntityGet =
            requestEntityUtil.init(VDSAPIEnum.GET_SITE_VARIABLES, SiteVariablesItems.class)
                .queryParams(new QueryParams().use("identity[EQ]", updatedSiteVariableResponse.getResponseEntity().getIdentity()))
                .expectedResponseCode(HttpStatus.SC_OK);

        final ResponseWrapper<SiteVariablesItems> updatedSiteVariableResponseGet = HTTPRequest.build(requestEntityGet).get();

        softAssertions.assertThat(updatedSiteVariableResponseGet.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        softAssertions.assertThat(updatedSiteVariableResponseGet.getResponseEntity().getItems().get(0));
    }

    @Test
    @TestRail(id = {30935, 30688})
    @Description("Creates a PrimitiveValueMap SiteVariable for a user with updated ETL representation, and then finds the created entry.")
    public void createPrimitiveValueMapForAUser() {
        SiteVariable siteVariable = postPrimitiveValueMapSiteVariable();

        siteVariableNamesToDelete.add(siteVariable.getIdentity());

        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_SITE_VARIABLES, SiteVariable.class)
                .body(initUpdateRequestBody(siteVariable))
                .expectedResponseCode(HttpStatus.SC_CREATED);

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTPRequest.build(requestEntity).put();

        SiteVariableUtil.validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());

        RequestEntity requestEntityGet =
            requestEntityUtil.init(VDSAPIEnum.GET_SITE_VARIABLES, SiteVariablesItems.class)
                .queryParams(new QueryParams().use("identity[EQ]", updatedSiteVariableResponse.getResponseEntity().getIdentity()))
                .expectedResponseCode(HttpStatus.SC_OK);

        final ResponseWrapper<SiteVariablesItems> updatedSiteVariableResponseGet = HTTPRequest.build(requestEntityGet).get();

        softAssertions.assertThat(updatedSiteVariableResponseGet.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        softAssertions.assertThat(updatedSiteVariableResponseGet.getResponseEntity().getItems().get(0));
    }

    @Test
    @TestRail(id = {30936, 30689})
    @Description("Creates a ProcessModelDefaults SiteVariable for a user with updated ETL representation, and then finds the created entry.")
    public void createProcessModelDefaultsForAUser() {
        SiteVariable siteVariable = postProcessModelDefaultsSiteVariable();

        siteVariableNamesToDelete.add(siteVariable.getIdentity());

        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_SITE_VARIABLES, SiteVariable.class)
                .body(initUpdateRequestBody(siteVariable))
                .expectedResponseCode(HttpStatus.SC_CREATED);

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTPRequest.build(requestEntity).put();

        SiteVariableUtil.validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());

        RequestEntity requestEntityGet =
            requestEntityUtil.init(VDSAPIEnum.GET_SITE_VARIABLES, SiteVariablesItems.class)
                .queryParams(new QueryParams().use("identity[EQ]", updatedSiteVariableResponse.getResponseEntity().getIdentity()))
                .expectedResponseCode(HttpStatus.SC_OK);

        final ResponseWrapper<SiteVariablesItems> updatedSiteVariableResponseGet = HTTPRequest.build(requestEntityGet).get();

        softAssertions.assertThat(updatedSiteVariableResponseGet.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        softAssertions.assertThat(updatedSiteVariableResponseGet.getResponseEntity().getItems().get(0));
    }

    @Test
    @TestRail(id = {30937, 30690})
    @Description("Creates a ProcessModelOverrides SiteVariable for a user with updated ETL representation, and then finds the created entry.")
    public void createProcessModelOverridesForAUser() {
        SiteVariable siteVariable = postProcessModelOverridesSiteVariable();

        siteVariableNamesToDelete.add(siteVariable.getIdentity());

        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_SITE_VARIABLES, SiteVariable.class)
                .body(initUpdateRequestBody(siteVariable))
                .expectedResponseCode(HttpStatus.SC_CREATED);

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTPRequest.build(requestEntity).put();

        SiteVariableUtil.validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());

        RequestEntity requestEntityGet =
            requestEntityUtil.init(VDSAPIEnum.GET_SITE_VARIABLES, SiteVariablesItems.class)
                .queryParams(new QueryParams().use("identity[EQ]", updatedSiteVariableResponse.getResponseEntity().getIdentity()))
                .expectedResponseCode(HttpStatus.SC_OK);

        final ResponseWrapper<SiteVariablesItems> updatedSiteVariableResponseGet = HTTPRequest.build(requestEntityGet).get();

        softAssertions.assertThat(updatedSiteVariableResponseGet.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        softAssertions.assertThat(updatedSiteVariableResponseGet.getResponseEntity().getItems().get(0));
    }

    @Test
    @TestRail(id = {30938})
    @Description("Attempt to create a SiteVariable with empty name.")
    public void emptyNameSiteVariableError() {

        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_SITE_VARIABLES, ErrorMessage.class)
                .body(SiteVariableRequest.builder()
                    .name("")
                    .type("STRING")
                    .value("yes")
                    .variableType("PrimitiveValueMap")
                    .processGroupName("Sheet Plastic")
                    .notes("foo bar")
                    .createdBy("#SYSTEM00000")
                    .build()
                )
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<ErrorMessage> responseWrapper = HTTPRequest.build(requestEntity).put();
        responseWrapper.getResponseEntity();
    }

    @Test
    @TestRail(id = {30939})
    @Description("Attempt to create a SiteVariable with invalid type.")
    public void invalidTypeSiteVariableError() {

        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_SITE_VARIABLES, ErrorMessage.class)
                .body(SiteVariableRequest.builder()
                    .name(new GenerateStringUtil().generateSiteName())
                    .type("BLUE")
                    .value("yes")
                    .variableType("ProcessModelDefaults")
                    .processGroupName("Sheet Plastic")
                    .notes("foo bar")
                    .createdBy("#SYSTEM00000")
                    .build()
                )
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<ErrorMessage> responseWrapper = HTTPRequest.build(requestEntity).put();
        responseWrapper.getResponseEntity();
    }

    @Test
    @TestRail(id = {30939})
    @Description("Attempt to create a SiteVariable with empty createdBy.")
    public void emptyCreatedBySiteVariableError() {

        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_SITE_VARIABLES, ErrorMessage.class)
                .body(SiteVariableRequest.builder()
                    .name(new GenerateStringUtil().generateSiteName())
                    .type("STRING")
                    .value("yes")
                    .variableType("ProcessModelOverrides")
                    .processGroupName("Sheet Plastic")
                    .notes("foo bar")
                    .createdBy("")
                    .build()
                )
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<ErrorMessage> responseWrapper = HTTPRequest.build(requestEntity).put();
        responseWrapper.getResponseEntity();
    }

    private static void deleteSiteVariablesByName(final String name) {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.DELETE_SITE_VARIABLE_BY_ID, null)
                .inlineVariables(name)
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }

    private SiteVariable postSiteVariables() {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_SITE_VARIABLES, SiteVariable.class)
                .body(SiteVariableRequest.builder()
                    .name(new GenerateStringUtil().generateSiteName())
                    .type("STRING")
                    .value("bar")
                    .notes("foo bar")
                    .createdBy("#SYSTEM00000")
                    .build()
                )
                .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<SiteVariable> siteVariableResponse = HTTPRequest.build(requestEntity).put();

        return siteVariableResponse.getResponseEntity();
    }

    private SiteVariable postSystemVariableMapSiteVariable() {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_SITE_VARIABLES, SiteVariable.class)
                .body(SiteVariableRequest.builder()
                    .name(new GenerateStringUtil().generateSiteName())
                    .type("BOOLEAN")
                    .value("true")
                    .variableType("SystemVariableMap")
                    .notes("foo bar")
                    .createdBy("#SYSTEM00000")
                    .build()
                )
                .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<SiteVariable> siteVariableResponse = HTTPRequest.build(requestEntity).put();

        return siteVariableResponse.getResponseEntity();
    }

    private SiteVariable postSystemConfigurationMapSiteVariable() {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_SITE_VARIABLES, SiteVariable.class)
                .body(SiteVariableRequest.builder()
                    .name(new GenerateStringUtil().generateSiteName())
                    .type("DOUBLE")
                    .value("1.999")
                    .variableType("SystemConfigurationMap")
                    .notes("foo bar")
                    .createdBy("#SYSTEM00000")
                    .build()
                )
                .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<SiteVariable> siteVariableResponse = HTTPRequest.build(requestEntity).put();

        return siteVariableResponse.getResponseEntity();
    }

    private SiteVariable postPrimitiveValueMapSiteVariable() {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_SITE_VARIABLES, SiteVariable.class)
                .body(SiteVariableRequest.builder()
                    .name(new GenerateStringUtil().generateSiteName())
                    .type("INTEGER")
                    .value("30")
                    .variableType("PrimitiveValueMap")
                    .processGroupName("Forging")
                    .notes("foo bar")
                    .createdBy("#SYSTEM00000")
                    .build()
                )
                .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<SiteVariable> siteVariableResponse = HTTPRequest.build(requestEntity).put();

        return siteVariableResponse.getResponseEntity();
    }

    private SiteVariable postProcessModelDefaultsSiteVariable() {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_SITE_VARIABLES, SiteVariable.class)
                .body(SiteVariableRequest.builder()
                    .name(new GenerateStringUtil().generateSiteName())
                    .type("STRING")
                    .value("yes")
                    .variableType("ProcessModelDefaults")
                    .processGroupName("Sheet Plastic")
                    .notes("foo bar")
                    .createdBy("#SYSTEM00000")
                    .build()
                )
                .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<SiteVariable> siteVariableResponse = HTTPRequest.build(requestEntity).put();

        return siteVariableResponse.getResponseEntity();
    }

    private SiteVariable postProcessModelOverridesSiteVariable() {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_SITE_VARIABLES, SiteVariable.class)
                .body(SiteVariableRequest.builder()
                    .name(new GenerateStringUtil().generateSiteName())
                    .type("STRING")
                    .value("no")
                    .variableType("ProcessModelOverrides")
                    .processGroupName("Sheet Plastic")
                    .notes("foo bar")
                    .createdBy("#SYSTEM00000")
                    .build()
                )
                .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<SiteVariable> siteVariableResponse = HTTPRequest.build(requestEntity).put();

        return siteVariableResponse.getResponseEntity();
    }
}
