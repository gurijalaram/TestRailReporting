package com.apriori.utils.reader.file.part;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartData {

    @JsonProperty("filename")
    private String fileName;
    private File file;
    private String processGroup;
    private String material;
    private String digitalFactory;
    private Integer annualVolume;
    @JsonProperty("secondaryDigitalFactory")
    private String secondary;
    private Double years;
    private Integer batchSize;
}
