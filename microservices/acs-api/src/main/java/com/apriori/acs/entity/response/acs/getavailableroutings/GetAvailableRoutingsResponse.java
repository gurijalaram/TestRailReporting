package com.apriori.acs.entity.response.acs.getavailableroutings;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Schema(location = "acs/GetAvailableRoutingsResponse.json")
@NoArgsConstructor
public class GetAvailableRoutingsResponse {
    private String name;
    private String displayName;
    private String plantName;
    private String processGroupName;
    private Boolean optional;
    private Boolean included;
    private Boolean overriden;
    private Boolean alternNode;
    private List<AvailableRouting> children;

}
