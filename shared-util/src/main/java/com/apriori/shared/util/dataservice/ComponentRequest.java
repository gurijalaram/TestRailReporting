package com.apriori.shared.util.dataservice;

import com.apriori.shared.util.builder.ComponentInfoBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComponentRequest {
    private List<ComponentInfoBuilder> assemblies;
    private List<ComponentInfoBuilder> components;
}
