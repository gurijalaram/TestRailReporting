package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.apibase.services.cas.objects.Customers;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.cds.entity.response.Customer;
import com.apriori.cds.entity.response.Users;
import com.apriori.cds.entity.response.apVersion.apVersionResponse;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class apVersions extends CdsTestUtil {
    private String url;

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }


    @Test
    @Description("Get a list of ap Versions in CDSDb")
    public void getapVersions() {
        url = String.format(url,
            String.format("ap-versions", Constants.getCdsIdentityCustomer()));

        ResponseWrapper<apVersionResponse> response = getCommonRequest(url, true, apVersionResponse.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getVersion(), is(not(emptyString())));
    }

}
