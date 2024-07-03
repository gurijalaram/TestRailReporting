package com.apriori.cds.api.utils;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.request.CustomAttributeRequest;
import com.apriori.cds.api.models.response.CustomAttribute;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

public class AttributeUtil {
    private RequestEntityUtil requestEntityUtil;

    public AttributeUtil(RequestEntityUtil requestEntityUtil) {
        this.requestEntityUtil = requestEntityUtil;
    }

    /**
     * Posts custom attribute
     *
     * @param inlineVariables - the inline variables
     * @return - new object
     */
    public CustomAttribute addCustomAttribute(String... inlineVariables) {
        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.CUSTOM_ATTRIBUTES, CustomAttribute.class)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "customAttribute",
                CustomAttributeRequest.builder()
                    .key("department")
                    .name("department")
                    .value("TestDepartment")
                    .type("STRING")
                    .createdBy("#SYSTEM00000")
                    .build()
            );

        ResponseWrapper<CustomAttribute> response = HTTPRequest.build(requestEntity).post();

        return response.getResponseEntity();
    }

    /**
     * Updates or adds custom attributes
     *
     * @param updatedDepartment - updated department string
     * @param inlineVariables   - the inline variables
     * @return - new object
     */
    public CustomAttribute putCustomAttribute(String updatedDepartment, String... inlineVariables) {
        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.CUSTOM_ATTRIBUTES, CustomAttribute.class)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "customAttribute",
                CustomAttributeRequest.builder()
                    .name("department")
                    .value(updatedDepartment)
                    .updatedBy("#SYSTEM00000")
                    .build()
            );

        ResponseWrapper<CustomAttribute> response = HTTPRequest.build(requestEntity).put();

        return response.getResponseEntity();
    }

    /**
     * Updates custom attribute
     *
     * @param inlineVariables - the inline variables
     * @return - new object
     */
    public CustomAttribute updateAttribute(String updatedDepartment, String... inlineVariables) {
        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.CUSTOM_ATTRIBUTE_BY_ID, CustomAttribute.class)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "customAttribute",
                CustomAttributeRequest.builder()
                    .value(updatedDepartment)
                    .updatedBy("#SYSTEM00000")
                    .build()
            );

        ResponseWrapper<CustomAttribute> response = HTTPRequest.build(requestEntity).patch();

        return response.getResponseEntity();
    }
}
