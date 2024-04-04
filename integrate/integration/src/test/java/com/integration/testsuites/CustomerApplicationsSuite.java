package com.integration.testsuites;

import com.apriori.cic.ui.testsuites.CicGuiSanitySuite;
import com.apriori.cid.api.testsuites.CidApiSanitySuite;
import com.apriori.cir.ui.testsuites.CirGuiReportingSuite;
import com.apriori.cis.ui.testsuites.CisGuiRegressionSuite;
import com.apriori.shared.util.testconfig.TestSuiteType.TestSuite;

import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CisGuiRegressionSuite.class, // aP Workspace
    CicGuiSanitySuite.class, // aP Generate
    CirGuiReportingSuite.class, // aP Analytics
    CidApiSanitySuite.class, // aP Design
})
@ExcludeTags({TestSuite.CUSTOMER, TestSuite.NON_CUSTOMER})
public class CustomerApplicationsSuite {
}
