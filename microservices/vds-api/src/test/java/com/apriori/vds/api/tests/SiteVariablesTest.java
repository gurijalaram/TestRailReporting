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
import com.apriori.vds.api.tests.util.SiteVariableUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(TestRulesAPI.class)
public class SiteVariablesTest extends SiteVariableUtil {
    protected static final Set<String> siteVariableNamesToDelete = new HashSet<>();

    @AfterAll
    public static void deleteTestingData() {
        siteVariableNamesToDelete.forEach(SiteVariablesTest::deleteSiteVariablesByName);
    }

    @Test
    @TestRail(id = {8294})
    @Description("DELETEs a site variable.")
    public void deleteSiteVariablesByName() {
        deleteSiteVariablesByName(this.postSiteVariables().getName());
    }

    @Test
    @TestRail(id = {8292})
    @Description("Adds or Replaces a SiteVariable for a user.")
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
                    .type("DOUBLE")
                    .value("bar")
                    .valueType("STRING")
                    .notes("foo bar")
                    .createdBy("#SYSTEM00000")
                    .build()
                )
                .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<SiteVariable> siteVariableResponse = HTTPRequest.build(requestEntity).put();

        return siteVariableResponse.getResponseEntity();
    }
}
