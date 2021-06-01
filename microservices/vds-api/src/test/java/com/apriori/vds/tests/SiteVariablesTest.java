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
import java.util.Collections;
import java.util.List;

public class SiteVariablesTest extends VDSTestUtil {

    private static final List<String> siteVariableIdsToDelete = new ArrayList<>();

    private final String updatedName = new GenerateStringUtil().generateSiteName();
    private final String updatedValue = "UpdatedValue";
    private final String updatedNotes = "UpdatedNotes";

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
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_SITE_VARIABLE_BY_ID, SiteVariable.class)
                .inlineVariables(Collections.singletonList(
                    this.getFirstSiteVariable().getIdentity()
                ));
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTP2Request.build(requestEntity).get().getStatusCode());
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
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.PATCH_SITE_VARIABLES_BY_ID, SiteVariable.class)
                .inlineVariables(Collections.singletonList(siteVariableBeforeUpdate.getIdentity()))
                .body(this.initUpdateRequestBody(siteVariableBeforeUpdate));

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTP2Request.build(requestEntity).patch();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, updatedSiteVariableResponse.getStatusCode());
        this.validateUpdatedFields(updatedSiteVariableResponse.getResponseEntity());
    }

    @Test
    @TestRail(testCaseId = {"8292"})
    @Description("Adds or Replaces a SiteVariable for a user.")
    public void putSiteVariableForAUser() {
        SiteVariable siteVariable = getFirstSiteVariable();

        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.PUT_SITE_VARIABLES, SiteVariable.class)
                .body(this.initUpdateRequestBody(siteVariable));

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTP2Request.build(requestEntity).put();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, updatedSiteVariableResponse.getStatusCode());
        this.validateUpdatedFields(updatedSiteVariableResponse.getResponseEntity());
    }

    private static void deleteSiteVariableById(final String identity) {
        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.DELETE_SITE_VARIABLE_BY_ID, null)
                .inlineVariables(Collections.singletonList(identity));
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, HTTP2Request.build(requestEntity).delete().getStatusCode());
    }

    private void validateUpdatedFields(SiteVariable updatedSiteVariable) {
        assertNotEquals("The name should not be updated.", updatedName, updatedSiteVariable.getName());
        assertEquals("The value should be updated.", updatedValue, updatedSiteVariable.getValue());
        assertEquals("Notes should be updated.", updatedNotes, updatedSiteVariable.getNotes());
    }

    private SiteVariableRequest initUpdateRequestBody(final SiteVariable siteVariableInfo) {
        return SiteVariableRequest.builder()
            .name(updatedName)
            .value(updatedValue)
            .notes(updatedNotes)
            .updatedBy(siteVariableInfo.getCreatedBy())
            .createdBy(siteVariableInfo.getCreatedBy())
            .build();
    }

    private SiteVariable getFirstSiteVariable() {
        List<SiteVariable> siteVariables = this.getSiteVariablesResponse();
        assertNotEquals("To get Site Variable, response should contain it.", 0, siteVariables.size());

        return siteVariables.get(0);
    }

    private List<SiteVariable> getSiteVariablesResponse() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_SITE_VARIABLES, SiteVariablesItems.class);
        ResponseWrapper<SiteVariablesItems> siteVariablesResponse = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, siteVariablesResponse.getStatusCode());

        return siteVariablesResponse.getResponseEntity().getItems();
    }

    private SiteVariable postSiteVariables() {
        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.POST_SITE_VARIABLE, SiteVariable.class)
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
