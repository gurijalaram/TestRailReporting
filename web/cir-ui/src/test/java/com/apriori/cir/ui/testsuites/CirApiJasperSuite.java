package com.apriori.cir.ui.testsuites;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori.cir.ui.tests.ootbreports.newreportstests")
@IncludeTags(JASPER_API)
public class CirApiJasperSuite {
}
