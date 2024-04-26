package com.apriori.acs.api.tests;

import com.apriori.acs.api.models.response.acs.allmaterialstocksinfo.AllMaterialStocksInfoResponse;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericExtendedPropertyInfoItem;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AllMaterialStocksInfoTests extends TestUtil {
    private final UserCredentials user = UserUtil.getUser("common");

    @Test
    @TestRail(id = 16829)
    @Description("Test Get All Material Stocks Info endpoint")
    public void testGetAllMaterialStocksInfo() {
        AcsResources acsResources = new AcsResources(user);
        AllMaterialStocksInfoResponse allMaterialStocksInfoResponse = acsResources
            .getAllMaterialStocksInfo(
                "aPriori USA",
                ProcessGroupEnum.SHEET_METAL.getProcessGroup(),
                "Steel, Cold Worked, AISI 1020"
        );

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(allMaterialStocksInfoResponse.getPropertyValuesList().size()).isGreaterThan(0);

        GenericExtendedPropertyInfoItem costPerKG = allMaterialStocksInfoResponse.getPropertyInfoMap().getCostPerKG();

        softAssertions.assertThat(costPerKG.getName()).isEqualTo("costPerKG");
        softAssertions.assertThat(costPerKG.getUnitTypeName()).isEqualTo("USD / kg");
        softAssertions.assertThat(costPerKG.getSupportedSerializedType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }
}
