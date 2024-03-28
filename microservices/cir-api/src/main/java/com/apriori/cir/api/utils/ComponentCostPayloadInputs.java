package com.apriori.cir.api.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComponentCostPayloadInputs {
    private ComponentCostPayloadInputsItem exportSetName;
    private ComponentCostPayloadInputsItem componentType;
    private ComponentCostPayloadInputsItem latestExportDate;
    private ComponentCostPayloadInputsItem createdBy;
    private ComponentCostPayloadInputsItem lastModifiedBy;
    private ComponentCostPayloadInputsItem componentNumber;
    private ComponentCostPayloadInputsItem scenarioName;
    private ComponentCostPayloadInputsItem componentSelect;
    private ComponentCostPayloadInputsItem componentCostCurrencyCode;
}
