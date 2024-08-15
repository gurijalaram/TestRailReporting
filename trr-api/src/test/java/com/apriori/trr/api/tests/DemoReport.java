package com.apriori.trr.api.tests;

import com.apriori.trr.api.testrail.TestRail;
import com.apriori.trr.api.testrail.TestRailConfig;
import com.apriori.trr.api.testrail.TestRailRule;
import com.apriori.trr.api.testrail.controller.ProjectTestCase;
import com.apriori.trr.api.testrail.controller.TestRailReport;
import com.apriori.trr.api.testrail.model.Case;
import com.apriori.trr.api.testrail.model.CaseField;
import com.apriori.trr.api.testrail.model.Project;
import com.apriori.trr.api.testrail.utils.FileResourceUtil;
import com.apriori.trr.api.testrail.utils.TestRailUtil;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DemoReport {

    private static final Logger log = LoggerFactory.getLogger(DemoReport.class);

    @Test
    @TestRailRule(id = 1)
    public void getProjects() {
        TestRailConfig testRailConfig = new TestRailConfig();
        TestRail testRail = TestRail.builder(testRailConfig.getBaseApiUrl(),
            testRailConfig.getUsername(), testRailConfig.getPassword()).build();

        List<Project> pl = testRail.projects().list().execute();
        System.out.println(pl.get(0));
    }


    @Test
    public void testCoverageReport() {
        TestRailConfig testRailConfig = new TestRailConfig();
        TestRail testRail = TestRail.builder(testRailConfig.getBaseApiUrl(),
            testRailConfig.getUsername(), testRailConfig.getPassword()).build();

        List<Project> pl = testRail.projects().list().execute();
        System.out.println(pl.get(0));
    }

    @Test
    public void getCases() {
        TestRailConfig testRailConfig = new TestRailConfig();
        TestRail testRail = TestRail.builder(testRailConfig.getBaseApiUrl(),
            testRailConfig.getUsername(), testRailConfig.getPassword()).build();
        List<CaseField> caseFields = testRail.caseFields().list().execute();

        List<CaseField> caseFieldList = caseFields.stream().filter(caseField -> caseField.getId() == 14).collect(Collectors.toList());

        List<Case> testCases = testRail.cases().list(11).execute();
        testCases.stream().forEach(System.out::println);
        List<Case> automatedList = testCases.stream()
            .filter(testCase -> !(testCase.getCustomAutomationType() == null))
            .filter(testCase -> Integer.parseInt(testCase.getCustomAutomationType()) == 1)
            .collect(Collectors.toList());

        List<Case> automatableList = testCases.stream()
            .filter(testCase -> !(testCase.getCustomAutomationType() == null))
            .filter(testCase -> Integer.parseInt(testCase.getCustomAutomationType()) == 2)
            .collect(Collectors.toList());

        List<Case> manual = testCases.stream()
            .filter(testCase -> !(testCase.getCustomAutomationType() == null))
            .filter(testCase -> Integer.parseInt(testCase.getCustomAutomationType()) == 0)
            .collect(Collectors.toList());

        List<Case> nullCases = testCases.stream()
            .filter(testCase -> (testCase.getCustomAutomationType() == null))
            .collect(Collectors.toList());

        System.out.println(automatedList.size() * 100 / (automatedList.size() + automatableList.size()));

        System.out.println(manual.size());

    }

    @Test
    public void testReport() throws IOException {
        // Configure Thymeleaf
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        File file = null;
        try {
            file = new File(
                URLDecoder.decode(
                    ClassLoader.getSystemResource("image-template.html").getFile(),
                    "UTF-8"
                ));
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        String imageFile = FileResourceUtil.encodeImageToBase64(FileResourceUtil.getLocalResourceFile("barChart.jpeg").getPath());

        templateResolver.setPrefix(file.getParent());
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // Prepare context
        Context context = new Context();
        context.setVariable("title", "Report Title");
        context.setVariable("chart", imageFile);

        // Generate HTML content
        String htmlContent = templateEngine.process("/image-template.html", context);

        // Write HTML content to file
        try (FileWriter fileWriter = new FileWriter("report.html")) {
            fileWriter.write(htmlContent);
            System.out.println("Report generated successfully: report.html");
        } catch (IOException e) {
            System.out.println("Error writing HTML file: " + e.getMessage());
        }
    }


    @Test
    public void getTestCases() {
        List<ProjectTestCase> projectTestCases = new ArrayList<>();
        TestRailReport htmlReport = new TestRailReport();
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
