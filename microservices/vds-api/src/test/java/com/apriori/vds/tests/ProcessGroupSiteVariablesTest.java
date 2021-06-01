package com.apriori.vds.tests;

import static org.junit.Assert.assertEquals;
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
import com.apriori.vds.tests.util.VDSRequestEntityUtil;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProcessGroupSiteVariablesTest extends VDSTestUtil {

    private static List<String> siteVariableIdsToDelete = new ArrayList<>();

    @AfterClass
    public static void removeTestingData() {
        siteVariableIdsToDelete.forEach(ProcessGroupSiteVariablesTest::deleteSiteVariableById);
    }

    @Test
    @TestRail(testCaseId = {"8284"})
    @Description("GET a paged set of Site Variables for a specific customer. ")
    public void getSiteVariables() {
        this.getSiteVariablesResponse();
    }

    @Test
    @TestRail(testCaseId = {"8287"})
    @Description("GET a site variable for a customer. ")
    public void getSiteVariablesByIdentity() {

        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_PROCESS_GROUP_SITE_VARIABLE_BY_PG_SITE_IDs, SiteVariable.class)
                .inlineVariables(Arrays.asList(
                    getProcessGroupIdentity(),
                    this.getFirstSiteVariable().getIdentity()
                ));
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTP2Request.build(requestEntity).get().getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"8285"})
    @Description("POST a Site Variable for a customer. The site variable can't already exist. Better to use PUT to create or replace the site variable.")
    public void postSiteVariablesByIdentity() {
        siteVariableIdsToDelete.add(this.postSiteVariables().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"8288"})
    @Description("DELETEs a site variable. ")
    public void deleteSiteVariablesByIdentity() {
        deleteSiteVariableById(this.postSiteVariables().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"8289"})
    @Description("PATCH a Site Variable for a customer. User PUT to create or replace if the site variable does not exist.")
    public void updateSiteVariablesByIdentity() {
        final String updatedName = new GenerateStringUtil().generateSiteName();
        final String updatedValue = "UpdatedValue";
        final String updatedNotes = "UpdatedNotes";

        SiteVariable siteVariableBeforeUpdate = this.postSiteVariables();
        siteVariableIdsToDelete.add(siteVariableBeforeUpdate.getIdentity());

        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.PATCH_PROCESS_GROUP_SITE_VARIABLES_BY_PG_SITE_IDs, SiteVariable.class)
                .inlineVariables(Arrays.asList(getProcessGroupIdentity(), siteVariableBeforeUpdate.getIdentity()))
                .body(SiteVariableRequest.builder()
                    .name(updatedName)
                    .value(updatedValue)
                    .notes(updatedNotes)
                    .updatedBy(siteVariableBeforeUpdate.getCreatedBy())
                    .createdBy(siteVariableBeforeUpdate.getCreatedBy())
                    .build()
                );

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTP2Request.build(requestEntity).patch();
        final SiteVariable updatedSiteVariable = updatedSiteVariableResponse.getResponseEntity();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, updatedSiteVariableResponse.getStatusCode());

        assertNotEquals("The name should not be updated.", updatedName, updatedSiteVariable.getName());

        assertEquals("The value should be updated.", updatedValue, updatedSiteVariable.getValue());
        assertEquals("Notes should be updated.", updatedNotes, updatedSiteVariable.getNotes());
    }

    @Test
    @TestRail(testCaseId = {"8286"})
    @Description("PUT Adds or Replaces a CustomAttribute for a user. ")
    public void updateCustomAttributeByIdentity() {
        final String updatedName = new GenerateStringUtil().generateSiteName();
        final String updatedValue = "UpdatedValue";
        final String updatedNotes = "UpdatedNotes";

        SiteVariable siteVariableBeforeUpdate = this.postSiteVariables();
        siteVariableIdsToDelete.add(siteVariableBeforeUpdate.getIdentity());

        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.PUT_PROCESS_GROUP_SITE_VARIABLE_BY_PG_ID, SiteVariable.class)
                .inlineVariables(Arrays.asList(getProcessGroupIdentity()))
                .body(SiteVariableRequest.builder()
                        .name(updatedName)
                        .value(updatedValue)
                        .notes(updatedNotes)
                        .customerIdentity(siteVariableBeforeUpdate.getCustomerIdentity())
                        .createdBy(siteVariableBeforeUpdate.getCreatedBy())
                        .build()
                );

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTP2Request.build(requestEntity).put();
        final SiteVariable updatedSiteVariable = updatedSiteVariableResponse.getResponseEntity();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, updatedSiteVariableResponse.getStatusCode());

        assertNotEquals("The name should not be updated.", updatedName, updatedSiteVariable.getName());

        assertEquals("The value should be updated.", updatedValue, updatedSiteVariable.getValue());
        assertEquals("Notes should be updated.", updatedNotes, updatedSiteVariable.getNotes());
    }

    private static void deleteSiteVariableById(final String identity) {
        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.DELETE_PROCESS_GROUP_SITE_VARIABLE_BY_PG_SITE_IDs, null)
                .inlineVariables(Arrays.asList(getProcessGroupIdentity(), identity));
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, HTTP2Request.build(requestEntity).delete().getStatusCode());
    }

    private SiteVariable getFirstSiteVariable() {
        List<SiteVariable> siteVariables = this.getSiteVariablesResponse();
        assertNotEquals("To get Site Variable, response should contain it.", 0, siteVariables.size());

        return siteVariables.get(0);
    }

    private List<SiteVariable> getSiteVariablesResponse() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_PROCESS_GROUP_SITE_VARIABLES_BY_PG_ID, SiteVariablesItems.class)
            .inlineVariables(Collections.singletonList(getProcessGroupIdentity()));
        ResponseWrapper<SiteVariablesItems> siteVariablesResponse = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, siteVariablesResponse.getStatusCode());

        return siteVariablesResponse.getResponseEntity().getItems();
    }

    private SiteVariable postSiteVariables() {
        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.POST_PROCESS_GROUP_SITE_VARIABLES_BY_PG_ID, SiteVariable.class)
                .inlineVariables(Collections.singletonList(getProcessGroupIdentity()))
                .body(SiteVariableRequest.builder()
                    .name(new GenerateStringUtil().generateSiteName())
                    .type("DOUBLE")
                    .value("bar")
                    .valueType("STRING")
                    .notes("foo bar")
                    .createdBy(this.getFirstSiteVariable().getCreatedBy())
                    .build()
                );

        ResponseWrapper<SiteVariable> siteVariableResponse = HTTP2Request.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, siteVariableResponse.getStatusCode());

        return siteVariableResponse.getResponseEntity();
    }
}
