package com.apriori.fms.tests.suite;

import com.apriori.fms.tests.FileManagementControllerTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("373")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        FileManagementControllerTest.class
})
public class APISuite {
}
