package com.apriori.cidappapi.models.request;

import com.apriori.models.request.component.GroupItems;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForkRequest {
    @Builder.Default
    private Boolean override = false;
    private String scenarioName;
    private List<GroupItems> groupItems;
}
