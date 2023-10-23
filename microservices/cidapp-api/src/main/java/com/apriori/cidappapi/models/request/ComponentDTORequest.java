package com.apriori.cidappapi.models.request;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.ComponentDTO;
import com.apriori.enums.ProcessGroupEnum;
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

public class ComponentDTORequest {
    private ComponentInfoBuilder componentAssembly;
    private ComponentInfoBuilder component;

    /**
     * Gets a random assembly
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getAssembly() {
        Random random = new Random();
        final String jsonFile = "ComponentStore.json";

        ComponentDTO assemblyDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(jsonFile), ComponentDTO.class);

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
        final String jsonFile = "ComponentStore.json";

        ComponentDTO assemblyDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(jsonFile), ComponentDTO.class);

        component = assemblyDTO.getComponents().get(random.nextInt(assemblyDTO.getComponents().size()));

        component.setResourceFile(FileResourceUtil.getCloudFile(component.getProcessGroup(), component.getComponentName() + component.getExtension()));
        component.setScenarioName(new GenerateStringUtil().generateScenarioName());
        component.setUser(UserUtil.getUser());

        return component;
    }

    /**
     * Gets a number of components
     *
     * @param noOfComponents - the number of components
     * @return component builder object
     */
    public List<ComponentInfoBuilder> getComponent(int noOfComponents) {
        final String jsonFile = "ComponentStore.json";

        ComponentDTO assemblyDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(jsonFile), ComponentDTO.class);

        List<ComponentInfoBuilder> components = assemblyDTO.getComponents().subList(0, noOfComponents);

        components.forEach(component -> {
            component.setResourceFile(FileResourceUtil.getCloudFile(component.getProcessGroup(), component.getComponentName() + component.getExtension()));
            component.setUser(UserUtil.getUser());
            component.setScenarioName(new GenerateStringUtil().generateScenarioName());
        });

        return components;
    }

    /**
     * Gets an assembly specified by name
     *
     * @param assembly - the assembly
     * @return component builder object
     */
    public ComponentInfoBuilder getAssembly(String assembly) {
        final String jsonFile = "ComponentStore.json";
        ComponentDTO assemblyDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(jsonFile), ComponentDTO.class);

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
        final String jsonFile = "ComponentStore.json";
        ComponentDTO assemblyDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(jsonFile), ComponentDTO.class);

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
        final String jsonFile = "ComponentStore.json";
        ComponentDTO assemblyDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(jsonFile), ComponentDTO.class);

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

    /**
     * Gets random component by extension
     * The first dot (.) should be ignored e.g. getComponentByExtension("stp")
     *
     * @param extension - the extension
     * @return component builder object
     */
    public ComponentInfoBuilder getComponentByExtension(String extension) {
        Random random = new Random();
        final String jsonFile = "ComponentStore.json";

        ComponentDTO assemblyDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(jsonFile), ComponentDTO.class);

        List<ComponentInfoBuilder> componentExtension = assemblyDTO.getComponents().stream()
            .filter(component -> component.getExtension().equalsIgnoreCase("." + extension)).collect(Collectors.toList());
        ComponentInfoBuilder componentInfoExtension = componentExtension.get(random.nextInt(componentExtension.size()));

        componentInfoExtension.setResourceFile(FileResourceUtil.getCloudFile(componentInfoExtension.getProcessGroup(),
            componentInfoExtension.getComponentName() + componentInfoExtension.getExtension()));
        componentInfoExtension.setScenarioName(new GenerateStringUtil().generateScenarioName());
        componentInfoExtension.setUser(UserUtil.getUser());
        return componentInfoExtension;
    }

    /**
     * Gets random component by process group
     *
     * @param processGroup - the process group
     * @return component builder object
     */
    public ComponentInfoBuilder getComponentByProcessGroup(ProcessGroupEnum processGroup) {
        Random random = new Random();
        final String jsonFile = "ComponentStore.json";

        ComponentDTO assemblyDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(jsonFile), ComponentDTO.class);

        List<ComponentInfoBuilder> componentPG = assemblyDTO.getComponents().stream()
            .filter(component -> component.getProcessGroup().equals(processGroup)).collect(Collectors.toList());
        ComponentInfoBuilder componentInfoPG = componentPG.get(random.nextInt(componentPG.size()));

        componentInfoPG.setResourceFile(FileResourceUtil.getCloudFile(componentInfoPG.getProcessGroup(), componentInfoPG.getComponentName() + componentInfoPG.getExtension()));
        componentInfoPG.setScenarioName(new GenerateStringUtil().generateScenarioName());
        componentInfoPG.setUser(UserUtil.getUser());
        return componentInfoPG;
    }
}
