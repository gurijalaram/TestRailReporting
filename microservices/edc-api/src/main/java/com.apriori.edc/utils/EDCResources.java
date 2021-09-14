package com.apriori.edc.utils;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EDCResources {

    private static String token = new JwtTokenUtil().retrieveJwtToken();

    public static ResponseWrapper<String> deleteBillOfMaterial(String endpoint) {
        RequestEntity requestEntity = RequestEntity.init(endpoint, null)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        return GenericRequestUtil.delete(requestEntity, new RequestAreaApi());
    }
}
