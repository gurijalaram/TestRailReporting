package com.apriori.util;

import com.apriori.TestUtil;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.properties.PropertiesContext;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.access.control.AccessControlGroup;
import com.apriori.vds.entity.response.access.control.AccessControlGroupItems;
import com.apriori.vds.entity.response.digital.factories.DigitalFactoriesItems;
import com.apriori.vds.entity.response.digital.factories.DigitalFactory;

import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

public abstract class VDSTestUtil extends TestUtil {
    protected static final String customerId =  PropertiesContext.get("customer_identity");
    protected static final String userId = PropertiesContext.get("user_identity");
    protected static UserCredentials testingUser;

    private static DigitalFactory digitalFactory;
    private static String digitalFactoryIdentity;

    @BeforeAll
    public static  void init() {
        RequestEntityUtil.useApUserContextForRequests(testingUser = UserUtil.getUser());
    }

    protected static DigitalFactory getDigitalFactoriesResponse() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_DIGITAL_FACTORIES, DigitalFactoriesItems.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<DigitalFactoriesItems> digitalFactoriesItemsResponseWrapper = HTTPRequest.build(requestEntity).get();

        List<DigitalFactory> digitalFactories = digitalFactoriesItemsResponseWrapper.getResponseEntity().getItems();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(digitalFactories.size()).isNotZero();

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
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_GROUPS, AccessControlGroupItems.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<AccessControlGroupItems> accessControlGroupsResponse = HTTPRequest.build(requestEntity).get();

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
