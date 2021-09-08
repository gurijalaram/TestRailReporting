package com.util;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.web.driver.TestBase;

public class EdcTestUtil extends TestBase {

    private static String token = new JwtTokenUtil().retrieveJwtToken();

    public ResponseWrapper<String> deleteBillOfMaterial(String endpoint) {
        RequestEntity requestEntity = RequestEntity.init(endpoint, null)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        return GenericRequestUtil.delete(requestEntity, new RequestAreaApi());
    }
}
