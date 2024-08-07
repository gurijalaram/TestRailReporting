package com.apriori.trr.api.testrail.http.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

/**
 * @author cfrith
 */

@Slf4j
public class RestAssuredFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext filterContext) {
        Response response = filterContext.next(requestSpec, responseSpec);

        if (response.statusCode() >= HttpStatus.SC_BAD_REQUEST) {
            log.error("Request Method:- {} \n Request URI:- {} \n Request Body:- {} \n Response Status:- {} \n Response Line:- {} \n Response Body:- {}",
                requestSpec.getMethod(), requestSpec.getURI(), requestSpec.getBody(), response.getStatusCode(), response.getStatusLine(), response.getBody().asPrettyString());
        }
        log.info("Request Method:- {} \n Request URI:- {} \n Request Body:- {} \n Response Status:- {} \n Response Line:- {} \n Response Body:- {}",
            requestSpec.getMethod(), requestSpec.getURI(), requestSpec.getBody(), response.getStatusCode(), response.getStatusLine(), response.getBody().asPrettyString());

        return response;
    }
}
