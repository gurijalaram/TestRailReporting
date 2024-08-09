package com.apriori.trr.api.testrail.controller;

import com.apriori.trr.api.testrail.http.utils.FileResourceUtil;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlReport {

    private final static String htmlReportTemplate = "report-template.html";

    public void generateTestReport(List<ProjectTestCase> ptcs) {
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        File file = FileResourceUtil.getLocalResourceFile(htmlReportTemplate);
        templateResolver.setPrefix(file.getParent());
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // Prepare context
        Context context = new Context();
        context.setVariable("title", "Report Title");

        List<List<String>> rows = new ArrayList<>();
        ptcs.stream().forEach( ptc -> {
            rows.add(List.of(ptc.getProjectID().toString(),ptc.getProjectName().toString(),
                ptc.getTotalCases().toString(), ptc.getAutomatable().toString(), ptc.getAutomated().toString(),
                ptc.calculatePercentage().toString()));
        });
        context.setVariable("rows", rows);
        // Generate HTML content
        String htmlContent = templateEngine.process("/" + htmlReportTemplate, context);

        // Write HTML content to file
        try (FileWriter fileWriter = new FileWriter("TestCoverageReport.html")) {
            fileWriter.write(htmlContent);
            System.out.println("Report generated successfully: report.html");
        } catch (IOException e) {
            System.out.println("Error writing HTML file: " + e.getMessage());
        }
    }
}
