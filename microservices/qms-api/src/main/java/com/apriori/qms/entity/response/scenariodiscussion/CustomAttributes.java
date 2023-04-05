package com.apriori.qms.entity.response.scenariodiscussion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomAttributes {
    private String location;
    private Integer workspaceId;
    private String defaultRole;
    private List<Object> roles;
}
