package com.apriori.sds.tests;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.CostingTemplate;
import com.apriori.sds.entity.response.CostingTemplatesItems;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

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
        ResponseWrapper<CostingTemplate> response =
            new CommonRequestUtil().getCommonRequestWithInlineVariables(SDSAPIEnum.GET_COSTING_TEMPLATE_SINGLE_BY_IDENTITY_ID, CostingTemplate.class,
                new APIAuthentication().initAuthorizationHeaderContent(token),
                this.getCostingTemplatesResponseWrapper().getResponseEntity().getItems().get(0).getIdentity()
            );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    private ResponseWrapper<CostingTemplatesItems> getCostingTemplatesResponseWrapper() {
        return new CommonRequestUtil().getCommonRequestWithInlineVariables(SDSAPIEnum.GET_COSTING_TEMPLATES, CostingTemplatesItems.class,
            new APIAuthentication().initAuthorizationHeaderContent(token)
        );
    }

}
