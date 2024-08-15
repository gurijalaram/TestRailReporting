package com.apriori.trr.api.testrail.controller;

import com.apriori.trr.api.testrail.TestRail;
import com.apriori.trr.api.testrail.exceptions.TestRailException;
import com.apriori.trr.api.testrail.model.Result;
import com.apriori.trr.api.testrail.model.ResultField;
import com.apriori.trr.api.testrail.model.Run;
import com.apriori.trr.api.testrail.properties.LoadProperties;
import com.apriori.trr.api.testrail.utils.FileResourceUtil;

import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * For original code to @see < href="https://github.com/codepine/testrail-api-java-client">test-api-client</>
 * For original code @see <a href="https://github.com/sandeeprao/testrail">TestRail</a>
 * Explanation @see <a href="https://medium.com/@sandeep12.rao/automating-unit-test-results-reporting-to-testrail-658d4bf97763">Code Explanation</a>
 */

@Slf4j
public class TestRailReport {

    private final static String htmlReportTemplate = "report-template.html";
    private final static String barChartFile = "Reports/barChart.jpeg";
    private final static String testCoverageReport = "Reports/TestCoverageReport.html";

    private int projectId;
    private List<Result> results;
    private JFreeChart jfreeChart;

    /**
     * Generate Project Test Coverage Report in tabular format
     *
     * @param projectTestCaseList - list of ProjectTestCase
     */
    public void generateCoverageReport(List<ProjectTestCase> projectTestCaseList) {
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        File file = FileResourceUtil.getLocalResourceFile(htmlReportTemplate);
        templateResolver.setPrefix(file.getParent());
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // Prepare context
        Context context = new Context();
        context.setVariable("title", "Coverage Report");

        List<List<String>> rows = new ArrayList<>();
        projectTestCaseList.stream().forEach(ptc -> {
            rows.add(List.of(ptc.getProjectID().toString(), ptc.getProjectName().toString(),
                ptc.getTotalCases().toString(), ptc.getAutomatable().toString(), ptc.getAutomated().toString(),
                ptc.getPercentageCovered().toString()));
        });

        File barChartImg = createCoverageBarChart(projectTestCaseList);
        context.setVariable("rows", rows);
        try {
            context.setVariable("chart", FileResourceUtil.encodeImageToBase64(barChartImg.getPath()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to encode bar chart image!!!");
        }
        // Generate HTML content
        String htmlContent = templateEngine.process("/" + htmlReportTemplate, context);

        // Write HTML content to file
        try (FileWriter fileWriter = new FileWriter(testCoverageReport)) {
            fileWriter.write(htmlContent);
            log.info("Report generated successfully: TestCoverageReport.html");
        } catch (IOException e) {
            log.error("Failed to Generate the report file: {}", e.getMessage());
        }
    }

    /**
     * Create Project test metrics to bar Chart
     *
     * @param projectTestCaseList - list of ProjectTestCase
     * @return image File
     */
    public File createCoverageBarChart(List<ProjectTestCase> projectTestCaseList) {
        DefaultCategoryDataset dataset = this.getDataSet(projectTestCaseList);
        this.jfreeChart = ChartFactory.createBarChart(
            "Coverage Report",
            "Project",
            "Percentage",
            dataset);

        File barChartImage = new File(barChartFile);
        // Customize the plot
        CategoryPlot plot = jfreeChart.getCategoryPlot();
        // Adjust margins
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        BarRenderer renderer = new BarRenderer();

        // Set bar width
        renderer.setMaximumBarWidth(0.2); // Width of bars, from 0.0 to 1.0

        // Set colors for different series
        renderer.setSeriesPaint(0, Color.BLUE);  // Color for Series1
        renderer.setSeriesPaint(1, Color.RED); // Color for Series2

        // Customize the plot area
        jfreeChart.getCategoryPlot().setRenderer(renderer);
        jfreeChart.getCategoryPlot().setBackgroundPaint(Color.LIGHT_GRAY);

        // Customize the jFreeChart background
        jfreeChart.setBackgroundPaint(Color.WHITE);

        // Save the jFreeChart as a PNG image
        try {
            ChartUtils.saveChartAsJPEG(barChartImage, jfreeChart, 800, 600);
        } catch (IOException ioException) {
            throw new RuntimeException("Failed create bar chart jpeg image !!!" + ioException.getMessage());
        }

        return barChartImage;
    }


    /**
     * add Result and project ID
     *
     * @param result    - Result
     * @param projectId - projectId
     */
    public void addResult(Result result, int projectId) {
        this.results = new ArrayList<>();
        results.add(result);
        this.projectId = projectId;
    }

    /**
     * Report the result to testrail
     */
    public void reportResults() {
        Properties properties = LoadProperties.loadProperties("testrail");
        String url = properties.getProperty("url").trim();
        String userId = properties.getProperty("username").trim();
        String pwd = properties.getProperty("password").trim();
        TestRail testRail = TestRail.builder(url, userId, pwd).build();

        Run run = testRail.runs().get(projectId).execute();
        List<ResultField> customResultFields = testRail.resultFields().list().execute();

        results.forEach(result -> {
            try {
                testRail.results().addForCase(run.getId(), result.getTestId(), result, customResultFields).execute();
            } catch (TestRailException tre) {
                log.info(tre.getMessage() + " Or may not be a part of current run. Run :- {}, Case ID :- {}", run.getId(), result.getTestId());
            }
        });
    }

    /**
     * Convert list of projectTestCase to DataSet
     *
     * @param projectTestCaseList - list of ProjectTestCase
     * @return DefaultCategoryDataset
     */
    private DefaultCategoryDataset getDataSet(List<ProjectTestCase> projectTestCaseList) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        projectTestCaseList.stream().forEach(ptc -> {
            dataset.addValue(ptc.getPercentageCovered(), "Percentage", ptc.getProjectName());
        });

        return dataset;
    }
}