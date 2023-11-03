package com.apriori.cid.api.utils;

import com.apriori.cid.api.enums.CidAppAPIEnum;
import com.apriori.cid.api.models.response.customizations.Customizations;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;

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
