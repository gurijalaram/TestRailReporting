package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.apibase.services.cds.objects.AccessControls.AccessControlResponse;
import com.apriori.cds.entity.response.User;
import com.apriori.cds.entity.response.Users;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CdsAccessControls extends CdsTestUtil {
    private String url;

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }


    @Test
    @TestRail(testCaseId = "3289")
    @Description("API returns a list of all the access controls in the CDS DB")
    public void getAccessControls() {
        url = String.format(url, "access-controls");
        ResponseWrapper<AccessControlResponse> response = getCommonRequest(url, true, AccessControlResponse.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
       // assertThat(response.getResponseEntity().getResponse().getItems().get(0).getOutOfContext(), is(not(emptyString())));
    }

}
