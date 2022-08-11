package com.apriori.edcapi.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.edcapi.entity.request.PartsRequest;
import com.apriori.edcapi.entity.response.parts.PartsResponse;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

public class PartsUtil extends TestUtil {

    /**
     * List the parts in a line item matching a specified query
     *
     * @param bomIdentity      - bill of material identity
     * @param lineItemIdentity - line of item identity
     * @return response object
     */
    public PartsResponse getAllPartsInLineItem(String bomIdentity, String lineItemIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.GET_BILL_OF_MATERIALS_LINE_ITEMS_PARTS, PartsResponse.class)
                .inlineVariables(bomIdentity, lineItemIdentity);

        ResponseWrapper<PartsResponse> getAllPartsResponse = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, getAllPartsResponse.getStatusCode());

        return getAllPartsResponse.getResponseEntity();
    }

    public ResponseWrapper<ErrorMessage> postNewPartToLineItem(String bomIdentity, String lineItemIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.POST_BILL_OF_MATERIALS_LINE_ITEMS_PARTS, ErrorMessage.class)
                .inlineVariables(bomIdentity, lineItemIdentity)
                .body("partsInput",
                    PartsRequest.builder().availability("Active")
                        .actionRequired(true)
                        .averageCost(1.1239)
                        .externalId("523648")
                        .classification("Connectors > Connector Other > Connector Terminals")
                        .dataSheetUrl("http://www.te.com/usa-en/product-53440-1.html")
                        .description("ELECTRO-TAP, 18-14 AWG RUN TAP")
                        .isUserPart(true)
                        .manufacturer("TE Connectivity")
                        .manufacturerPartNumber("53440-1")
                        .minimumCost(0.4656)
                        .mountType("Cable Mount")
                        .pinCount(5456)
                        .rohs("Yes")
                        .rohsVersion("2011/65/EU, 2015/863")
                        .build()
                );

        return HTTPRequest.build(requestEntity).post();
    }
}
