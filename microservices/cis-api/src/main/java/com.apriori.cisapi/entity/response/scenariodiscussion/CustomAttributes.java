package com.apriori.cisapi.entity.response.scenariodiscussion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomAttributes {
    private String location;
    private Integer workspaceId;
}
