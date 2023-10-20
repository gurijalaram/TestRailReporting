package com.apriori.cidappapi.models.request;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.AssembliesDTO;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.json.JsonManager;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;

import org.openqa.selenium.NoSuchElementException;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AssemblyRequest {
    private ComponentInfoBuilder componentAssembly;
    private ComponentInfoBuilder component;

    /**
     * Gets a random assembly
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getAssembly() {
        Random random = new Random();
        final String jsonFile = "AssemblyStore.json";

        AssembliesDTO assemblyDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(jsonFile), AssembliesDTO.class);

        componentAssembly = assemblyDTO.getAssemblies().get(random.nextInt(assemblyDTO.getAssemblies().stream().findAny().get().getSubComponents().size() - 1));

        final UserCredentials user = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        componentAssembly.setUser(user);
        componentAssembly.setScenarioName(scenarioName);
        componentAssembly.getSubComponents().forEach(o -> {
            o.setUser(user);
            o.setScenarioName(scenarioName);
        });
        return componentAssembly;
    }

    /**
     * Gets a random component
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getComponent() {
        Random random = new Random();
        final String jsonFile = "AssemblyStore.json";

        AssembliesDTO assemblyDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(jsonFile), AssembliesDTO.class);

        component = assemblyDTO.getComponents().get(random.nextInt(assemblyDTO.getComponents().size() - 1));

        component.setResourceFile(FileResourceUtil.getCloudFile(component.getProcessGroup(), component.getComponentName() + component.getExtension()));
        component.setScenarioName(new GenerateStringUtil().generateScenarioName());
        component.setUser(UserUtil.getUser());

        return component;
    }

    /**
     * Gets an assembly specified by name
     *
     * @param assembly - the assembly
     * @return component builder object
     */
    public ComponentInfoBuilder getAssembly(String assembly) {
        final String jsonFile = "AssemblyStore.json";
        AssembliesDTO assemblyDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(jsonFile), AssembliesDTO.class);

        componentAssembly = assemblyDTO.getAssemblies()
            .stream()
            .filter(o -> o.getComponentName().equalsIgnoreCase(assembly))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("The assembly '%s' was not defined in the '%s' file", assembly, jsonFile)));

        final UserCredentials user = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        componentAssembly.setUser(user);
        componentAssembly.setScenarioName(scenarioName);
        componentAssembly.getSubComponents().forEach(o -> {
            o.setUser(user);
            o.setScenarioName(scenarioName);
        });
        return componentAssembly;
    }

    /**
     * Gets a component specified by name
     *
     * @param componentName - the part name
     * @return component builder object
     */
    public ComponentInfoBuilder getComponent(String componentName) {
        final String jsonFile = "AssemblyStore.json";
        AssembliesDTO assemblyDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(jsonFile), AssembliesDTO.class);

        component = assemblyDTO.getComponents()
            .stream()
            .filter(o -> o.getComponentName().equalsIgnoreCase(componentName))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("The part '%s' was not defined in the '%s' file", componentName, jsonFile)));

        component.setResourceFile(FileResourceUtil.getCloudFile(component.getProcessGroup(), component.getComponentName() + component.getExtension()));
        component.setScenarioName(new GenerateStringUtil().generateScenarioName());
        component.setUser(UserUtil.getUser());

        return component;
    }

    /**
     * Gets an assembly and subcomponent(s) by name
     *
     * @param assembly          - the assembly
     * @param subcomponentNames - the subcomponent names
     * @return component builder object
     */
    public ComponentInfoBuilder getAssemblySubcomponents(String assembly, String... subcomponentNames) {
        final String jsonFile = "AssemblyStore.json";
        AssembliesDTO assemblyDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(jsonFile), AssembliesDTO.class);

        componentAssembly = assemblyDTO.getAssemblies()
            .stream()
            .filter(o -> o.getComponentName().equalsIgnoreCase(assembly))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("The assembly '%s' was not defined in the '%s' file", assembly, jsonFile)));

        List<String> componentNames = Arrays.stream(subcomponentNames).collect(Collectors.toList());

        componentAssembly.setSubComponents(componentAssembly.getSubComponents()
            .stream()
            .filter(subassembly -> componentNames.contains(subassembly.getComponentName()))
            .collect(Collectors.toList()));

        final UserCredentials user = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        componentAssembly.setUser(user);
        componentAssembly.setScenarioName(scenarioName);
        componentAssembly.getSubComponents().forEach(o -> {
            o.setUser(user);
            o.setScenarioName(scenarioName);
        });
        return componentAssembly;
    }
}
