package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.configuration.ConfigurationsItems;
import com.apriori.vds.entity.response.custom.attributes.CustomAttributesItems;
import com.apriori.vds.tests.util.VDSRequestEntityUtil;
import com.apriori.vds.tests.util.VDSTestUtil;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class CustomAttributesTest extends VDSTestUtil {

    @Test
    @TestRail(testCaseId = {"7949"})
    @Description("Returns a list of UDAs for a specific customer.")
    public void getCustomAttributes() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_CUSTOM_ATTRIBUTES, CustomAttributesItems.class);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTP2Request.build(requestEntity).get().getStatusCode()
        );
    }
}
