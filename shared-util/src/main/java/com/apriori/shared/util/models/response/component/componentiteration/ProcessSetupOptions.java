package com.apriori.shared.util.models.response.component.componentiteration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessSetupOptions {
    private OtherSecondaryProcesses otherSecondaryProcesses;
}