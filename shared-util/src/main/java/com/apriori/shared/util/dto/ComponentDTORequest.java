package com.apriori.shared.util.dto;

import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;

import org.openqa.selenium.NoSuchElementException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ComponentDTORequest {
    private ComponentInfoBuilder component;
    private static final String COMPONENT_STORE = "ComponentStore.json";
    private static final DTOReader DTO_READER = new DTOReader(COMPONENT_STORE);

    /**
     * Gets a random component
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getComponent() {

        List<ComponentInfoBuilder> listOfComponents = DTO_READER.getComponents();
        Collections.shuffle(listOfComponents);

        component = listOfComponents.stream().findFirst().get();

        component.setResourceFile(FileResourceUtil.getCloudFile(component.getProcessGroup(), component.getComponentName() + component.getExtension()));
        component.setScenarioName(new GenerateStringUtil().generateScenarioName());
        component.setUser(UserUtil.getUser());

        return component;
    }

    /**
     * Gets a component specified by name
     *
     * @param componentName - the part name
     * @return component builder object
     */
    public ComponentInfoBuilder getComponent(String componentName) {

        component = DTO_READER.getComponents()
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
     * Gets a number of components
     *
     * @param noOfComponents - the number of components
     * @return component builder object
     */
    public List<ComponentInfoBuilder> getComponents(int noOfComponents) {

        List<ComponentInfoBuilder> listOfComponents = DTO_READER.getComponents();
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
     * Gets random component by extension
     * The first dot (.) should be ignored e.g. getComponentByExtension("stp")
     *
     * @param extension - the extension
     * @return component builder object
     */
    public ComponentInfoBuilder getComponentByExtension(String extension) {

        List<ComponentInfoBuilder> componentExtension = DTO_READER.getComponents()
            .stream()
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

        List<ComponentInfoBuilder> componentPG = DTO_READER.getComponents()
            .stream()
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