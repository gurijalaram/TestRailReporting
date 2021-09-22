package com.apriori.edcapi.utils;

import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EDCResources {

    public static ResponseWrapper<Object> deleteBillOfMaterialById(final String identity) {
        com.apriori.utils.http2.builder.common.entity.RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.DELETE_BILL_OF_MATERIALS_BY_IDENTITY, null)
                .inlineVariables(identity).token(new JwtTokenUtil().retrieveJwtToken());

        return HTTP2Request.build(requestEntity).delete();
    }
}
