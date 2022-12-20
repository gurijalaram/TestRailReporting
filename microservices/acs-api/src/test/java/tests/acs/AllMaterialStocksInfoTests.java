package tests.acs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThan;

import com.apriori.acs.entity.response.acs.allmaterialstocksinfo.AllMaterialStocksInfoResponse;
import com.apriori.acs.entity.response.acs.genericclasses.GenericExtendedPropertyInfoItem;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.junit.Test;

public class AllMaterialStocksInfoTests extends TestUtil {

    @Test
    @TestRail(testCaseId = "16829")
    @Description("Test Get All Material Stocks Info endpoint")
    public void testGetAllMaterialStocksInfo() {
        AcsResources acsResources = new AcsResources();
        AllMaterialStocksInfoResponse allMaterialStocksInfoResponse = acsResources
            .getAllMaterialStocksInfo(
                "aPriori USA",
                ProcessGroupEnum.SHEET_METAL.getProcessGroup(),
                "Steel, Cold Worked, AISI 1020"
        );

        assertThat(allMaterialStocksInfoResponse.getPropertyValuesList().size(), is(greaterThan(0)));

        GenericExtendedPropertyInfoItem costPerKG = allMaterialStocksInfoResponse.getPropertyInfoMap().getCostPerKG();
        assertThat(costPerKG.getName(), is(equalTo("costPerKG")));
        assertThat(costPerKG.getUnitTypeName(), is(equalTo("USD / kg")));
        assertThat(costPerKG.getSupportedSerializedType(), is(equalTo("DOUBLE")));
    }
}
