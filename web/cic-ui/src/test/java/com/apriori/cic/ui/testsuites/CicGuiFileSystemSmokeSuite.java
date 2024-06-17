package com.apriori.cic.ui.testsuites;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cic.ui.tests.FileSystemSmokeTests;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    FileSystemSmokeTests.class
})

@IncludeTags(SMOKE)
public class CicGuiFileSystemSmokeSuite {
}
