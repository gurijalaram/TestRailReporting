package com.apriori.shared.util.dto;

import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AssemblyDTORequest {
    private static final String ASSEMBLY_STORE = "AssemblyStore.json";
    private static final DTOReader DTO_READER = new DTOReader(ASSEMBLY_STORE);

    /**
     * Gets a random small assembly
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getAssembly() {
        ComponentInfoBuilder componentAssembly = DTO_READER.getAssembly();

        final UserCredentials user = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        componentAssembly.setUser(user);
        componentAssembly.setScenarioName(scenarioName);
        componentAssembly.setResourceFile(FileResourceUtil.getCloudFile(componentAssembly.getProcessGroup(), componentAssembly.getComponentName() + componentAssembly.getExtension()));
        return iterateSetSubcomponents(componentAssembly);
    }

    /**
     * Gets an assembly specified by name
     *
     * @param assembly - the assembly
     * @return component builder object
     */
    public ComponentInfoBuilder getAssembly(String assembly) {
        ComponentInfoBuilder componentAssembly = DTO_READER.getAssembly(assembly);

        final UserCredentials user = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        componentAssembly.setUser(user);
        componentAssembly.setScenarioName(scenarioName);
        componentAssembly.setResourceFile(FileResourceUtil.getCloudFile(componentAssembly.getProcessGroup(), componentAssembly.getComponentName() + componentAssembly.getExtension()));

        return iterateSetSubcomponents(componentAssembly);
    }

    /**
     * Gets an assembly and subcomponent(s) by name
     *
     * @param assembly          - the assembly
     * @param subcomponentNames - the subcomponent names
     * @return component builder object
     */
    public ComponentInfoBuilder getAssemblySubcomponents(String assembly, String... subcomponentNames) {
        ComponentInfoBuilder componentAssembly = DTO_READER.getAssembly(assembly);

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
        return iterateSetSubcomponents(componentAssembly);
    }

    /**
     * Gets a random medium assembly
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getMediumAssembly() {
        ComponentInfoBuilder componentAssembly = DTO_READER.getMediumAssembly();

        final UserCredentials user = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        componentAssembly.setUser(user);
        componentAssembly.setScenarioName(scenarioName);
        componentAssembly.setResourceFile(FileResourceUtil.getCloudFile(componentAssembly.getProcessGroup(), componentAssembly.getComponentName() + componentAssembly.getExtension()));

        return iterateSetSubcomponents(componentAssembly);
    }

    /**
     * Gets a random large assembly
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getLargeAssembly() {
        ComponentInfoBuilder componentAssembly = DTO_READER.getLargeAssembly();

        final UserCredentials user = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        componentAssembly.setUser(user);
        componentAssembly.setScenarioName(scenarioName);
        componentAssembly.setResourceFile(FileResourceUtil.getCloudFile(componentAssembly.getProcessGroup(), componentAssembly.getComponentName() + componentAssembly.getExtension()));

        return iterateSetSubcomponents(componentAssembly);
    }

    private ComponentInfoBuilder iterateSetSubcomponents(ComponentInfoBuilder componentAssembly) {
        componentAssembly.getSubComponents().forEach(o -> {
            o.setUser(componentAssembly.getUser());
            o.setScenarioName(componentAssembly.getScenarioName());
            o.setResourceFile(FileResourceUtil.getCloudFile(o.getProcessGroup(), o.getComponentName() + o.getExtension()));
            if (o.getSubComponents() != null) {
                o.getSubComponents().forEach(p -> {
                    p.setUser(o.getUser());
                    p.setScenarioName(o.getScenarioName());
                    p.setResourceFile(FileResourceUtil.getCloudFile(p.getProcessGroup(), p.getComponentName() + p.getExtension()));
                });
            }
        });
        return componentAssembly;
    }
}
