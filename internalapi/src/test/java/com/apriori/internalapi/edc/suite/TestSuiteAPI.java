package com.apriori.internalapi.edc.suite;

import com.apriori.internalapi.edc.AccountsTest;
import com.apriori.internalapi.edc.BillOfMaterialsTest;
import com.apriori.internalapi.edc.PartOfMaterialsTest;
import com.apriori.internalapi.services.CdsUsers;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    PartOfMaterialsTest.class,
    BillOfMaterialsTest.class,
    AccountsTest.class,
    CdsUsers.class
})
public class TestSuiteAPI {
}
