package com.apriori.shared.util.http.utils;

import com.apriori.shared.util.annotations.CreatableModel;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.interfaces.EndpointEnum;
import com.apriori.shared.util.interfaces.Paged;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUtil {

    /**
     * Invokes a find operation and returns all items.
     *
     * @param apiEnum         The api to invoke.
     * @param klass           The type of data to return.
     * @param filter          Filter parameters
     * @param sort            Sort parameters
     * @param inlineVariables The optional variables for the api
     * @param <P>             The pagination type.
     * @param <T>             The data type for an individual item
     * @return The list of all entities across all pages.
     */
    public final <E extends EndpointEnum, T, P extends Paged<T>> List<T> findAll(
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
     * Gets the first item in a find operation.
     *
     * @param apiEnum         The api to invoke.
     * @param klass           The expected pageable class type
     * @param filter          The filter to cull the data.
     * @param sort            The sort order.
     * @param inlineVariables Optional inline variables to fill out the api request
     * @param <E>             The endpoint api type
     * @param <T>             The underlying data type in the page.
     * @param <P>             The Paged type.
     * @return The first item found in the query.  Null if an empty set is returned.
     */
    public final <E extends EndpointEnum, T, P extends Paged<T>> T findFirst(
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

    /**
     * Invokes a post request to the given api
     *
     * @param apiEnum         The api enum that specifies the endpoint.
     * @param klass           The expected class being returned.
     * @param body            The creatable body model.  This must have a @CreatableModel annotation with the specified kind.
     * @param inlineVariables Additional variables that fill out the apiEnum endpoint.
     * @param <E>             The api enum type.
     * @param <R>             The expected type being returned.
     * @param <B>             The body type.  This will usually be the same as R, but if you are expecting and validating
     *                        an error, then you may have this be the object attempted to be created, while having something
     *                        like ErrorMessage for R.
     * @return The api response.
     */
    public final <E extends EndpointEnum, R, B> ResponseWrapper<R> create(E apiEnum, Class<R> klass, B body, Integer expectedResponseCode, String... inlineVariables) {
        CreatableModel model = body.getClass().getAnnotation(CreatableModel.class);

        if (model == null) {
            throw new IllegalArgumentException("The body is not a CreatableModel.  Did you forget a @CreatableModel(kind) annotation?");
        }

        RequestEntity requestEntity = RequestEntityUtil.init(apiEnum, klass).body(model.value(), body).inlineVariables(inlineVariables).expectedResponseCode(expectedResponseCode);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Invokes a search on an api.
     *
     * @param apiEnum         The enum to invoke the search on.
     * @param klass           The class return value.
     * @param filter          The filter parameters
     * @param sort            The sort parameters
     * @param pageNumber      What page to retrieve
     * @param pageSize        What the page size is
     * @param inlineVariables The optional inline variables
     * @param <E>             The api enum type
     * @param <T>             The paginated data type to return
     * @return The pagination for the given klass.
     */
    public final <E extends EndpointEnum, T, P extends Paged<T>> ResponseWrapper<P> find(
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
            .expectedResponseCode(HttpStatus.SC_OK)
            .urlParams(Arrays.asList(filter, sort, pagination));

        return HTTPRequest.build(request).get();
    }

    /**
     * Calls an api with the GET verb.
     *
     * @param apiEnum         The api enum that specifies the endpoint.
     * @param klass           The returning class object.
     * @param inlineVariables Options variables used to help build the endpoint string.
     * @param <E>             The api enum type
     * @param <T>             The data type expected to be returned.
     * @return The response wrapper that contains the response data.
     */
    public final <E extends EndpointEnum, T> ResponseWrapper<T> getCommonRequest(E apiEnum, Class<T> klass, Integer expectedResponseCode, String... inlineVariables) {
        RequestEntity request = RequestEntityUtil.init(apiEnum, klass).inlineVariables(inlineVariables).expectedResponseCode(expectedResponseCode);
        return HTTPRequest.build(request).get();
    }

    /**
     * Calls an api with the DELETE verb.
     *
     * @return The response of what was deleted
     */
    public final <E extends EndpointEnum> ResponseWrapper<String> delete(E apiEnum, String... inlineVariables) {
        return HTTPRequest.build(RequestEntityUtil.init(apiEnum, null).inlineVariables(inlineVariables).expectedResponseCode(HttpStatus.SC_NO_CONTENT)).delete();
    }
}
