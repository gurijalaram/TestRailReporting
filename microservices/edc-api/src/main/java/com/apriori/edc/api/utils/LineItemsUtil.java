package com.apriori.edc.api.utils;

import com.apriori.edc.api.enums.EDCAPIEnum;
import com.apriori.edc.api.models.response.line.items.LineItemsItemsResponse;
import com.apriori.edc.api.models.response.line.items.LineItemsResponse;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;

import org.apache.http.HttpStatus;

import java.util.List;

public class LineItemsUtil extends TestUtil {

    /**
     * Get all Line items in a Bill of Materials
     *
     * @param identity - identity
     * @return response object
     */
    public List<LineItemsResponse> getAllLineItems(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(EDCAPIEnum.LINE_ITEMS, LineItemsItemsResponse.class)
            .inlineVariables(identity)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<LineItemsItemsResponse> getAllResponse = HTTPRequest.build(requestEntity).get();

        return getAllResponse.getResponseEntity().getItems();
    }

    /**
     * Get all Line items in a Bill of Materials
     *
     * @param identity - identity
     * @return response object
     */
    //TODO z: fix threads
    public List<LineItemsResponse> getAllLineItemsWithToken(String identity, String token) {
        RequestEntity requestEntity = new RequestEntity().endpoint(EDCAPIEnum.LINE_ITEMS).returnType(LineItemsItemsResponse.class)
            .token(token)
            .inlineVariables(identity)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<LineItemsItemsResponse> getAllResponse = HTTPRequest.build(requestEntity).get();

        return getAllResponse.getResponseEntity().getItems();
    }
}