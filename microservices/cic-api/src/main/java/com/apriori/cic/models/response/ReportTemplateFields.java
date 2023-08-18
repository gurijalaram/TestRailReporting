package com.apriori.cic.models.response;

import com.apriori.cic.models.request.FieldDefinitionKey;

import lombok.Data;

@Data
public class ReportTemplateFields {
    public FieldDefinitionKey displayName;
    public FieldDefinitionKey isDisabled;
    public FieldDefinitionKey sectionName;
    public FieldDefinitionKey value;
}
