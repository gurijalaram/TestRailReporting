package com.apriori;

import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.testrail.TestRail;
import com.apriori.util.VDSTestUtil;
import com.apriori.vds.enums.VDSAPIEnum;
import com.apriori.vds.models.response.customizations.CustomizationsItems;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

public class CustomizationsTest extends VDSTestUtil {

    @Test
    @TestRail(id = {7953})
    @Description("Get a list of Customizations for a specific customer.")
    public void getCustomizations() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_CUSTOMIZATIONS, CustomizationsItems.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }
}
