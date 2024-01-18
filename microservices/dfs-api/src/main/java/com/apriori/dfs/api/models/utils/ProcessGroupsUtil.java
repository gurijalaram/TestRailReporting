package com.apriori.dfs.api.models.utils;

import com.apriori.dfs.api.enums.DFSApiEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;

public class ProcessGroupsUtil {

    /**
     * FIND process groups
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType Expected type from body of HTTP response
     * @param inlineVariables - identity or identity/secret
     * @return Response object
     */
    public <T> ResponseWrapper<T> findProcessGroups(Integer expectedResponseCode,
                                                  Class<T> expectedType,
                                                  String... inlineVariables) {

        DFSApiEnum path = inlineVariables.length == 0
            ? DFSApiEnum.PROCESS_GROUPS : DFSApiEnum.PROCESS_GROUPS_WITH_KEY_PARAM;

        final RequestEntity requestEntity = RequestEntityUtil_Old.init(path, expectedType)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }
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

        final RequestEntity requestEntity =  RequestEntityUtil_Old.init(DFSApiEnum.PROCESS_GROUPS_BY_PATH, expectedType)
            .inlineVariables(new String[]{ identity, ""}) // hack - add one more empty variable to skip auto adding shared secret
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    private <T> RequestEntity getRequestEntity(Integer expectedResponseCode,
                                               Class<T> expectedType,
                                               String... inlineVariables) {

        DFSApiEnum path = inlineVariables.length == 1
            ? DFSApiEnum.PROCESS_GROUPS_BY_PATH : DFSApiEnum.PROCESS_GROUPS_BY_PATH_WITH_KEY_PARAM;

        return RequestEntityUtil_Old.init(path, expectedType)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(expectedResponseCode);
    }

    /**
     * GET process group
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType Expected type from body of HTTP response
     * @param identity - identity
     * @return Response object
     */
    public <T> ResponseWrapper<T> findProcessGroupsWithoutKeyParameter(Integer expectedResponseCode,
                                                                     Class<T> expectedType,
                                                                     String identity) {

        final RequestEntity requestEntity =  RequestEntityUtil_Old.init(DFSApiEnum.PROCESS_GROUPS_BY_PATH, expectedType)
            .inlineVariables(new String[]{ identity, ""}) // hack - add one more empty variable to skip auto adding shared secret
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * FIND process groups page
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType Expected type from body of HTTP response
     * @return Response object
     */
    public <T> ResponseWrapper<T> findProcessGroupsPage(Integer expectedResponseCode,
                                                        Class<T> expectedType,
                                                        String... inlineVariables) {

        final RequestEntity requestEntity =  RequestEntityUtil_Old.init(
            DFSApiEnum.PROCESS_GROUPS_WITH_PAGE_SIZE_AND_PAGE_NUMBER, expectedType)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * FIND all process groups sored by name
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType Expected type from body of HTTP response
     * @return Response object
     */
    public <T> ResponseWrapper<T> findAllProcessGroupsSortedByName(Integer expectedResponseCode,
                                                        Class<T> expectedType,
                                                        String... inlineVariables) {

        final RequestEntity requestEntity =  RequestEntityUtil_Old.init(
            DFSApiEnum.PROCESS_GROUPS_SORTED_BY_NAME, expectedType)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * FIND all process groups by name
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType Expected type from body of HTTP response
     * @return Response object
     */
    public <T> ResponseWrapper<T> findAllProcessGroupsByName(Integer expectedResponseCode,
                                                             Class<T> expectedType,
                                                             String... inlineVariables) {

        final RequestEntity requestEntity =  RequestEntityUtil_Old.init(
            DFSApiEnum.PROCESS_GROUPS_BY_NAME, expectedType)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }
}
