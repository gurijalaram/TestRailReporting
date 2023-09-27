package testsuites;

import com.apriori.testconfig.TestSuiteType;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
//@SelectClasses({
//    CIAdminApplicationTest.class,
//    CIConnectApplicationTest.class,
//    CIDesignApplicationTest.class,
//    CIReportApplicationTest.class,
//    CISandboxApplicationTest.class
//})

@SelectPackages({
        "com.login", // cir login
        "com.ootbreports.general.assemblydetails", // cir
        "com.apriori.navigation" // cas

//        "com.apriori.cis-ui"
//        "com.apriori.cas-ui"
})
@IncludeTags(TestSuiteType.TestSuite.CUSTOMER)
public class CustomerApplicationsTestSuite {

}
