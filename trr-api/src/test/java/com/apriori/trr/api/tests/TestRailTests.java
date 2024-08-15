package com.apriori.trr.api.tests;

import com.apriori.trr.api.testrail.TestRail;
import com.apriori.trr.api.testrail.TestRailConfig;
import com.apriori.trr.api.testrail.controller.ProjectTestCase;
import com.apriori.trr.api.testrail.controller.TestRailReport;
import com.apriori.trr.api.testrail.model.Project;
import com.apriori.trr.api.testrail.rules.TestRulesAPI;
import com.apriori.trr.api.testrail.utils.TestRailUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class TestRailTests {
    private TestRailUtil testRailUtil;
    private TestRailConfig testRailConfig;
    private TestRail testRail;

    @BeforeEach
    public void init() {
        testRailConfig = new TestRailConfig();
        testRail = TestRail.builder(testRailConfig.getBaseApiUrl(),
            testRailConfig.getUsername(), testRailConfig.getPassword()).build();
    }

    @Test
    public void generateTestCoverageReport() throws IOException {
        List<ProjectTestCase> projectTestCases = new ArrayList<>();
        List<Project> projectList = new TestRailUtil().getProjects().stream()
            .filter(p -> !p.getName().contains("Inactive"))
            .filter(p -> !p.getName().contains("Cloud Futures"))
            .filter(p -> !p.getName().contains("Cloud Migration"))
            .filter(p -> p.getId() != 10).collect(Collectors.toList());

        projectList.stream().forEach(p -> {
            projectTestCases.add(new TestRailUtil().getProjectTestMetrics(p.getId(), p.getName()));
        });

        projectTestCases.sort(Comparator.comparingInt(ProjectTestCase::getPercentageCovered).reversed());
        new TestRailReport().generateCoverageReport(projectTestCases);
    }
}