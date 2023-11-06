package com.apriori.cic.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "AgentReportTemplatesResponseSchema.json")
public class AgentWorkflowReportTemplates {
    public ReportTemplateDataShape dataShape;
    public List<ReportTemplatesRow> rows;
}
