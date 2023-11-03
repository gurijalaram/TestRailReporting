package com.apriori.cis.api.models.request.bidpackage;

import com.apriori.cis.api.models.response.component.ComponentParameters;

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
    private List<ComponentParameters> components;
}
