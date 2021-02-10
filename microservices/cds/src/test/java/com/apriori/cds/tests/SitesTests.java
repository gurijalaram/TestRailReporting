package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.not;

import com.apriori.apibase.services.cds.objects.Site;
import com.apriori.apibase.services.cds.objects.Sites;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class SitesTests extends CdsTestUtil {
    private String url;

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }


    @Test
    @Description("Get a list of Sites in CDS Db")
    public void getSites() {
        url = String.format(url,"sites");

        ResponseWrapper<Sites> response = getCommonRequest(url, true, Sites.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getSiteId(), is(not(emptyString())));
    }

    @Test
    @Description("Get details of a site by its Identity")
    public void getSiteByIdentity() {
        String sitesUrl = String.format(url,"sites");

        ResponseWrapper<Sites> response = getCommonRequest(sitesUrl, true, Sites.class);
        String siteIdentity = response.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        String identityUrl = String.format(url, String.format("sites/%s", siteIdentity));
        ResponseWrapper<Site> responseWrapper = getCommonRequest(identityUrl, true, Site.class);

        assertThat(responseWrapper.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        //assertThat(responseWrapper.getResponseEntity().getResponse().getName(),is("USER"));
    }

}
