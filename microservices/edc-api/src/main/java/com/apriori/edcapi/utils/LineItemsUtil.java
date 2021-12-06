package com.apriori.edcapi.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.edcapi.entity.response.line.items.LineItemsItemsResponse;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

public class LineItemsUtil extends TestUtil {

    protected static ResponseWrapper<LineItemsItemsResponse> getAllLineItems(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(EDCAPIEnum.GET_LINE_ITEMS, LineItemsItemsResponse.class)
            .inlineVariables(identity);

        return HTTPRequest.build(requestEntity).get();
    }
}
