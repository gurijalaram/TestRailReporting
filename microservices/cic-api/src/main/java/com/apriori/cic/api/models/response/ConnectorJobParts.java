package com.apriori.cic.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "ConnectorJobPartsResponseSchema.json")
public class ConnectorJobParts {
    public ConnectorJobPartsDataShape dataShape;
    public List<ConnectorJobPart> rows;
}
