package com.apriori.tests.testsuite;

import com.apriori.tests.UploadComponentTests;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("361")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    UploadComponentTests.class
})
public class CidAppAPISuite {

}
