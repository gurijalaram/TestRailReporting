package tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.getunitvariantsettings.GetUnitVariantSettingsResponse;
import com.apriori.acs.entity.response.getunitvariantsettings.UnitVariantSetting;
import com.apriori.acs.utils.AcsResources;
import com.apriori.acs.utils.Constants;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class GetUnitVariantSettingsTests {

    @BeforeClass
    public static void getAuthorizationToken() {
        Constants.getDefaultUrl();
    }

    @Test
    /*@Category()
    @TestRail(testCaseId = "")
    @Description("")*/
    public void testGetUnitVariantSettings() {
        AcsResources acsResources = new AcsResources();
        GetUnitVariantSettingsResponse getUnitVariantSettingsResponse = acsResources.getUnitVariantSettings();

        assertThat(getUnitVariantSettingsResponse, is(notNullValue()));

        ArrayList<UnitVariantSetting> allItems = getUnitVariantSettingsResponse.getAllUnitVariantSetting();
        assertThat(allItems, is(notNullValue()));

        for (UnitVariantSetting item : allItems) {
            assertThat(item.getType(), is(equalTo("simple")));
            assertThat(item.getDecimalPlaces(), is(equalTo(2.0)));
            assertThat(item.isSystem(), is(equalTo(true)));
            assertThat(item.isCustom(), is(equalTo(false)));
        }
    }
}
