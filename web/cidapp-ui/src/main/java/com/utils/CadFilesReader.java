package com.utils;

import com.apriori.utils.FileResourceUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CadFilesReader {
    
    public void fileReader() {
        File resourceFile = FileResourceUtil.getResourceAsFile("test-parts3.csv");
        String path = "C:/wip/new/apriori-qa/web/cidapp-ui/src/test/resources/test-parts3.csv";
        List<MultiUpload> lines = new ArrayList<>();
        String line;

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                String[] values = line.split(" ");
                for (String data : values) {
                    System.out.println(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readCSV(){
        File resourceFile = FileResourceUtil.getResourceAsFile("test-parts3.csv");
        List<MultiUpload> lines = new ArrayList<>();
        String line;

        try (BufferedReader br =
                 new BufferedReader(new FileReader(resourceFile))) {
            while((line = br.readLine()) != null){
                MultiUpload values = (MultiUpload) Arrays.asList(line.split(","));
                lines.add(values);
                System.out.println(values);
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
