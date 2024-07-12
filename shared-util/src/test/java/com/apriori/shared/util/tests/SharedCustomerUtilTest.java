package com.apriori.shared.util.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import com.apriori.shared.util.SharedCustomerUtil;
import com.apriori.shared.util.models.response.Customer;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SharedCustomerUtilTest {

    @Test
    public void getCustomerIdentityTest() {
        final String customerIdentity = SharedCustomerUtil.getCurrentCustomerIdentity();

        assertThat(customerIdentity, is(not(emptyOrNullString())));
    }

    @Test
    @SneakyThrows
    public void getCustomerIdentityInThreads() {
        final Integer threadsCount = 10;
        CountDownLatch latch = new CountDownLatch(threadsCount);

        for (int i = 0; i < threadsCount; i++) {
            new Thread(() -> {
                final String customerIdentity = SharedCustomerUtil.getCurrentCustomerIdentity();

                assertThat(customerIdentity, is(not(emptyOrNullString())));

                latch.countDown();
            }).start();

        }

        latch.await(1, TimeUnit.MINUTES);
    }

    @Test
    public void getApIntCustomerDataTest() {
        final Customer apIntCustomerData = SharedCustomerUtil.getCustomerData();

        assertThat(apIntCustomerData, is(not(nullValue())));
    }


    @Test
    public void getAuthTargetCloudContextTest() {
        final String authTargetCloudContext = SharedCustomerUtil.getAuthTargetCloudContext();

        assertThat(authTargetCloudContext, is(not(emptyOrNullString())));
    }


    @Test
    public void getCustomerDataTest() {
        final Customer customerData = SharedCustomerUtil.getCustomerData();

        assertThat(customerData, is(not(nullValue())));
    }
}