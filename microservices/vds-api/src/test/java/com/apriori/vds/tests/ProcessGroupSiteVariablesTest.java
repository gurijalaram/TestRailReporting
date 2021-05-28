package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.process.group.site.variables.SiteVariable;
import com.apriori.vds.entity.response.process.group.site.variables.SiteVariablesItems;
import com.apriori.vds.tests.util.VDSRequestEntityUtil;
import com.apriori.vds.tests.util.VDSTestUtil;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.List;

public class ProcessGroupSiteVariablesTest extends VDSTestUtil {

    @Test
    @TestRail(testCaseId = {"8271"})
    @Description("Get a list of process groups for a specific customer.")
    public void getSiteVariables() {
        this.getSiteVariablesResponse();
    }

    private List<SiteVariable> getSiteVariablesResponse() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_SITE_VARIABLES, SiteVariablesItems.class);
        ResponseWrapper<SiteVariablesItems> siteVariablesResponse = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, siteVariablesResponse.getStatusCode());

        return siteVariablesResponse.getResponseEntity().getItems();
    }

}
