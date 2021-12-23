package com.apriori.cidappapi.utils;

import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ComponentInfoBuilder {
    private final String componentName;
    private final String scenarioName;
    private final String componentId;
    private final String scenarioId;
    private final ProcessGroupEnum processGroup;
    private final DigitalFactoryEnum digitalFactory;
    private final String mode;
    private final String material;
    private final UserCredentials user;
}
