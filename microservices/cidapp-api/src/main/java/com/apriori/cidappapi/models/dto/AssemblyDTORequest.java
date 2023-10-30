package com.apriori.cidappapi.models.dto;

import com.apriori.builder.ComponentInfoBuilder;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AssemblyDTORequest {
    private ComponentInfoBuilder componentAssembly;
    private static final String ASSEMBLY_STORE = "AssemblyStore.json";
    private static final DTOReader DTO_READER = new DTOReader(ASSEMBLY_STORE);

    /**
     * Gets a random assembly
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getAssembly() {

        componentAssembly = DTO_READER.getAssembly();

        final UserCredentials user = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        componentAssembly.setUser(user);
        componentAssembly.setScenarioName(scenarioName);
        componentAssembly.setResourceFile(FileResourceUtil.getCloudFile(componentAssembly.getProcessGroup(), componentAssembly.getComponentName() + componentAssembly.getExtension()));
        componentAssembly.getSubComponents().forEach(o -> {
            o.setUser(user);
            o.setScenarioName(scenarioName);
            o.setResourceFile(FileResourceUtil.getCloudFile(o.getProcessGroup(), o.getComponentName() + o.getExtension()));
        });
        return componentAssembly;
    }

    /**
     * Gets an assembly specified by name
     *
     * @param assembly - the assembly
     * @return component builder object
     */
    public ComponentInfoBuilder getAssembly(String assembly) {

        componentAssembly = DTO_READER.getAssembly(assembly);

        final UserCredentials user = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        componentAssembly.setUser(user);
        componentAssembly.setScenarioName(scenarioName);
        componentAssembly.setResourceFile(FileResourceUtil.getCloudFile(componentAssembly.getProcessGroup(), componentAssembly.getComponentName() + componentAssembly.getExtension()));
        componentAssembly.getSubComponents().forEach(o -> {
            o.setUser(user);
            o.setScenarioName(scenarioName);
            o.setResourceFile(FileResourceUtil.getCloudFile(o.getProcessGroup(), o.getComponentName() + o.getExtension()));
        });
        return componentAssembly;
    }

    /**
     * Gets an assembly and subcomponent(s) by name
     *
     * @param assembly          - the assembly
     * @param subcomponentNames - the subcomponent names
     * @return component builder object
     */
    public ComponentInfoBuilder getAssemblySubcomponents(String assembly, String... subcomponentNames) {

        componentAssembly = DTO_READER.getAssembly(assembly);

        List<String> componentNames = Arrays.stream(subcomponentNames).collect(Collectors.toList());

        componentAssembly.setSubComponents(componentAssembly.getSubComponents()
            .stream()
            .filter(subassembly -> componentNames.contains(subassembly.getComponentName()))
            .collect(Collectors.toList()));

        final UserCredentials user = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        componentAssembly.setUser(user);
        componentAssembly.setScenarioName(scenarioName);
        componentAssembly.setResourceFile(FileResourceUtil.getCloudFile(componentAssembly.getProcessGroup(), componentAssembly.getComponentName() + componentAssembly.getExtension()));
        componentAssembly.getSubComponents().forEach(o -> {
            o.setUser(user);
            o.setScenarioName(scenarioName);
            o.setResourceFile(FileResourceUtil.getCloudFile(o.getProcessGroup(), o.getComponentName() + o.getExtension()));
        });
        return componentAssembly;
    }
}
