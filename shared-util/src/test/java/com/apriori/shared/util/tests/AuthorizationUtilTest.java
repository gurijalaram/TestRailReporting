package com.apriori.shared.util.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.AuthorizationUtil;
import com.apriori.shared.util.models.response.Token;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AuthorizationUtilTest {

    @Test
    public void getToken() {
        final UserCredentials currentUser = UserUtil.getUser();
        ResponseWrapper<Token> response = new AuthorizationUtil().getToken(currentUser);

        assertThat(response.getResponseEntity().getToken(), is(not(emptyString())));
    }

    @Test
    @SneakyThrows
    public void getTokenInThreads() {
        final UserCredentials currentUser = UserUtil.getUser();
        final Integer threadsCount = 10;
        CountDownLatch latch = new CountDownLatch(threadsCount);

        for (int i = 0; i < threadsCount; i++) {
            new Thread(() -> {
                ResponseWrapper<Token> response = new AuthorizationUtil().getToken(currentUser);

                assertThat(response.getResponseEntity().getToken(), is(not(emptyString())));
                latch.countDown();
            }).start();

        }

        latch.await(1, TimeUnit.MINUTES);
    }
}
