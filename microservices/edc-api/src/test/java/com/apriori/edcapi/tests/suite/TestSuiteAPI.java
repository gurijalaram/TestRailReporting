package com.apriori.edcapi.tests.suite;

import com.apriori.edcapi.tests.AccountsTest;
import com.apriori.edcapi.tests.BillOfMaterialsTest;
import com.apriori.edcapi.tests.PartOfMaterialsTest;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    PartOfMaterialsTest.class,
    BillOfMaterialsTest.class,
    AccountsTest.class
})
public class TestSuiteAPI {
}
