package com.apriori.cid.api.testsuites;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.DELETE;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@IncludeTags(DELETE)
public class CidApiDeleteSuite {
}
