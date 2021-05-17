
package com.apriori.cidapp.entity.response.css;

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
}

class CustomAttributes {
}

class ProcessSetupOptions {
}

class SecondaryProcesses {
}

class SecondaryVpes {
}

class Threads {
}

class Tolerances {
}

