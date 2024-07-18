package com.apriori.vds.api.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.vds.api.enums.VDSAPIEnum;
import com.apriori.vds.api.models.response.customizations.CustomizationsItems;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CustomizationsTest {
    private RequestEntityUtil requestEntityUtil;

    @BeforeEach
    public void setup() {
        requestEntityUtil = TestHelper.initUser();
    }

    @Test
    @TestRail(id = {7953})
    @Description("Get a list of Customizations for a specific customer.")
    public void getCustomizations() {
        RequestEntity requestEntity = requestEntityUtil.init(VDSAPIEnum.CUSTOMIZATIONS, CustomizationsItems.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<CustomizationsItems> response = HTTPRequest.build(requestEntity).get();

        assertThat(response.getResponseEntity().getItems()).isNotEmpty();
    }
}
