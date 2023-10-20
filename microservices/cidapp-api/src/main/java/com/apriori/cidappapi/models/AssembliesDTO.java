package com.apriori.cidappapi.models;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssembliesDTO {
    private List<ComponentInfoBuilder> assemblies;
    private List<ComponentInfoBuilder> components;
}
