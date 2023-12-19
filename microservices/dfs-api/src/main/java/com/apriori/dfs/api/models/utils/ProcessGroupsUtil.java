package com.apriori.dfs.api.models.utils;

import com.apriori.dfs.api.enums.DFSApiEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtilBuilder;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;

public class ProcessGroupsUtil {

    protected static RequestEntityUtil requestEntityUtil;

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

        return HTTPRequest.build(getRequestEntity(expectedResponseCode, expectedType, inlineVariables)).get();
}

    /**
     * GET process group
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType Expected type from body of HTTP response
     * @param identity - identity
     * @return Response object
     */
    public <T> ResponseWrapper<T> getProcessGroupWithoutKeyParameter(Integer expectedResponseCode,
                                                                       Class<T> expectedType,
                                                                       String identity) {

        final RequestEntity requestEntity =  requestEntityUtil.init(DFSApiEnum.DIGITAL_FACTORIES_BY_PATH, expectedType)
            .inlineVariables(new String[]{ identity, ""}) // hack - add one more empty variable to skip auto adding shared secret
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    private <T> RequestEntity getRequestEntity(Integer expectedResponseCode,
                                               Class<T> expectedType,
                                               String... inlineVariables) {

        DFSApiEnum path = inlineVariables.length == 1
            ? DFSApiEnum.PROCESS_GROUPS_BY_PATH : DFSApiEnum.PROCESS_GROUPS_BY_PATH_WITH_KEY_PARAM;

        return requestEntityUtil.init(path, expectedType)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(expectedResponseCode);
    }

    public static void init() {
        requestEntityUtil = RequestEntityUtilBuilder.useRandomUser();
    }
}
