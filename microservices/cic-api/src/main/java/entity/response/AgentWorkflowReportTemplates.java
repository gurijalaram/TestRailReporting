package entity.response;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "AgentReportTemplatesResponseSchema.json")
public class AgentWorkflowReportTemplates {
    public ReportTemplateDataShape dataShape;
    public List<ReportTemplatesRow> rows;
}