package com.apriori.sds.tests;

import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.CostingTemplate;
import com.apriori.sds.entity.response.CostingTemplatesItems;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;
import util.SDSTestUtil;

public class CostingTemplatesTest extends SDSTestUtil {

    @Test
    @TestRail(testCaseId = {"6934"})
    @Description("Find costing templates for a customer matching a specified query.")
    public void getCostingTemplates() {
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, this.getCostingTemplatesResponseWrapper().getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"6935"})
    @Description("Get the current representation of a costing template.")
    public void getCostingTemplateByIdentity() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_COSTING_TEMPLATE_SINGLE_BY_IDENTITY_ID, CostingTemplate.class)
                .inlineVariables(
                    this.getCostingTemplatesResponseWrapper().getResponseEntity().getItems().get(0).getIdentity()
                );

        ResponseWrapper<CostingTemplate> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    private ResponseWrapper<CostingTemplatesItems> getCostingTemplatesResponseWrapper() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_COSTING_TEMPLATES, CostingTemplatesItems.class)
                .inlineVariables(
                    this.getCostingTemplatesResponseWrapper().getResponseEntity().getItems().get(0).getIdentity()
                );

        return HTTP2Request.build(requestEntity).get();
    }

}
