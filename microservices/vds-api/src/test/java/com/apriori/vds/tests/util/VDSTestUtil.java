package com.apriori.vds.tests.util;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.access.control.AccessControlGroup;
import com.apriori.vds.entity.response.access.control.AccessControlGroupItems;
import com.apriori.vds.entity.response.digital.factories.DigitalFactoriesItems;
import com.apriori.vds.entity.response.digital.factories.DigitalFactory;

import org.apache.http.HttpStatus;
import org.junit.Assert;

import java.util.List;

public abstract class VDSTestUtil extends TestUtil {
    protected static final String customerId =  PropertiesContext.get("${env}.customer_identity");
    protected static final String userId = PropertiesContext.get("${env}.user_identity");

    private static DigitalFactory digitalFactory;
    private static String digitalFactoryIdentity;

    protected static DigitalFactory getDigitalFactoriesResponse() {
        RequestEntity requestEntity = RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_DIGITAL_FACTORIES, DigitalFactoriesItems.class);
        ResponseWrapper<DigitalFactoriesItems> digitalFactoriesItemsResponseWrapper = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            digitalFactoriesItemsResponseWrapper.getStatusCode()
        );

        List<DigitalFactory> digitalFactories = digitalFactoriesItemsResponseWrapper.getResponseEntity().getItems();
        Assert.assertNotEquals("To get Digital Factory, response should contain it.", 0, digitalFactories.size());

        return findDigitalFactoryByName(digitalFactories, "aPriori USA");
    }

    private static DigitalFactory findDigitalFactoryByName(List<DigitalFactory> digitalFactories, final String name) {
        return digitalFactories.stream()
            .filter(digitalFactory -> name.equals(digitalFactory.getName()))
            .findFirst()
            .orElseThrow(
                () -> new IllegalArgumentException(String.format("Digital Factory with location: %s, was not found.", name))
            );
    }

    protected static List<AccessControlGroup> getAccessControlGroupsResponse() {
        RequestEntity requestEntity = RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_GROUPS, AccessControlGroupItems.class);

        ResponseWrapper<AccessControlGroupItems> accessControlGroupsResponse = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            accessControlGroupsResponse.getStatusCode()
        );

        return accessControlGroupsResponse.getResponseEntity().getItems();
    }

    public static DigitalFactory getDigitalFactory() {
        if (digitalFactory == null) {
            digitalFactory = getDigitalFactoriesResponse();
        }
        return digitalFactory;
    }

    public static String getDigitalFactoryIdentity() {
        if (digitalFactoryIdentity == null) {
            digitalFactoryIdentity = getDigitalFactory().getIdentity();
        }
        return digitalFactoryIdentity;
    }
}
