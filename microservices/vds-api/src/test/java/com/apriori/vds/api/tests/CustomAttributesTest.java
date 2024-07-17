package com.apriori.vds.api.tests;

import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.vds.api.enums.VDSAPIEnum;
import com.apriori.vds.api.models.response.custom.attributes.CustomAttributesItems;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CustomAttributesTest {
    private RequestEntityUtil requestEntityUtil;

    @BeforeEach
    public void setup() {
        requestEntityUtil = TestHelper.initUser();
    }

    @Test
    @TestRail(id = {7949})
    @Description("Returns a list of UDAs for a specific customer.")
    public void getCustomAttributes() {
        RequestEntity requestEntity = requestEntityUtil.init(VDSAPIEnum.CUSTOM_ATTRIBUTES, CustomAttributesItems.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }
}
