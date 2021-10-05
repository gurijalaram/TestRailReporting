package com.apriori.cis.testsuites;

import com.apriori.cis.testsuites.suiteinterface.SmokeTests;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(SmokeTests.class)
@Suite.SuiteClasses({

})
public class CISSmokeTestSuite {
}