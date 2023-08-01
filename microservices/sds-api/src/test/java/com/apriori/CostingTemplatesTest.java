package com.apriori;

import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.CostingTemplate;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

public class CostingTemplatesTest extends SDSTestUtil {

    @Test
    @TestRail(id = {6934})
    @Description("Find costing templates for a customer matching a specified query.")
    public void testGetCostingTemplates() {
        this.getCostingTemplates();
    }

    @Test
    @TestRail(id = {6935})
    @Description("Get the current representation of a costing template.")
    public void testGetCostingTemplateByIdentity() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_COSTING_TEMPLATE_SINGLE_BY_IDENTITY_ID, CostingTemplate.class)
                .inlineVariables(this.getFirstCostingTemplate().getIdentity())
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

}
