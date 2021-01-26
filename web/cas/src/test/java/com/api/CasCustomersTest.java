package com.api;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.apibase.services.cas.objects.Customers;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.Constants;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class CasCustomersTest extends TestUtil {

    private static String token;

    @Test
    @Description("Get a list of CAS customers")
    public void getCustomers() {
        String apiUrl = String.format(Constants.getApiUrl(), "customers?sortBy[ASC]=name");

        token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
            Constants.getCasServiceHost(),
            HttpStatus.SC_CREATED,
            Constants.getCasTokenUsername(),
            Constants.getCasTokenEmail(),
            Constants.getCasTokenIssuer(),
            Constants.getCasTokenSubject());

        ResponseWrapper<Customers> response = new CommonRequestUtil().getCommonRequest(apiUrl, true, Customers.class,
            new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }
}
