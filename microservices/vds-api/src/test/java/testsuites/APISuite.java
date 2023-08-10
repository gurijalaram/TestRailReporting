package testsuites;

import com.apriori.AccessControlsTest;
import com.apriori.ConfigurationTest;
import com.apriori.CustomAttributesTest;
import com.apriori.CustomizationsTest;
import com.apriori.DigitalFactoriesTest;
import com.apriori.ProcessGroupMaterialStocksTest;
import com.apriori.ProcessGroupMaterialsTest;
import com.apriori.ProcessGroupSiteVariablesTest;
import com.apriori.ProcessGroupsTest;
import com.apriori.SiteVariablesTest;
import com.apriori.UserGroupAssociationsTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    AccessControlsTest.class,
    ConfigurationTest.class,
    CustomAttributesTest.class,
    CustomizationsTest.class,
    DigitalFactoriesTest.class,
    ProcessGroupMaterialsTest.class,
    ProcessGroupMaterialStocksTest.class,
    ProcessGroupsTest.class,
    ProcessGroupSiteVariablesTest.class,
    SiteVariablesTest.class,
    UserGroupAssociationsTest.class
})
public class APISuite {
}
