package com.apriori.vds.tests;

import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.request.process.group.site.variable.SiteVariableRequest;
import com.apriori.vds.entity.response.process.group.site.variable.SiteVariable;
import com.apriori.vds.entity.response.process.group.site.variable.SiteVariablesItems;
import com.apriori.vds.tests.util.SiteVariableUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SiteVariablesTest extends SiteVariableUtil {
    protected static final Set<String> siteVariableIdsToDelete = new HashSet<>();

    @AfterClass
    public static void deleteTestingData() {
        siteVariableIdsToDelete.forEach(SiteVariablesTest::deleteSiteVariableById);
    }

    @Test
    @TestRail(testCaseId = {"8290"})
    @Description("Returns a paged set of Site Variables for a specific customer.")
    public void getSiteVariables() {
        this.getSiteVariablesResponse();
    }

    @Test
    @TestRail(testCaseId = {"8293"})
    @Description("Get a site variable for a customer.")
    public void getSiteVariablesByIdentity() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.GET_SITE_VARIABLE_BY_ID, SiteVariable.class)
                .inlineVariables(
                    this.getFirstSiteVariable().getIdentity()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(testCaseId = {"8291"})
    @Description("Adds a Site Variable for a customer. The site variable can't already exist. Better to use PUT to create or replace the site variable.")
    public void postSiteVariablesByIdentity() {
        siteVariableIdsToDelete.add(this.postSiteVariables().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"8294"})
    @Description("DELETEs a site variable.")
    public void deleteSiteVariablesByIdentity() {
        deleteSiteVariableById(this.postSiteVariables().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"8295"})
    @Description("Updates a Site Variable for a customer. User PUT to create or replace if the site variable does not exist.")
    public void patchSiteVariablesByIdentity() {
        SiteVariable siteVariableBeforeUpdate = this.postSiteVariables();
        siteVariableIdsToDelete.add(siteVariableBeforeUpdate.getIdentity());

        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.PATCH_SITE_VARIABLES_BY_ID, SiteVariable.class)
                .inlineVariables(siteVariableBeforeUpdate.getIdentity())
                .body(initUpdateRequestBody(siteVariableBeforeUpdate))
                .expectedResponseCode(HttpStatus.SC_CREATED);

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTPRequest.build(requestEntity).patch();

        validateUpdatedObject(updatedSiteVariableResponse.getResponseEntity());
    }

    @Test
    @TestRail(testCaseId = {"8292"})
    @Description("Adds or Replaces a SiteVariable for a user.")
    public void putSiteVariableForAUser() {
        SiteVariable siteVariable = getFirstSiteVariable();

        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.PUT_SITE_VARIABLES, SiteVariable.class)
                .body(initUpdateRequestBody(siteVariable))
                .expectedResponseCode(HttpStatus.SC_CREATED);

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTPRequest.build(requestEntity).put();

        validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());
    }

    private static void deleteSiteVariableById(final String identity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.DELETE_SITE_VARIABLE_BY_ID, null)
                .inlineVariables(identity)
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }

    private SiteVariable getFirstSiteVariable() {
        List<SiteVariable> siteVariables = this.getSiteVariablesResponse();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(siteVariables.size()).isNotZero();
        softAssertions.assertAll();

        return siteVariables.get(0);
    }

    private List<SiteVariable> getSiteVariablesResponse() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_SITE_VARIABLES, SiteVariablesItems.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<SiteVariablesItems> siteVariablesResponse = HTTPRequest.build(requestEntity).get();

        return siteVariablesResponse.getResponseEntity().getItems();
    }

    private SiteVariable postSiteVariables() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.POST_SITE_VARIABLE, SiteVariable.class)
                .body(SiteVariableRequest.builder()
                    .name(new GenerateStringUtil().generateSiteName())
                    .type("DOUBLE")
                    .value("bar")
                    .valueType("STRING")
                    .notes("foo bar")
                    .createdBy(this.getFirstSiteVariable().getCreatedBy())
                    .build()
                )
                .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<SiteVariable> siteVariableResponse = HTTPRequest.build(requestEntity).post();

        return siteVariableResponse.getResponseEntity();
    }
}
