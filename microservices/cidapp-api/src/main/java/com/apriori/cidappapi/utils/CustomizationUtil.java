package com.apriori.cidappapi.utils;

import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.response.customizations.Customizations;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

public class CustomizationUtil {
    private String userCredentials;

    public CustomizationUtil(String userCredentials) {
        this.userCredentials = userCredentials;
    }

    /**
     * GET call for customizations
     *
     * @return customizations object
     */
    public ResponseWrapper<Customizations> getCustomizations() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_CUSTOMIZATIONS, Customizations.class)
                .token(this.userCredentials);

        return HTTPRequest.build(requestEntity).get();
    }
}
