package com.apriori.apitests.fms.suite;

import com.apriori.apitests.fms.FmsFileManagementService;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("373")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        FmsFileManagementService.class
})
public class FmsAPISuite {
}
