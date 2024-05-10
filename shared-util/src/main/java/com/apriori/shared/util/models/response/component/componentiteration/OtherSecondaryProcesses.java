package com.apriori.shared.util.models.response.component.componentiteration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherSecondaryProcesses {
    @JsonProperty("Hydrostatic Leak Testing")
    private HydroStaticLeakTesting hydroStaticLeakTesting;
}
