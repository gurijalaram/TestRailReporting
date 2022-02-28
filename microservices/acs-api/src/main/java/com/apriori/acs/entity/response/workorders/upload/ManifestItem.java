package com.apriori.acs.entity.response.workorders.upload;

import lombok.Data;

@Data
public class ManifestItem {
    private String modelName;
    private String cadType;
    private String baseName;
    private String fileName;
    private String occurrences;
}
