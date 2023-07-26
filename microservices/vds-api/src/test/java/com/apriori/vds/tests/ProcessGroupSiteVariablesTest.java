package com.apriori.vds.tests;

import static org.junit.Assert.assertNotEquals;

import com.apriori.GenerateStringUtil;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.request.process.group.site.variable.SiteVariableRequest;
import com.apriori.vds.entity.response.process.group.site.variable.SiteVariable;
import com.apriori.vds.entity.response.process.group.site.variable.SiteVariablesItems;
import com.apriori.vds.tests.util.SiteVariableUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProcessGroupSiteVariablesTest extends SiteVariableUtil {

    protected static final Set<String> siteVariableIdsToDelete = new HashSet<>();

    @AfterClass
    public static void deleteTestingData() {
        siteVariableIdsToDelete.forEach(ProcessGroupSiteVariablesTest::deleteProcessGroupSiteVariableById);
    }

    @Test
    @TestRail(id = {8284})
    @Description("GET a paged set of Site Variables for a specific customer. ")
    public void getSiteVariables() {
        this.getProcessGroupSiteVariablesResponse();
    }

    @Test
    @TestRail(id = {8287})
    @Description("GET a site variable for a customer. ")
    public void getSiteVariablesByIdentity() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.GET_PROCESS_GROUP_SITE_VARIABLE_BY_PG_SITE_IDs, SiteVariable.class)
                .inlineVariables(
                    getProcessGroupIdentity(),
                    this.getFirstProcessGroupSiteVariable().getIdentity()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(id = {8285})
    @Description("POST a Site Variable for a customer. The site variable can't already exist. Better to use PUT to create or replace the site variable.")
    public void postSiteVariablesByIdentity() {
        siteVariableIdsToDelete.add(this.postProcessGroupSiteVariables().getIdentity());
    }

    @Test
    @TestRail(id = {8288})
    @Description("DELETEs a site variable. ")
    public void deleteSiteVariablesByIdentity() {
        deleteProcessGroupSiteVariableById(this.postProcessGroupSiteVariables().getIdentity());
    }

    @Test
    @TestRail(id = {8289})
    @Description("PATCH a Site Variable for a customer. User PUT to create or replace if the site variable does not exist.")
    public void patchSiteVariablesByIdentity() {
        SiteVariable siteVariableBeforeUpdate = this.postProcessGroupSiteVariables();
        siteVariableIdsToDelete.add(siteVariableBeforeUpdate.getIdentity());

        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.PATCH_PROCESS_GROUP_SITE_VARIABLES_BY_PG_SITE_IDs, SiteVariable.class)
                .inlineVariables(getProcessGroupIdentity(), siteVariableBeforeUpdate.getIdentity())
                .body(initUpdateRequestBody(siteVariableBeforeUpdate))
                .expectedResponseCode(HttpStatus.SC_CREATED);

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTPRequest.build(requestEntity).patch();

        validateUpdatedObject(updatedSiteVariableResponse.getResponseEntity());
    }

    @Test
    @TestRail(id = {8286})
    @Description("PUT Adds or Replaces a CustomAttribute for a user. ")
    public void putCustomAttributeByIdentity() {
        SiteVariable siteVariableBeforeUpdate = this.postProcessGroupSiteVariables();
        siteVariableIdsToDelete.add(siteVariableBeforeUpdate.getIdentity());

        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.PUT_PROCESS_GROUP_SITE_VARIABLE_BY_PG_ID, SiteVariable.class)
                .inlineVariables(getProcessGroupIdentity())
                .body(initUpdateRequestBody(siteVariableBeforeUpdate))
                .expectedResponseCode(HttpStatus.SC_CREATED);

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTPRequest.build(requestEntity).put();
        validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());
    }

    private static void deleteProcessGroupSiteVariableById(final String identity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.DELETE_PROCESS_GROUP_SITE_VARIABLE_BY_PG_SITE_IDs, null)
                .inlineVariables(getProcessGroupIdentity(), identity)
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }

    private SiteVariable getFirstProcessGroupSiteVariable() {
        List<SiteVariable> siteVariables = this.getProcessGroupSiteVariablesResponse();
        assertNotEquals("To get Site Variable, response should contain it.", 0, siteVariables.size());

        return siteVariables.get(0);
    }

    private List<SiteVariable> getProcessGroupSiteVariablesResponse() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_PROCESS_GROUP_SITE_VARIABLES_BY_PG_ID, SiteVariablesItems.class)
            .inlineVariables(getProcessGroupIdentity())
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<SiteVariablesItems> siteVariablesResponse = HTTPRequest.build(requestEntity).get();

        return siteVariablesResponse.getResponseEntity().getItems();
    }

    private SiteVariable postProcessGroupSiteVariables() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.POST_PROCESS_GROUP_SITE_VARIABLES_BY_PG_ID, SiteVariable.class)
                .inlineVariables(getProcessGroupIdentity())
                .body(SiteVariableRequest.builder()
                    .name(new GenerateStringUtil().generateSiteName())
                    .type("DOUBLE")
                    .value("bar")
                    .valueType("STRING")
                    .notes("foo bar")
                    .createdBy(this.getFirstProcessGroupSiteVariable().getCreatedBy())
                    .build()
                )
                .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<SiteVariable> siteVariableResponse = HTTPRequest.build(requestEntity).post();

        return siteVariableResponse.getResponseEntity();
    }
}
