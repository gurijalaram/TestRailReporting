package com.apriori.cir.tests;

import com.apriori.cirapi.utils.JasperReportUtil;
import com.google.common.io.LineReader;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class JasperReportTest {

    @Test
    public void myJasperTest() {
        JasperReportUtil.myJasperTest();
    }


    @Test
    public void tt() {

        try {
            Scanner scanner = new Scanner(new File("E:\\junitTest.log"));

            while (scanner.hasNextLine()) {
                extractData(scanner.nextLine());
            }

            Long globalTime = metrics.get("TB_GLOBAL_TIME") / 1000;

            metrics.entrySet().stream().forEach(entry ->
                System.out.println(entry.getKey() + "    " + entry.getValue() / 1000 / 60+ " min (" + entry.getValue() / 1000 / 60 / 60 + "," + entry.getValue() / 1000 / 60 % 60 + " hr ) | " +
                    + 100 * (entry.getValue() / 1000) / globalTime + "%"));

            System.out.println("CM_VALIDATE_TIME time in seconds    " + metrics.get("CM_VALIDATE_TIME") + " milliseconds");
            System.out.println("Prepare files to validate time in seconds    " + metrics.get("CM_PREPARE_FILES_TO_VALIDATE_TIME") + " milliseconds");

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }


    }

    private List<String> arr = Arrays.asList("***CM_VALIDATE_TIME", "***CM_BULK_LOAD_TIME", "***CM_COST_AP_TIME", "***CM_INITIALIZE_TIME", "***CM_PREPARE_FILES_TO_VALIDATE_TIME", "***TB_SETUP_TIME", "***TB_GLOBAL_TIME");
    private Map<String, Long> metrics = new HashMap<>();

    private void extractData(String nextLine) {

        String key = StringUtils.substringBetween(nextLine, "***", ": [");

        if (!StringUtils.isBlank(key)) {
            Long value = Long.valueOf(StringUtils.substringBetween(nextLine, "[", "]"));
            metrics.merge(key, value, Long::sum);
        }
    }
}
//
//    private boolean lineContainsKey(String nextLine) {
//
//        for (String s : arr) {
//            if(nextLine.contains(s)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
