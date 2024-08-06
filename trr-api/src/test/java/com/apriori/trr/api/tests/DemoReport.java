package com.apriori.trr.api.tests;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class DemoReport {
    public static void main(String[] args) throws UnsupportedEncodingException {
        // Configure Thymeleaf
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("report-template.html");

        File file = new File(
            URLDecoder.decode(
                ClassLoader.getSystemResource("report-template.html").getFile(),
                "UTF-8"
            ));

        templateResolver.setPrefix(file.getParent());
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // Prepare context
        Context context = new Context();
        context.setVariable("title", "Report Title");

        List<List<String>> rows = new ArrayList<>();
        rows.add(List.of("Row 1 Data 1", "Row 1 Data 2"));
        rows.add(List.of("Row 2 Data 1", "Row 2 Data 2"));
        context.setVariable("rows", rows);

        // Generate HTML content
        String htmlContent = templateEngine.process("/report-template.html", context);

        // Write HTML content to file
        try (FileWriter fileWriter = new FileWriter("Reports/report.html")) {
            fileWriter.write(htmlContent);
            System.out.println("Report generated successfully: report.html");
        } catch (IOException e) {
            System.out.println("Error writing HTML file: " + e.getMessage());
        }
    }
}
