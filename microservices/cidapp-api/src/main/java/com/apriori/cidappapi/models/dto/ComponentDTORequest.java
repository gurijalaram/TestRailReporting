package com.apriori.cidappapi.models.dto;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.json.JsonManager;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;

import org.openqa.selenium.NoSuchElementException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ComponentDTORequest {
    private ComponentInfoBuilder component;
    private static final String COMPONENT_STORE = "ComponentStore.json";

    /**
     * Gets a random component
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getComponent() {

        ComponentDTO componentDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(COMPONENT_STORE), ComponentDTO.class);

        List<ComponentInfoBuilder> listOfComponents = componentDTO.getComponents();
        Collections.shuffle(listOfComponents);

        component = listOfComponents.stream().findFirst().get();

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

        ComponentDTO componentDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(COMPONENT_STORE), ComponentDTO.class);

        List<ComponentInfoBuilder> listOfComponents = componentDTO.getComponents();
        Collections.shuffle(listOfComponents);

        List<ComponentInfoBuilder> components = listOfComponents.subList(0, noOfComponents);

        final UserCredentials currentUser = UserUtil.getUser();
        components.forEach(component -> {
            component.setResourceFile(FileResourceUtil.getCloudFile(component.getProcessGroup(), component.getComponentName() + component.getExtension()));
            component.setUser(currentUser);
            component.setScenarioName(new GenerateStringUtil().generateScenarioName());
        });

        return components;
    }

    /**
     * Gets a component specified by name
     *
     * @param componentName - the part name
     * @return component builder object
     */
    public ComponentInfoBuilder getComponent(String componentName) {

        ComponentDTO componentDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(COMPONENT_STORE), ComponentDTO.class);

        component = componentDTO.getComponents()
            .stream()
            .filter(o -> o.getComponentName().equalsIgnoreCase(componentName))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("The part '%s' was not defined in the '%s' file", componentName, COMPONENT_STORE)));

        component.setResourceFile(FileResourceUtil.getCloudFile(component.getProcessGroup(), component.getComponentName() + component.getExtension()));
        component.setScenarioName(new GenerateStringUtil().generateScenarioName());
        component.setUser(UserUtil.getUser());

        return component;
    }

    /**
     * Gets random component by extension
     * The first dot (.) should be ignored e.g. getComponentByExtension("stp")
     *
     * @param extension - the extension
     * @return component builder object
     */
    public ComponentInfoBuilder getComponentByExtension(String extension) {

        ComponentDTO componentDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(COMPONENT_STORE), ComponentDTO.class);

        List<ComponentInfoBuilder> componentExtension = componentDTO.getComponents().stream()
            .filter(component -> component.getExtension().equalsIgnoreCase("." + extension)).collect(Collectors.toList());
        Collections.shuffle(componentExtension);

        ComponentInfoBuilder componentInfoExtension = componentExtension.stream().findFirst().get();

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

        ComponentDTO componentDTO = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(COMPONENT_STORE), ComponentDTO.class);

        List<ComponentInfoBuilder> componentPG = componentDTO.getComponents().stream()
            .filter(component -> component.getProcessGroup().equals(processGroup)).collect(Collectors.toList());
        Collections.shuffle(componentPG);

        ComponentInfoBuilder componentInfoPG = componentPG.stream().findFirst().get();

        componentInfoPG.setResourceFile(FileResourceUtil.getCloudFile(componentInfoPG.getProcessGroup(),
            componentInfoPG.getComponentName() + componentInfoPG.getExtension()));
        componentInfoPG.setScenarioName(new GenerateStringUtil().generateScenarioName());
        componentInfoPG.setUser(UserUtil.getUser());
        return componentInfoPG;
    }
}
