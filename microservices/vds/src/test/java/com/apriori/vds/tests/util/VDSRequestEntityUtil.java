package com.apriori.vds.tests.util;

import com.apriori.utils.http.enums.EndpointEnum;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.vds.utils.Constants;

public class VDSRequestEntityUtil extends RequestEntityUtil {

    public static RequestEntity initWithSharedSecret(EndpointEnum endpoint, Class<?> returnType) {
        return initBuilder(endpoint, returnType)
            .header("ap-user-context", Constants.getApUserContext())
            .build();
    }
}
