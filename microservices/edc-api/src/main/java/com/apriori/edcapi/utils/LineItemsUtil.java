package com.apriori.edcapi.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.edcapi.entity.response.line.items.LineItemsResponse;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

public class LineItemsUtil extends TestUtil {

    protected static ResponseWrapper<LineItemsResponse> getAllLineItems(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(EDCAPIEnum.GET_LINE_ITEMS, LineItemsResponse.class)
            .inlineVariables(identity);

        return HTTPRequest.build(requestEntity).get();
    }
}
