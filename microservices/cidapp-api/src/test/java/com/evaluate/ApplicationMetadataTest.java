package com.evaluate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.authorization.ApplicationMetadata;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class ApplicationMetadataTest {

    @Test
    @Description("Returns the current application metadata")
    public void getApplicationMetadata() {
        final UserCredentials currentUser = UserUtil.getUser();

        ResponseWrapper<ApplicationMetadata> applicationMetadataUtil = new AuthorizationUtil().getApplicationMetadata(currentUser);

        assertThat(applicationMetadataUtil.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }
}
