package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.cds.objects.response.AccessControls;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CdsAccessControlsTests extends CdsTestUtil {
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
        ResponseWrapper<AccessControls> response = getCommonRequest(url, true, AccessControls.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getOutOfContext(), is(notNullValue()));
    }
}
