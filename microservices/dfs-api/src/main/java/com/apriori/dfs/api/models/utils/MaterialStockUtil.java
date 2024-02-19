package com.apriori.dfs.api.models.utils;

import com.apriori.dfs.api.enums.DFSApiEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.interfaces.EndpointEnum;

public class MaterialStockUtil {
    /**
     * FIND material stocks with shared secret key
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType - Expected type from body of HTTP response
     * @param inlineVariables - path and query parameters
     * @return Response object
     */
    public <T> ResponseWrapper<T> findMaterialStocks(Integer expectedResponseCode,
                                                Class<T> expectedType,
                                                String... inlineVariables) {

        return findMaterialStocks(expectedResponseCode, expectedType, true, inlineVariables);
    }

    /**
     * FIND material stocks
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType - Expected type from body of HTTP response
     * @param withSharedSecretKey - whether shared access key is provided or should be added
     * @param inlineVariables - path and query parameters
     * @return Response object
     */
    public <T> ResponseWrapper<T> findMaterialStocks(Integer expectedResponseCode,
                                                Class<T> expectedType,
                                                Boolean withSharedSecretKey,
                                                String... inlineVariables) {

        DFSApiEnum path = inlineVariables.length < 4
                ? DFSApiEnum.MATERIAL_STOCKS : DFSApiEnum.MATERIAL_STOCKS_WITH_KEY_PARAM;

        return findMaterialStocks(path, expectedResponseCode, expectedType, withSharedSecretKey, inlineVariables);
    }

    /**
     * FIND material stocks with default shared secret key and path specified
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType - Expected type from body of HTTP response
     * @param inlineVariables - path and query parameters
     * @return Response object
     */
    public <T> ResponseWrapper<T> findMaterialStocks(DFSApiEnum path,
                                                Integer expectedResponseCode,
                                                Class<T> expectedType,
                                                String... inlineVariables) {

        return findMaterialStocks(path, expectedResponseCode, expectedType, null, inlineVariables);
    }

    /**
     * FIND material stocks with shared secret key and path specified
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType - Expected type from body of HTTP response
     * @param inlineVariables - path and query parameters
     * @return Response object
     */
    public <T> ResponseWrapper<T> findMaterialStocks(DFSApiEnum path,
                                                Integer expectedResponseCode,
                                                Class<T> expectedType,
                                                Boolean withSharedSecretKey,
                                                String... inlineVariables) {

        path.setWithSharedSecret(withSharedSecretKey);

        final RequestEntity requestEntity = RequestEntityUtil_Old.init(path, expectedType)
                .inlineVariables(inlineVariables)
                .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * GET Material Stock
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType         Expected type from body of HTTP response
     * @param inlineVariables      - identity or identity/secret
     * @return Response object
     */
    public <T> ResponseWrapper<T> getMaterialStock(EndpointEnum endpointEnum, Integer expectedResponseCode,
                                              Class<T> expectedType, String... inlineVariables) {

        return HTTPRequest.build(getRequestEntity(endpointEnum, expectedResponseCode, expectedType, inlineVariables)).get();
    }

    /**
     * GET Material Stock
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType         Expected type from body of HTTP response
     * @param inlineVariables             - inlineVariables
     * @return Response object
     */
    public <T> ResponseWrapper<T> getMaterialStockWithoutKeyParameter(EndpointEnum endpointEnum,
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
