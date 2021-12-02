package com.apriori.apibase.utils;

import com.apriori.apibase.services.common.objects.Paged;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an api that contains find features.
 *
 * @param <E> The enum that contains the appropriate endpoints.
 */
public interface SearchApi<E extends ExternalEndpointEnum> {
    /**
     * Invokes a find operation and returns all items.
     *
     * @param apiEnum The api to invoke.
     * @param klass The type of data to return.
     * @param filter Filter parameters
     * @param sort Sort parameters
     * @param inlineVariables The optional variables for the api
     * @param <P> The pagination type.
     * @param <T> The data type for an individual item
     *
     * @return The list of all entities across all pages.
     */
    default <T, P extends Paged<T>> List<T> find(
        E apiEnum,
        Class<P> klass,
        Map<String, ?> filter,
        Map<String, String> sort,
        String... inlineVariables) {

        List<T> entities = new ArrayList<>();
        int pageNumber = 1;
        int pageSize = 1000;
        long read = 0L;
        long count;

        do {
            P page = find(apiEnum, klass, filter, sort, pageNumber, pageSize, inlineVariables).getResponseEntity();
            count = page.getTotalItemCount();
            read += page.getItems().size();
            entities.addAll(page.getItems());
            ++pageNumber;
        } while (read < count);

        return entities;
    }

    /**
     * Invokes a search on an api.
     *
     * @param apiEnum The enum to invoke the search on.
     * @param klass The class return value.
     * @param filter The filter parameters
     * @param sort The sort parameters
     * @param pageNumber What page to retrieve
     * @param pageSize What the page size is
     * @param inlineVariables The optional inline variables
     * @param <T> The paginated data type to return
     *
     * @return The pagination for the given klass.
     */
    default <T, P extends Paged<T>> ResponseWrapper<P> find(
        E apiEnum,
        Class<P> klass,
        Map<String, ?> filter,
        Map<String, String> sort,
        int pageNumber,
        int pageSize,
        String... inlineVariables) {

        Map<String, String> pagination = new HashMap<>();
        pagination.put("pageNumber", String.format("%d", pageNumber));
        pagination.put("pageSize", String.format("%d", pageSize));

        RequestEntity request = RequestEntityUtil.init(apiEnum, klass)
            .inlineVariables(inlineVariables)
            .urlParams(Arrays.asList(filter, sort, pagination));

        return HTTPRequest.build(request).get();
    }

    /**
     * Gets the first item in a find operation.
     *
     * @param apiEnum The api to invoke.
     * @param klass The expected pageable class type
     * @param filter The filter to cull the data.
     * @param sort The sort order.
     * @param inlineVariables Optional inline variables to fill out the api request
     *
     * @param <T> The underlying data type in the page.
     * @param <P> The Paged type.
     *
     * @return The first item found in the query.  Null if an empty set is returned.
     */
    default <T, P extends Paged<T>> T findFirst(
        E apiEnum,
        Class<P> klass,
        Map<String, ?> filter,
        Map<String, String> sort,
        String... inlineVariables
    ) {
        return find(apiEnum, klass, filter, sort, 1, 1, inlineVariables)
            .getResponseEntity()
            .getItems()
            .stream()
            .findFirst()
            .orElse(null);
    }
}
