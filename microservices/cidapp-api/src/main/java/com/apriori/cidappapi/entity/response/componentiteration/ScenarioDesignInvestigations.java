package com.apriori.cidappapi.entity.response.componentiteration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScenarioDesignInvestigations {
    private String identity;
    private List<Data_> data;
    private String investigationType;
}
