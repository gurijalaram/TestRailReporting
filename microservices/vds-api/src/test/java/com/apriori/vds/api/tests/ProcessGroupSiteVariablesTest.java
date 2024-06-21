package com.apriori.vds.api.tests;

import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.vds.api.enums.VDSAPIEnum;
import com.apriori.vds.api.models.request.process.group.site.variable.SiteVariableRequest;
import com.apriori.vds.api.models.response.process.group.site.variable.SiteVariable;
import com.apriori.vds.api.tests.util.ProcessGroupUtil;
import com.apriori.vds.api.tests.util.SiteVariableUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(TestRulesAPI.class)
public class ProcessGroupSiteVariablesTest extends SiteVariableUtil {

    protected static final Set<String> siteVariableNamesToDelete = new HashSet<>();

    @AfterAll
    public static void deleteTestingData() {
        siteVariableNamesToDelete.forEach(ProcessGroupSiteVariablesTest::deleteProcessGroupSiteVariableByName);
    }

    @Test
    @TestRail(id = {8288})
    @Description("DELETEs a site variable. ")
    public void deleteSiteVariablesByName() {
        deleteProcessGroupSiteVariableByName(this.postProcessGroupSiteVariables().getName());
    }

    @Test
    @TestRail(id = {8286})
    @Description("PUT Adds or Replaces a CustomAttribute for a user. ")
    public void putCustomAttributeByIdentity() {
        SiteVariable siteVariableBeforeUpdate = this.postProcessGroupSiteVariables();
        siteVariableNamesToDelete.add(siteVariableBeforeUpdate.getName());

        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_PROCESS_GROUP_SITE_VARIABLE_BY_PG_ID, SiteVariable.class)
                .inlineVariables(ProcessGroupUtil.getProcessGroupIdentity())
                .body(initUpdateRequestBody(siteVariableBeforeUpdate))
                .expectedResponseCode(HttpStatus.SC_CREATED);

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTPRequest.build(requestEntity).put();
        SiteVariableUtil.validateCreatedObject(updatedSiteVariableResponse.getResponseEntity());
    }

    private static void deleteProcessGroupSiteVariableByName(final String name) {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.DELETE_PROCESS_GROUP_SITE_VARIABLE_BY_PG_SITE_IDs, null)
                .inlineVariables(ProcessGroupUtil.getProcessGroupIdentity(), name)
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }

    private SiteVariable postProcessGroupSiteVariables() {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PUT_PROCESS_GROUP_SITE_VARIABLE_BY_PG_ID, SiteVariable.class)
                .inlineVariables(ProcessGroupUtil.getProcessGroupIdentity())
                .body(SiteVariableRequest.builder()
                    .name(new GenerateStringUtil().generateAlphabeticString("Site", 5))
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
}
