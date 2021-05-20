package com.apriori.entity.response.css;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostingInput {
    private CustomAttributes customAttributes;
    private ProcessSetupOptions processSetupOptions;
    private SecondaryProcesses secondaryProcesses;
    private SecondaryVpes secondaryVpes;
    private Threads threads;
    private Tolerances tolerances;

    static class CustomAttributes {
    }

    static class ProcessSetupOptions {
    }

    static class SecondaryProcesses {
    }

    static class SecondaryVpes {
    }

    static class Threads {
    }

    static class Tolerances {
    }
}

