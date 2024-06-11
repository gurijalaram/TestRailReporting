package com.apriori.cid.ui.testsuites;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ASSEMBLY;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@IncludeTags(ASSEMBLY)
public class CidGuiAssemblySuite {
}
