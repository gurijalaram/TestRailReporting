package com.apriori.qms.entity.response.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomAttributes {
    private int workspaceId;
    private String defaultRole;
    private List<Object> roles;
    private String location;
    private String department;
}
