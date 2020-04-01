package com.apriori.apitests.edc.suite;

import com.apriori.apitests.edc.AccountsTest;
import com.apriori.apitests.edc.BillOfMaterialsTest;
import com.apriori.apitests.edc.PartOfMaterialsTest;
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
