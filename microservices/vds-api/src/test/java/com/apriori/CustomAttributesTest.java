package com.apriori;

import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.testrail.TestRail;
import com.apriori.util.VDSTestUtil;
import com.apriori.vds.enums.VDSAPIEnum;
import com.apriori.vds.models.response.custom.attributes.CustomAttributesItems;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

public class CustomAttributesTest extends VDSTestUtil {

    @Test
    @TestRail(id = {7949})
    @Description("Returns a list of UDAs for a specific customer.")
    public void getCustomAttributes() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_CUSTOM_ATTRIBUTES, CustomAttributesItems.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }
}
