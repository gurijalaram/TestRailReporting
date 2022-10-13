package com.apriori.acs.entity.response.acs.getavailableroutings;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetAvailableRoutingsResponse {
    private String name;
    private String displayName;
    private String plantName;
    private String processGroupName;
    private Boolean optional;
    private Boolean included;
    private Boolean overriden;
    private Boolean alternNode;
    private List<GetAvailableRoutingsResponse> children;
    private String costStatus;

}
