package com.apriori.util.test.authorization;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.authorization.Token;
import com.apriori.utils.http.utils.ResponseWrapper;

import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AuthorizationUtilTest {

    @Test
    @SneakyThrows
    public void getTokenInThreads() {
        final Integer threadsCount = 10;
        CountDownLatch latch = new CountDownLatch(threadsCount);

        for (int i = 0; i < threadsCount; i++) {
            new Thread(() -> {
                ResponseWrapper<Token> response = new AuthorizationUtil().getToken();

                assertThat(response.getResponseEntity().getToken(), is(not(emptyString())));
                latch.countDown();
            }).start();

        }

        latch.await(1, TimeUnit.MINUTES);
    }
}
