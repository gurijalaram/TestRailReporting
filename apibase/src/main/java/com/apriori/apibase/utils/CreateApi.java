package com.apriori.apibase.utils;

import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

/**
 * Represents a test utility implementation that can create models in the api.
 *
 * @param <E> The resource enum type.
 */
public interface CreateApi<E extends ExternalEndpointEnum> {
    /**
     * Invokes a post request to the given api
     *
     * @param apiEnum The api enum that specifies the endpoint.
     * @param klass The expected class being returned.
     * @param body The creatable body model.  This must have a @CreatableModel annotation with the specified kind.
     * @param inlineVariables Additional variables that fill out the apiEnum endpoint.
     *
     * @param <R> The expected type being returned.
     * @param <B> The body type.  This will usually be the same as R, but if you are expecting and validating
     *            an error, then you may have this be the object attempted to be created, while having something
     *            like ErrorMessage.class for R.
     *
     * @return The api response.
     */
    default <R, B> ResponseWrapper<R> create(E apiEnum, Class<R> klass, B body, String... inlineVariables) {
        CreatableModel model = body.getClass().getAnnotation(CreatableModel.class);

        if (model == null) {
            throw new IllegalArgumentException("The body is not a CreatableModel.  Did you forget a @CreatableModel(kind) annotation?");
        }

        RequestEntity requestEntity = RequestEntityUtil.init(apiEnum, klass).body(model.value(), body).inlineVariables(inlineVariables);
        return HTTPRequest.build(requestEntity).post();
    }
}
