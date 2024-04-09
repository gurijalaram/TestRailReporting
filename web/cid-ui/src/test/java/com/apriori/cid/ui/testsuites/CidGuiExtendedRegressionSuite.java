package com.apriori.cid.ui.testsuites;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@IncludeTags(EXTENDED_REGRESSION)
public class CidGuiExtendedRegressionSuite {
}