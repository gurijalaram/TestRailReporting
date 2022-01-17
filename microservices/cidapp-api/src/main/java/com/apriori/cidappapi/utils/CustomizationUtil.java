package com.apriori.cidappapi.utils;

import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.response.customizations.Customizations;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

public class CustomizationUtil {

    /**
     * GET customizations
     *
     * @return customizations object
     */
    public ResponseWrapper<Customizations> getCustomizations(UserCredentials userCredentials) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_CUSTOMIZATIONS, Customizations.class)
                .token(userCredentials.getToken());

        return HTTPRequest.build(requestEntity).get();
    }
}
