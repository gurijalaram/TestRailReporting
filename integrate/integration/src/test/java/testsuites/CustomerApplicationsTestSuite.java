package testsuites;

import com.apriori.testconfig.TestSuiteType;

import com.integration.tests.customer.applications.cia.CIAdminApplicationTest;
import com.integration.tests.customer.applications.cic.CIConnectApplicationTest;
import com.integration.tests.customer.applications.cid.CIDesignApplicationTest;
import com.integration.tests.customer.applications.cir.CIReportApplicationTest;
import com.integration.tests.customer.applications.cis.CISandboxApplicationTest;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectModules;
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
        "com.ootbreports.general.assemblydetails"
})
@IncludeTags(TestSuiteType.TestSuite.CUSTOMER)
public class CustomerApplicationsTestSuite {
}
