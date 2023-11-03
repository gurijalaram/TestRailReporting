package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.CustomerAssociationItems;
import com.apriori.cds.api.models.response.CustomerAssociationResponse;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.Constants;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CdsCustomerAssociationTests {
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private SoftAssertions soft = new SoftAssertions();

    @Test
    @TestRail(id = {5387})
    @Description("Get customer association for apriori Internal")
    public void getCustomerAssociations() {
        ResponseWrapper<CustomerAssociationResponse> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMERS_ASSOCIATIONS,
            CustomerAssociationResponse.class,
            HttpStatus.SC_OK,
            Constants.getAPrioriInternalCustomerIdentity()
        );

        soft.assertThat(response.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(response.getResponseEntity().getItems().get(0).getTargetCustomerIdentity()).isNotNull();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5825})
    @Description("Get customer association by association Identity")
    public void getCustomerAssociationByIdentity() {
        ResponseWrapper<CustomerAssociationResponse> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMERS_ASSOCIATIONS,
            CustomerAssociationResponse.class,
            HttpStatus.SC_OK,
            Constants.getAPrioriInternalCustomerIdentity()
        );
        String associationIdentity = response.getResponseEntity().getItems().get(0).getIdentity();
        ResponseWrapper<CustomerAssociationItems> association = cdsTestUtil.getCommonRequest(CDSAPIEnum.SPECIFIC_CUSTOMERS_ASSOCIATION_BY_CUSTOMER_ASSOCIATION_ID,
            CustomerAssociationItems.class,
            HttpStatus.SC_OK,
            Constants.getAPrioriInternalCustomerIdentity(),
            associationIdentity
        );

        soft.assertThat(association.getResponseEntity().getIdentity()).isEqualTo(associationIdentity);
        soft.assertThat(association.getResponseEntity().getDescription()).contains("customer of aPriori");
        soft.assertAll();
    }
}
