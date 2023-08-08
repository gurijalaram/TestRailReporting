package com.apriori.cidappapi.utils;

import com.apriori.cidappapi.enums.CidAppAPIEnum;
import com.apriori.cidappapi.models.response.customizations.Customizations;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;

public class CustomizationUtil {

    /**
     * GET customizations
     *
     * @return customizations object
     */
    public ResponseWrapper<Customizations> getCustomizations(UserCredentials userCredentials) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.CUSTOMIZATIONS, Customizations.class)
                .token(userCredentials.getToken());

        return HTTPRequest.build(requestEntity).get();
    }
}
