package com.apriori.entity.response.upload;

import lombok.Data;

@Data
public class ManifestItem {
    private String modelName;
    private String cadType;
    private String baseName;
    private String fileName;
    private String occurrences;
}
