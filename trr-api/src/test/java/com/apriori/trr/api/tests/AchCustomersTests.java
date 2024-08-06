package com.apriori.trr.api.tests;

import com.apriori.trr.api.utils.TestRailConfig;
import com.apriori.trr.api.utils.TestRailUtil;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AchCustomersTests {
    private TestRailUtil testRailUtil;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void getProjects(){
        TestRailConfig testRailConfig = new TestRailConfig();
        TestRail testRail = TestRail.builder(testRailConfig.getBaseApiUrl(),
            testRailConfig.getUsername(), testRailConfig.getPassword()).build();

        List<Project> pl= testRail.projects().list().execute();
        System.out.println(pl.get(0));
    }

    @Test
    public void testCoverageReport(){
        TestRailConfig testRailConfig = new TestRailConfig();
        TestRail testRail = TestRail.builder(testRailConfig.getBaseApiUrl(),
            testRailConfig.getUsername(), testRailConfig.getPassword()).build();

        List<Project> pl= testRail.projects().list().execute();
        System.out.println(pl.get(0));
    }
}