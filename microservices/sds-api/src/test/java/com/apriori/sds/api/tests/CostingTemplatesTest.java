package com.apriori.sds.api.tests;

import com.apriori.sds.api.enums.SDSAPIEnum;
import com.apriori.sds.api.models.response.CostingTemplate;
import com.apriori.sds.api.util.SDSTestUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
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
            RequestEntityUtil_Old.init(SDSAPIEnum.GET_COSTING_TEMPLATE_SINGLE_BY_IDENTITY_ID, CostingTemplate.class)
                .inlineVariables(this.getFirstCostingTemplate().getIdentity())
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

}
