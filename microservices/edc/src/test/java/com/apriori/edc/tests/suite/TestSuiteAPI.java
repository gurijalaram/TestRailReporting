package com.apriori.edc.tests.suite;

import com.apriori.edc.tests.AccountsTest;
import com.apriori.edc.tests.BillOfMaterialsTest;
import com.apriori.edc.tests.PartOfMaterialsTest;
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
