package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.Constants;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Application;
import com.apriori.shared.util.models.response.Applications;
import com.apriori.shared.util.models.response.Customers;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CdsApplicationsTests {
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private SoftAssertions soft = new SoftAssertions();

    @Test
    @TestRail(id = {3251})
    @Description("API returns a list of all the available applications in the CDS DB")
    public void getAllApplications() {
        ResponseWrapper<Applications> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.APPLICATIONS, Applications.class, HttpStatus.SC_OK);

        soft.assertThat(response.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(response.getResponseEntity().getItems().get(0).getIsSingleTenant()).isNotNull();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {3700})
    @Description("API returns an application's information based on the supplied identity")
    public void getApplicationById() {
        ResponseWrapper<Application> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.APPLICATION_BY_ID,
            Application.class,
            HttpStatus.SC_OK,
            Constants.getApProApplicationIdentity()
        );

        soft.assertThat(response.getResponseEntity().getName()).isEqualTo("aP Pro");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5811})
    @Description(" API returns a paged list of customers authorized to use a particular application")
    public void getCustomersAuthorizedForApplication() {
        ResponseWrapper<Customers> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_APPLICATION_BY_ID,
            Customers.class,
            HttpStatus.SC_OK,
            Constants.getApProApplicationIdentity()
        );

        soft.assertThat(response.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }
}
