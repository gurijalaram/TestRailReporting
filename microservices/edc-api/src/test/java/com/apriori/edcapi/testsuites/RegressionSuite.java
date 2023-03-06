package com.apriori.edcapi.testsuites;

import com.apriori.edcapi.tests.AccountsControllerTest;
import com.apriori.edcapi.tests.BillOfMaterialsTest;
import com.apriori.edcapi.tests.LineItemsTest;
import com.apriori.edcapi.tests.PartsTest;
import com.apriori.edcapi.tests.ReportsTest;
import com.apriori.edcapi.tests.UsersTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("262")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    BillOfMaterialsTest.class,
    LineItemsTest.class,
    PartsTest.class,
    UsersTest.class,
    AccountsControllerTest.class,
    ReportsTest.class
})
public class RegressionSuite {
}
