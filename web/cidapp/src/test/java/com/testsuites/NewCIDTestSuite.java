package com.testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.login.LoginTests;
import com.testsuites.suiteinterface.SmokeTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("177")
@RunWith(CategorySuiteRunner.class)
@Categories.ExcludeCategory(SmokeTests.class)
@Suite.SuiteClasses({
    LoginTests.class,
})
public class NewCIDTestSuite {
}