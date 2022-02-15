package com.apriori.nts.tests.suite;

import com.apriori.nts.tests.Emails;
import com.apriori.nts.tests.NotificationsTests;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("369")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        Emails.class,
        NotificationsTests.class
})
public class APISuite {
}
