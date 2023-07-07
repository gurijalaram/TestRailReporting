package com.apriori.util.test.authorization;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.authorization.Token;
import com.apriori.utils.http.utils.ResponseWrapper;
import org.junit.Test;

public class AuthorizationUtilTest {

    @Test
    public void getTokenInThreads() {

        ResponseWrapper<Token> response = new AuthorizationUtil().getToken();

        assertThat(response.getResponseEntity().getToken(), is(not(emptyString())));

        for(int i=0; i<10; i++) {

            new Thread(() -> {
                ResponseWrapper<Token> response1 = new AuthorizationUtil().getToken();

                assertThat(response.getResponseEntity().getToken(), is(not(emptyString())));
            }).start();

        }
    }
}
