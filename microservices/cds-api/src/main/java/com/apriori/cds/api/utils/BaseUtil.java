package com.apriori.cds.api.utils;

import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.utils.RequestEntityUtil;

@SuppressWarnings("unchecked")
public class BaseUtil<T extends BaseUtil<T>> {
    protected RequestEntityUtil requestEntityUtil;
    protected UserCredentials testingUser;

    public T setRequestEntityUtil(RequestEntityUtil requestEntityUtil) {
        this.requestEntityUtil = requestEntityUtil;
        this.testingUser = requestEntityUtil.getEmbeddedUser();

        return (T) this;
    }

    public RequestEntityUtil getRequestEntityUtil(RequestEntityUtil requestEntityUtil) {
        if(requestEntityUtil == null) {
            throw new IllegalArgumentException("RequestEntityUtil is null as was not set to the variable.");
        }
        return requestEntityUtil;
    }
}
