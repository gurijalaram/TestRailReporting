package com.apriori.vds.tests.util;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.digital.factories.DigitalFactoriesItems;
import com.apriori.vds.entity.response.digital.factories.DigitalFactory;
import org.apache.http.HttpStatus;

public class VDSTestUtil extends TestUtil {

    protected DigitalFactory getDigitalFactoriesResponse() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_DIGITAL_FACTORIES, DigitalFactoriesItems.class);

        ResponseWrapper<DigitalFactoriesItems> digitalFactoriesItemsResponse = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            digitalFactoriesItemsResponse.getStatusCode()
        );

        return digitalFactoriesItemsResponse.getResponseEntity().getItems().get(0);
    }

}
