package testsuites;

import com.apriori.vds.api.tests.AccessControlsTest;
import com.apriori.vds.api.tests.ConfigurationTest;
import com.apriori.vds.api.tests.CustomAttributesTest;
import com.apriori.vds.api.tests.CustomizationsTest;
import com.apriori.vds.api.tests.DigitalFactoriesTest;
import com.apriori.vds.api.tests.ProcessGroupMaterialStocksTest;
import com.apriori.vds.api.tests.ProcessGroupMaterialsTest;
import com.apriori.vds.api.tests.ProcessGroupSiteVariablesTest;
import com.apriori.vds.api.tests.ProcessGroupsTest;
import com.apriori.vds.api.tests.SiteVariablesTest;
import com.apriori.vds.api.tests.UserGroupAssociationsTest;

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
