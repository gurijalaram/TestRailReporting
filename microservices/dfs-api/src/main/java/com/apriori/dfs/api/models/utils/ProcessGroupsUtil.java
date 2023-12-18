package com.apriori.dfs.api.models.utils;

import com.apriori.dfs.api.enums.DFSApiEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;

public class ProcessGroupsUtil {

    /**
     * GET process group
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType Expected type from body of HTTP response
     * @param inlineVariables - identity or identity/secret
     * @return Response object
     */
    public <T> ResponseWrapper<T> getProcessGroup(Integer expectedResponseCode,
                                                    Class<T> expectedType,
                                                    String... inlineVariables) {

        DFSApiEnum path = inlineVariables.length == 0
            ? DFSApiEnum.DIGITAL_FACTORIES : DFSApiEnum.DIGITAL_FACTORIES_WITH_KEY_PARAM;

        final RequestEntity requestEntity = RequestEntityUtil_Old.init(path, expectedType)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();    }
}
