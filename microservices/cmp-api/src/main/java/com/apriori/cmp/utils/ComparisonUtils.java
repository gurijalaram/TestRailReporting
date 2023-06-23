package com.apriori.cmp.utils;

import com.apriori.cmp.entity.enums.CMPAPIEnum;
import com.apriori.cmp.entity.request.CreateComparison;
import com.apriori.cmp.entity.response.PostComparisonResponse;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

public class ComparisonUtils {

    public ResponseWrapper<PostComparisonResponse> createComparison(CreateComparison comparison, UserCredentials currentUser) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CMPAPIEnum.COMPARISON, PostComparisonResponse.class)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                .body("comparison", comparison);

        return HTTPRequest.build(requestEntity).post();
    }
}
