package com.apriori.edcapi.utils;

import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EDCResources {

    public static ResponseWrapper<Object> deleteBillOfMaterialById(final String identity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.DELETE_BILL_OF_MATERIALS_BY_IDENTITY, null)
                .inlineVariables(identity).token(new JwtTokenUtil().retrieveJwtToken());

        return HTTPRequest.build(requestEntity).delete();
    }
}

