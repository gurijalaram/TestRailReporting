package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.custom.attributes.CustomAttributesItems;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class CustomAttributesTest extends VDSTestUtil {

    @Test
    @TestRail(testCaseId = {"7949"})
    @Description("Returns a list of UDAs for a specific customer.")
    public void getCustomAttributes() {
        RequestEntity requestEntity = RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_CUSTOM_ATTRIBUTES, CustomAttributesItems.class);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTPRequest.build(requestEntity).get().getStatusCode()
        );
    }
}
