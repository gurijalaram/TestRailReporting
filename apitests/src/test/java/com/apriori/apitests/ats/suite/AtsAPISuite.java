package com.apriori.apitests.ats.suite;

import com.apriori.apitests.ats.AtsAuthorization;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("375")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        AtsAuthorization.class
})
public class AtsAPISuite {

}
