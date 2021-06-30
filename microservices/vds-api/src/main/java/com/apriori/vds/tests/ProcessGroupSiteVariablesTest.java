package com.apriori.vds.tests;

import static org.junit.Assert.assertNotEquals;

import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.request.process.group.site.variable.SiteVariableRequest;
import com.apriori.vds.entity.response.process.group.site.variable.SiteVariable;
import com.apriori.vds.entity.response.process.group.site.variable.SiteVariablesItems;
import com.apriori.vds.tests.util.SiteVariableUtil;
import com.apriori.vds.tests.util.VDSRequestEntityUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.List;

public class ProcessGroupSiteVariablesTest extends SiteVariableUtil {

    @AfterClass
    public static void deleteTestingData() {
        siteVariableIdsToDelete.forEach(ProcessGroupSiteVariablesTest::deleteProcessGroupSiteVariableById);
    }

    @Test
    @TestRail(testCaseId = {"8284"})
    @Description("GET a paged set of Site Variables for a specific customer. ")
    public void getSiteVariables() {
        this.getProcessGroupSiteVariablesResponse();
    }

    @Test
    @TestRail(testCaseId = {"8287"})
    @Description("GET a site variable for a customer. ")
    public void getSiteVariablesByIdentity() {
        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_PROCESS_GROUP_SITE_VARIABLE_BY_PG_SITE_IDs, SiteVariable.class)
                .inlineVariables(
                    getProcessGroupIdentity(),
                    this.getFirstProcessGroupSiteVariable().getIdentity()
                );
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTP2Request.build(requestEntity).get().getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"8285"})
    @Description("POST a Site Variable for a customer. The site variable can't already exist. Better to use PUT to create or replace the site variable.")
    public void postSiteVariablesByIdentity() {
        siteVariableIdsToDelete.add(this.postProcessGroupSiteVariables().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"8288"})
    @Description("DELETEs a site variable. ")
    public void deleteSiteVariablesByIdentity() {
        deleteProcessGroupSiteVariableById(this.postProcessGroupSiteVariables().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"8289"})
    @Description("PATCH a Site Variable for a customer. User PUT to create or replace if the site variable does not exist.")
    public void patchSiteVariablesByIdentity() {
        SiteVariable siteVariableBeforeUpdate = this.postProcessGroupSiteVariables();
        siteVariableIdsToDelete.add(siteVariableBeforeUpdate.getIdentity());

        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.PATCH_PROCESS_GROUP_SITE_VARIABLES_BY_PG_SITE_IDs, SiteVariable.class)
                .inlineVariables(getProcessGroupIdentity(), siteVariableBeforeUpdate.getIdentity())
                .body(initUpdateRequestBody(siteVariableBeforeUpdate));

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTP2Request.build(requestEntity).patch();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, updatedSiteVariableResponse.getStatusCode());

        validateUpdatedObject(updatedSiteVariableResponse.getResponseEntity());
    }

    @Test
    @TestRail(testCaseId = {"8286"})
    @Description("PUT Adds or Replaces a CustomAttribute for a user. ")
    public void putCustomAttributeByIdentity() {
        SiteVariable siteVariableBeforeUpdate = this.postProcessGroupSiteVariables();
        siteVariableIdsToDelete.add(siteVariableBeforeUpdate.getIdentity());

        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.PUT_PROCESS_GROUP_SITE_VARIABLE_BY_PG_ID, SiteVariable.class)
                .inlineVariables(getProcessGroupIdentity())
                .body(initUpdateRequestBody(siteVariableBeforeUpdate));

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTP2Request.build(requestEntity).put();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, updatedSiteVariableResponse.getStatusCode());
        validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());
    }

    private static void deleteProcessGroupSiteVariableById(final String identity) {
        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.DELETE_PROCESS_GROUP_SITE_VARIABLE_BY_PG_SITE_IDs, null)
                .inlineVariables(getProcessGroupIdentity(), identity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, HTTP2Request.build(requestEntity).delete().getStatusCode());
    }

    private SiteVariable getFirstProcessGroupSiteVariable() {
        List<SiteVariable> siteVariables = this.getProcessGroupSiteVariablesResponse();
        assertNotEquals("To get Site Variable, response should contain it.", 0, siteVariables.size());

        return siteVariables.get(0);
    }

    private List<SiteVariable> getProcessGroupSiteVariablesResponse() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_PROCESS_GROUP_SITE_VARIABLES_BY_PG_ID, SiteVariablesItems.class)
            .inlineVariables(getProcessGroupIdentity());
        ResponseWrapper<SiteVariablesItems> siteVariablesResponse = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, siteVariablesResponse.getStatusCode());

        return siteVariablesResponse.getResponseEntity().getItems();
    }

    private SiteVariable postProcessGroupSiteVariables() {
        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.POST_PROCESS_GROUP_SITE_VARIABLES_BY_PG_ID, SiteVariable.class)
                .inlineVariables(getProcessGroupIdentity())
                .body(SiteVariableRequest.builder()
                    .name(new GenerateStringUtil().generateSiteName())
                    .type("DOUBLE")
                    .value("bar")
                    .valueType("STRING")
                    .notes("foo bar")
                    .createdBy(this.getFirstProcessGroupSiteVariable().getCreatedBy())
                    .build()
                );

        ResponseWrapper<SiteVariable> siteVariableResponse = HTTP2Request.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, siteVariableResponse.getStatusCode());

        return siteVariableResponse.getResponseEntity();
    }
}