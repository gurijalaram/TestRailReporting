package com.apriori.edcapi.utils;

import com.apriori.TestUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.edcapi.entity.response.line.items.LineItemsItemsResponse;
import com.apriori.edcapi.entity.response.line.items.LineItemsResponse;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;

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
}
