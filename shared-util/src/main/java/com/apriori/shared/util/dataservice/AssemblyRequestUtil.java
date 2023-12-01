package com.apriori.shared.util.dataservice;

import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AssemblyRequestUtil {
    private static final String ASSEMBLY_STORE = "AssemblyStore.json";
    private static final PartRequestManager ASSEMBLY_REQUEST = new PartRequestManager(ASSEMBLY_STORE);

    /**
     * Gets a random small assembly
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getAssembly() {
        ComponentInfoBuilder assembly = ASSEMBLY_REQUEST.getAssembly();

        final UserCredentials user = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        assembly.setUser(user);
        assembly.setScenarioName(scenarioName);
        assembly.setResourceFile(FileResourceUtil.getCloudFile(assembly.getProcessGroup(), assembly.getComponentName() + assembly.getExtension()));

        return iterateSetSubcomponents(assembly);
    }

    /**
     * Gets an assembly specified by name
     *
     * @param assemblyName - the assembly
     * @return component builder object
     */
    public ComponentInfoBuilder getAssembly(String assemblyName) {
        ComponentInfoBuilder assembly = ASSEMBLY_REQUEST.getAssembly(assemblyName);

        final UserCredentials user = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        assembly.setUser(user);
        assembly.setScenarioName(scenarioName);
        assembly.setResourceFile(FileResourceUtil.getCloudFile(assembly.getProcessGroup(), assembly.getComponentName() + assembly.getExtension()));

        return iterateSetSubcomponents(assembly);
    }

    /**
     * Gets an assembly and subcomponent(s) by name
     *
     * @param assemblyName      - the assembly
     * @param subcomponentNames - the subcomponent names
     * @return component builder object
     */
    public ComponentInfoBuilder getAssemblySubcomponents(String assemblyName, String... subcomponentNames) {
        ComponentInfoBuilder assembly = ASSEMBLY_REQUEST.getAssembly(assemblyName);

        List<String> componentNames = Arrays.stream(subcomponentNames).collect(Collectors.toList());

        assembly.setSubComponents(assembly.getSubComponents()
            .stream()
            .filter(subassembly -> componentNames.contains(subassembly.getComponentName()))
            .collect(Collectors.toList()));

        final UserCredentials user = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        assembly.setUser(user);
        assembly.setScenarioName(scenarioName);
        assembly.setResourceFile(FileResourceUtil.getCloudFile(assembly.getProcessGroup(), assembly.getComponentName() + assembly.getExtension()));

        return iterateSetSubcomponents(assembly);
    }

    /**
     * Gets a random medium assembly
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getMediumAssembly() {
        ComponentInfoBuilder assembly = ASSEMBLY_REQUEST.getMediumAssembly();

        final UserCredentials user = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        assembly.setUser(user);
        assembly.setScenarioName(scenarioName);
        assembly.setResourceFile(FileResourceUtil.getCloudFile(assembly.getProcessGroup(), assembly.getComponentName() + assembly.getExtension()));

        return iterateSetSubcomponents(assembly);
    }

    /**
     * Gets a random large assembly
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getLargeAssembly() {
        ComponentInfoBuilder assembly = ASSEMBLY_REQUEST.getLargeAssembly();

        final UserCredentials user = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        assembly.setUser(user);
        assembly.setScenarioName(scenarioName);
        assembly.setResourceFile(FileResourceUtil.getCloudFile(assembly.getProcessGroup(), assembly.getComponentName() + assembly.getExtension()));

        return iterateSetSubcomponents(assembly);
    }

    private ComponentInfoBuilder iterateSetSubcomponents(ComponentInfoBuilder assembly) {
        assembly.getSubComponents().forEach(subcomponent -> {
            subcomponent.setUser(assembly.getUser());
            subcomponent.setScenarioName(assembly.getScenarioName());
            subcomponent.setResourceFile(FileResourceUtil.getCloudFile(subcomponent.getProcessGroup(), subcomponent.getComponentName() + subcomponent.getExtension()));

            if (subcomponent.getSubComponents() != null) {
                iterateSetSubcomponents(subcomponent);
            }
        });
        return assembly;
    }
}
