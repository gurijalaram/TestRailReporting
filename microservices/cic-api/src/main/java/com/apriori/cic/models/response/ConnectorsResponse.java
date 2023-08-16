package com.apriori.cic.models.response;

import com.apriori.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "AgentConnectorsSchema.json")
public class ConnectorsResponse {
    private ConnectorResponseDataShape dataShape;
    private List<ConnectorInfo> rows;
}
