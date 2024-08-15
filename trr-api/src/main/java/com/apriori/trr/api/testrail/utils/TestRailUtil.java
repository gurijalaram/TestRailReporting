package com.apriori.trr.api.testrail.utils;

import com.apriori.trr.api.testrail.TestRail;
import com.apriori.trr.api.testrail.TestRailConfig;
import com.apriori.trr.api.testrail.controller.ProjectTestCase;
import com.apriori.trr.api.testrail.enums.CustomAutomationType;
import com.apriori.trr.api.testrail.model.Case;
import com.apriori.trr.api.testrail.model.Project;
import com.apriori.trr.api.testrail.model.Suite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class TestRailUtil {

    private static final Logger log = LoggerFactory.getLogger(TestRailUtil.class);
    TestRailConfig testRailConfig;
    TestRail testRail;

    public TestRailUtil() {
        this.testRailConfig = new TestRailConfig();
        this.testRail = TestRail.builder(testRailConfig.getBaseApiUrl(),
            testRailConfig.getUsername(), testRailConfig.getPassword()).build();
    }

    /**
     * get projects list
     *
     * @return List<Project></Project>
     */
    public List<Project> getProjects() {
        return testRail.projects().list().execute();
    }

    /**
     * get project test suites
     *
     * @param projectId - project id
     * @return List<Suite>
     */
    public List<Suite> getSuites(Integer projectId) {
        List<Suite> suites = testRail.suites().list(projectId).execute();
        return suites;
    }

    /**
     * get project test Cases list
     *
     * @param projectId - project id
     * @return List<Case>
     */
    public List<Case> getCases(Integer projectId) {
        return testRail.cases().list(projectId).execute();
    }

    /**
     * Get Project Test Metrics for each project
     * Total Manual cases, Automatable, Automated and percentage covered
     *
     * @param projectId   - Project ID
     * @param projectName - Project Name
     * @return ProjectTestCase class object
     */
    public ProjectTestCase getProjectTestMetrics(Integer projectId, String projectName) {
        ProjectTestCase projectTestMetrics = new ProjectTestCase();
        List<Case> testCases = this.getCases(projectId);

        List<Case> automatedList = testCases.stream()
            .filter(testCase -> !(testCase.getCustomAutomationType() == null))
            .filter(testCase -> Integer.parseInt(testCase.getCustomAutomationType()) == CustomAutomationType.AUTOMATED.getTypeIndex())
            .collect(Collectors.toList());

        List<Case> automatableList = testCases.stream()
            .filter(testCase -> !(testCase.getCustomAutomationType() == null))
            .filter(testCase -> Integer.parseInt(testCase.getCustomAutomationType()) == CustomAutomationType.AUTOMATABLE.getTypeIndex())
            .collect(Collectors.toList());

        List<Case> manualCasesList = testCases.stream()
            .filter(testCase -> !(testCase.getCustomAutomationType() == null))
            .filter(testCase -> Integer.parseInt(testCase.getCustomAutomationType()) == CustomAutomationType.MANUAL.getTypeIndex())
            .collect(Collectors.toList());

        projectTestMetrics.setProjectID(projectId);
        projectTestMetrics.setProjectName(projectName);
        projectTestMetrics.setAutomated(automatedList.size());
        projectTestMetrics.setAutomatable(automatableList.size() + automatedList.size());
        projectTestMetrics.setTotalCases(manualCasesList.size());

        if (projectTestMetrics.getAutomated() == 0 && projectTestMetrics.getAutomatable() == 0) {
            projectTestMetrics.setPercentageCovered(0);
        } else {
            projectTestMetrics.setPercentageCovered(projectTestMetrics.getAutomated() * 100 / projectTestMetrics.getAutomatable());
        }
        return projectTestMetrics;
    }
}