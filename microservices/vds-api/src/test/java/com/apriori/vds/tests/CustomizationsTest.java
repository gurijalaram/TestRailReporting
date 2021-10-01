package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.customizations.CustomizationsItems;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class CustomizationsTest extends VDSTestUtil {

    @Test
    @TestRail(testCaseId = {"7953"})
    @Description("Get a list of Customizations for a specific customer.")
    public void getCustomizations() {
        RequestEntity requestEntity = RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_CUSTOMIZATIONS, CustomizationsItems.class);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTPRequest.build(requestEntity).get().getStatusCode()
        );
    }
}
