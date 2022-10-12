package com.apriori.acs.entity.response.acs.getavailableroutings;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetAvailableRoutingsResponse {
    private String name;
    private String plantName;
    private String processGroupName;
    private List<GetAvailableRoutingsResponse> children;
    private Boolean alternNode;
}
