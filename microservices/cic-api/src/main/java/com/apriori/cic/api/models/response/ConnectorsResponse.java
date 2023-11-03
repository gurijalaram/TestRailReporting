package com.apriori.cic.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "AgentConnectorsSchema.json")
public class ConnectorsResponse {
    private ConnectorResponseDataShape dataShape;
    private List<ConnectorInfo> rows;
}
