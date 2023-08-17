package com.apriori.cic.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportTemplatesRow {
    private String displayName;
    private String sectionName;
    private String value;
}
