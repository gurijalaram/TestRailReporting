package com.apriori.cia.ui.testsuites;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ADMIN;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori.cia.ui.tests.navigation")
@IncludeTags(ADMIN)
public class CiaGuiAdminSuite {
}