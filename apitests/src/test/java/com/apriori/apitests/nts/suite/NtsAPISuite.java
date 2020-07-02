package com.apriori.apitests.nts.suite;

import com.apriori.apitests.nts.NtsEmails;
import com.apriori.apitests.nts.NtsNotifications;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("369")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        NtsEmails.class,
        NtsNotifications.class
})
public class NtsAPISuite {
}
