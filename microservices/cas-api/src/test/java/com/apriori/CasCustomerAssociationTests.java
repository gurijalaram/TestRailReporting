package com.apriori;

import com.apriori.authorization.AuthorizationUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.models.response.CasErrorMessage;
import com.apriori.cas.models.response.CustomerAssociation;
import com.apriori.cas.models.response.CustomerAssociations;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CasCustomerAssociationTests {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private String aPIdentity;

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
        aPIdentity = casTestUtil.getAprioriInternal().getIdentity();
    }

    @Test
    @TestRail(id = {5681})
    @Description("Returns a list of customer associations for the customer")
    public void getCustomerAssociations() {
        ResponseWrapper<CustomerAssociations> associations = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER_ASSOCIATIONS,
            CustomerAssociations.class,
            HttpStatus.SC_OK,
            aPIdentity);

        soft.assertThat(associations.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertThat(associations.getResponseEntity().getItems().get(0).getTargetCustomerIdentity())
            .isNotNull();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5682})
    @Description("Get the customer association identified by its identity")
    public void getAssociationByIdentity() {
        ResponseWrapper<CustomerAssociations> associations = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER_ASSOCIATIONS,
            CustomerAssociations.class,
            HttpStatus.SC_OK,
            aPIdentity);

        String associationIdentity = associations.getResponseEntity().getItems().get(0).getIdentity();
        ResponseWrapper<CustomerAssociation> associationResponse = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER_ASSOCIATION,
            CustomerAssociation.class,
            HttpStatus.SC_OK,
            aPIdentity,
            associationIdentity);

        soft.assertThat(associationResponse.getResponseEntity().getIdentity())
            .isEqualTo(associationIdentity);
        soft.assertThat(associationResponse.getResponseEntity().getDescription())
            .contains("customer of aPriori");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {16125})
    @Description("Get the customer association with not existing identity")
    public void getAssociationThatNotExists() {
        String notExistingIdentity = "000000000000";
        ResponseWrapper<CasErrorMessage> response = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER_ASSOCIATION,
            CasErrorMessage.class,
            HttpStatus.SC_NOT_FOUND,
            aPIdentity,
            notExistingIdentity);

        soft.assertThat(response.getResponseEntity().getMessage())
            .isEqualTo(String.format("Can't find association for source customer with identity '%s' and association identity '%s'.", aPIdentity, notExistingIdentity));
        soft.assertAll();
    }
}