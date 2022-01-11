package com.evaluate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.response.ApplicationMetadata;
import com.apriori.cidappapi.utils.CidAppTestUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class ApplicationMetadataTest {
    private final CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();

    @Test
    @Description("Returns the current application's metadata")
    public void getApplicationMetadata() {
        final UserCredentials currentUser = UserUtil.getUser();

        ResponseWrapper<ApplicationMetadata> applicationMetadataResponseWrapper = cidAppTestUtil.getApplicationMetadata(currentUser);

        assertThat(applicationMetadataResponseWrapper.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }
}
