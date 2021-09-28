package com.apriori.sds.tests;

import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.CostingTemplate;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class CostingTemplatesTest extends SDSTestUtil {

    @Test
    @TestRail(testCaseId = {"6934"})
    @Description("Find costing templates for a customer matching a specified query.")
    public void testGetCostingTemplates() {
        this.getCostingTemplates();
    }

    @Test
    @TestRail(testCaseId = {"6935"})
    @Description("Get the current representation of a costing template.")
    public void testGetCostingTemplateByIdentity() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_COSTING_TEMPLATE_SINGLE_BY_IDENTITY_ID, CostingTemplate.class)
                .inlineVariables(this.getFirstCostingTemplate().getIdentity());

        ResponseWrapper<CostingTemplate> response = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

}
