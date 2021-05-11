package com.apriori.sds.tests;

import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.ScenarioIterationItemsResponse;
import com.apriori.sds.utils.Constants;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class DemoTestHTTP2 {

    @Test
    public void getIterationsDemo() {
        RequestEntityUtil.useTokenForRequests(initToken());

        RequestEntity requestEntity = RequestEntityUtil.init(SDSAPIEnum.GET_ITERATIONS_BY_COMPONENT_SCENARIO_IDS, ScenarioIterationItemsResponse.class)
            .inlineVariables(Arrays.asList("6MIGDH2M0N93", "5HN1KFMALLM1"));

        Assert.assertEquals(HttpStatus.SC_OK, HTTP2Request.build(requestEntity).get().getStatusCode());
    }

    private static String initToken() {
        return new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
            Constants.getCidServiceHost(),
            HttpStatus.SC_CREATED,
            Constants.getCidTokenUsername(),
            Constants.getCidTokenEmail(),
            Constants.getCidTokenIssuer(),
            Constants.getCidTokenSubject());
    }
}
