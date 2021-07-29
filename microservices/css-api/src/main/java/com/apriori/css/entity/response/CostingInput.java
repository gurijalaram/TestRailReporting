package com.apriori.css.entity.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostingInput {
    private CustomAttributes customAttributes;
    private ProcessSetupOptions processSetupOptions;
    private SecondaryProcesses secondaryProcesses;
    private SecondaryDigitalFactories secondaryDigitalFactories;
    private SecondaryVpes secondaryVpes;
    private Threads threads;
    private Tolerances tolerances;

    public static class CustomAttributes {
    }

    public static class ProcessSetupOptions {
    }

    public static class SecondaryProcesses {
    }

    public static class SecondaryDigitalFactories {
    }

    public static class SecondaryVpes {
    }

    public static class Threads {
    }

    public static class Tolerances {
    }
}

