package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.entity.response.CustomerAssociationItems;
import com.apriori.cds.entity.response.CustomerAssociationResponse;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CdsCustomerAssociationTests {
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Test
    @TestRail(testCaseId = {"5387"})
    @Description("Get customer association for apriori Internal")
    public void getCustomerAssociations() {
        ResponseWrapper<CustomerAssociationResponse> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_CUSTOMERS_ASSOCIATIONS_BY_CUSTOMER_ID,
            CustomerAssociationResponse.class,
            Constants.getAPrioriInternalCustomerIdentity()
        );

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getItems().get(0).getTargetCustomerIdentity(), is(not(nullValue())));
    }

    @Test
    @TestRail(testCaseId = {"5825"})
    @Description("Get customer association by association Identity")
    public void getCustomerAssociationByIdentity() {
        ResponseWrapper<CustomerAssociationResponse> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_CUSTOMERS_ASSOCIATIONS_BY_CUSTOMER_ID,
            CustomerAssociationResponse.class,
            Constants.getAPrioriInternalCustomerIdentity()
        );
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        String associationIdentity = response.getResponseEntity().getItems().get(0).getIdentity();
        ResponseWrapper<CustomerAssociationItems> association = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_SPECIFIC_CUSTOMERS_ASSOCIATION_BY_CUSTOMER_ASSOCIATION_ID,
            CustomerAssociationItems.class,
            Constants.getAPrioriInternalCustomerIdentity(),
            associationIdentity
        );

        assertThat(association.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(association.getResponseEntity().getResponse().getIdentity(), is(equalTo(associationIdentity)));
        assertThat(association.getResponseEntity().getResponse().getDescription(), containsString("customer of aPriori"));
    }
}
