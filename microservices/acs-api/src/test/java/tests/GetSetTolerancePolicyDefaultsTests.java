package tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.getsettolerancepolicydefaults.GetTolerancePolicyDefaultsResponse;
import com.apriori.acs.utils.AcsResources;
import com.apriori.utils.TestRail;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GetSetTolerancePolicyDefaultsTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10473")
    @Description("Test Get Tolerance Policy Defaults")
    public void testGetTolerancePolicyDefaults() {
        AcsResources acsResources = new AcsResources();
        GetTolerancePolicyDefaultsResponse response = acsResources.getTolerancePolicyDefaults();

        assertThat(response, is(notNullValue()));
    }
}
