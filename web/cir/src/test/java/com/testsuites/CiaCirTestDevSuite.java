package com.testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import com.testsuites.suiteinterface.CiaCirTestDevTest;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
    AssemblyDetailsReportTests.class
})

public class CiaCirTestDevSuite {
}
