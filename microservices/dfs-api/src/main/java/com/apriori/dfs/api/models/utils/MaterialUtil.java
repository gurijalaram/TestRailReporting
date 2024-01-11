package com.apriori.dfs.api.models.utils;

import com.apriori.dfs.api.enums.DFSApiEnum;
import com.apriori.dfs.api.models.response.Material;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.interfaces.EndpointEnum;

public class MaterialUtil {

    /**
     * GET Material
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType         Expected type from body of HTTP response
     * @param inlineVariables      - identity or identity/secret
     * @return Response object
     */

    public <T> ResponseWrapper<T> getMaterial(EndpointEnum endpointEnum, Integer expectedResponseCode,
                                              Class<T> expectedType, String... inlineVariables) {

        return HTTPRequest.build(getRequestEntity(endpointEnum, expectedResponseCode, expectedType, inlineVariables)).get();
    }

    /**
     * GET Material
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType         Expected type from body of HTTP response
     * @param identity             - identity
     * @return Response object
     */
    public <T> ResponseWrapper<T> getMaterialWithoutKeyParameter(EndpointEnum endpointEnum,
                                                                 Integer expectedResponseCode,
                                                                 Class<T> expectedType,
                                                                 String... inlineVariables) {

        final RequestEntity requestEntity = RequestEntityUtil_Old.init(endpointEnum, expectedType)
            .inlineVariables(inlineVariables) // hack - add one more empty variable to skip auto adding shared secret
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    private <T> RequestEntity getRequestEntity(EndpointEnum endpointEnum, Integer expectedResponseCode,
                                               Class<T> expectedType, String... inlineVariables) {

        return RequestEntityUtil_Old.init(endpointEnum, expectedType)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(expectedResponseCode);
    }
}
