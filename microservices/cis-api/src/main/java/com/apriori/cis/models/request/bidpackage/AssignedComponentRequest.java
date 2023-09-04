package com.apriori.cis.models.request.bidpackage;

import com.apriori.cis.models.response.component.ComponentParameters;

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
