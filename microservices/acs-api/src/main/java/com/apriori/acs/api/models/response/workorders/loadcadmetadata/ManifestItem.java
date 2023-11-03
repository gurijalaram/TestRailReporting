package com.apriori.acs.api.models.response.workorders.loadcadmetadata;

import lombok.Data;

@Data
public class ManifestItem {
    private String modelName;
    private String cadType;
    private String baseName;
    private String fileName;
    private String occurrences;
}
