package com.apriori.sds.util;

import com.apriori.sds.utils.Constants;
import com.apriori.utils.http.enums.EndpointEnum;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.utils.RequestEntityUtil;

public class SDSRequestEntityUtil extends RequestEntityUtil {
    public static RequestEntity initWithApUserContext(EndpointEnum endpoint, Class<?> returnType) {
        return initBuilder(endpoint, returnType)
            .header("ap-user-context", Constants.getApUserContext())
            .build();
    }
}
