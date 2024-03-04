package com.apriori.cic.api.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConnectorJobPartsDataShape {
    public  ConnectorJobPartFieldDefinitions fieldDefinitions;
}
