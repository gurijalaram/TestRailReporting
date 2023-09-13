package com.apriori.qms.models.request.bidpackage;

import com.apriori.qms.models.response.component.ComponentAssignedParameters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignedComponentRequest {
    private List<ComponentAssignedParameters> components;
}