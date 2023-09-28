package testsuites;

import com.apriori.testconfig.TestSuiteType;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({
        "com.apriori", // cic aP Generate
        "com.apriori.cir-ui.evaluate", // cid aP Design
        "com.apriori.navigation", // cis aP Workspace
        "com.login", // cir login aP Analytics
        "com.ootbreports.general.assemblydetails", // cir aP Analytics
})

@IncludeTags(TestSuiteType.TestSuite.CUSTOMER)
public class CustomerTagTestSuite {

}
