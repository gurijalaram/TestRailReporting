package com.apriori.shared.util.dataservice;

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

public class ComponentRequestUtil {
    private static final String COMPONENT_STORE = "ComponentStore.json";
    private static final PartRequestManager COMPONENT_REQUEST = new PartRequestManager(COMPONENT_STORE);
    private ComponentInfoBuilder component;

    /**
     * Gets a random component
     * N.B The part name is unique
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getComponent() {

        List<ComponentInfoBuilder> listOfComponents = COMPONENT_REQUEST.getComponents();
        Collections.shuffle(listOfComponents);

        component = listOfComponents.stream().findFirst().get();

        component.setResourceFile(FileResourceUtil.getS3FileAndSaveWithUniqueName(component.getComponentName().concat(component.getExtension()), component.getProcessGroup()));
        component.setComponentName(component.getResourceFile().getName().split("\\.", 2)[0]);
        component.setScenarioName(new GenerateStringUtil().generateScenarioName());
        component.setUser(UserUtil.getUser());

        return component;
    }

    /**
     * Gets a two model component
     * N.B The part name is unique
     *
     * @param componentName - the part name
     * @return component builder object
     */
    public ComponentInfoBuilder getTwoModelComponent(String componentName) {

        component = COMPONENT_REQUEST.getTwoModelComponents()
            .stream()
            .filter(component -> component.getComponentName().equalsIgnoreCase(componentName))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("The part '%s' was not defined in the '%s' file", componentName, COMPONENT_STORE)));

        component.setResourceFile(FileResourceUtil.getS3FileAndSaveWithUniqueName(component.getComponentName().concat(component.getExtension()), component.getProcessGroup()));
        component.setComponentName(component.getResourceFile().getName().split("\\.", 2)[0]);
        component.setScenarioName(new GenerateStringUtil().generateScenarioName());
        component.setUser(UserUtil.getUser());

        return component;
    }

    /**
     * Gets a component specified by name
     * N.B The part name is unique
     *
     * @param componentName - the part name
     * @return component builder object
     */
    public ComponentInfoBuilder getComponent(String componentName) {

        component = COMPONENT_REQUEST.getComponents()
            .stream()
            .filter(component -> component.getComponentName().equalsIgnoreCase(componentName))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("The part '%s' was not defined in the '%s' file", componentName, COMPONENT_STORE)));

        component.setResourceFile(FileResourceUtil.getS3FileAndSaveWithUniqueName(component.getComponentName().concat(component.getExtension()), component.getProcessGroup()));
        component.setComponentName(component.getResourceFile().getName().split("\\.", 2)[0]);
        component.setScenarioName(new GenerateStringUtil().generateScenarioName());
        component.setUser(UserUtil.getUser());

        return component;
    }

    /**
     * Gets a component specified by name
     * N.B The part name is not unique
     *
     * @param componentName - the part name
     * @return component builder object
     */
    public ComponentInfoBuilder getCloudComponent(String componentName) {

        component = COMPONENT_REQUEST.getComponents()
            .stream()
            .filter(component -> component.getComponentName().equalsIgnoreCase(componentName))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("The part '%s' was not defined in the '%s' file", componentName, COMPONENT_STORE)));

        component.setResourceFile(FileResourceUtil.getCloudFile(component.getProcessGroup(), component.getComponentName() + component.getExtension()));
        component.setScenarioName(new GenerateStringUtil().generateScenarioName());
        component.setUser(UserUtil.getUser());

        return component;
    }

    /**
     * Gets a component specified by name and extension
     * The first dot (.) should be ignored e.g. getComponentByExtension("stp")
     * N.B The part name is unique
     *
     * @param componentName - the part name
     * @param extension     - the extension
     * @return component builder object
     */
    public ComponentInfoBuilder getComponentWithExtension(String componentName, String extension) {

        component = COMPONENT_REQUEST.getComponents()
            .stream()
            .filter(component -> component.getComponentName().equalsIgnoreCase(componentName))
            .filter(component -> component.getExtension().equalsIgnoreCase("." + extension))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("The part '%s' was not defined in the '%s' file", componentName, COMPONENT_STORE)));

        component.setResourceFile(FileResourceUtil.getS3FileAndSaveWithUniqueName(component.getComponentName().concat(component.getExtension()), component.getProcessGroup()));
        component.setComponentName(component.getResourceFile().getName().split("\\.", 2)[0]);
        component.setScenarioName(new GenerateStringUtil().generateScenarioName());
        component.setUser(UserUtil.getUser());

        return component;
    }

    /**
     * Gets a component specified by name and process group
     * N.B The part name is unique
     *
     * @param componentName - the part name
     * @param processGroup  - the process group
     * @return component builder object
     */
    public ComponentInfoBuilder getComponentWithProcessGroup(String componentName, ProcessGroupEnum processGroup) {

        component = COMPONENT_REQUEST.getComponents()
            .stream()
            .filter(component -> component.getComponentName().equalsIgnoreCase(componentName))
            .filter(component -> component.getProcessGroup().equals(processGroup))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("The part '%s' was not defined in the '%s' file", componentName, COMPONENT_STORE)));

        component.setResourceFile(FileResourceUtil.getS3FileAndSaveWithUniqueName(component.getComponentName().concat(component.getExtension()), component.getProcessGroup()));
        component.setComponentName(component.getResourceFile().getName().split("\\.", 2)[0]);
        component.setScenarioName(new GenerateStringUtil().generateScenarioName());
        component.setUser(UserUtil.getUser());

        return component;
    }

    /**
     * Gets a number of components
     * N.B The part name is unique
     *
     * @param noOfComponents - the number of components
     * @return component builder object
     */
    public List<ComponentInfoBuilder> getComponents(int noOfComponents) {

        List<ComponentInfoBuilder> listOfComponents = COMPONENT_REQUEST.getComponents();
        Collections.shuffle(listOfComponents);

        List<ComponentInfoBuilder> components = listOfComponents.subList(0, noOfComponents);

        final UserCredentials currentUser = UserUtil.getUser();
        components.forEach(component -> {
            component.setResourceFile(FileResourceUtil.getS3FileAndSaveWithUniqueName(component.getComponentName().concat(component.getExtension()), component.getProcessGroup()));
            component.setComponentName(component.getResourceFile().getName().split("\\.", 2)[0]);
            component.setScenarioName(new GenerateStringUtil().generateScenarioName());
            component.setUser(currentUser);
        });

        return components;
    }

    /**
     * Gets random component by extension
     * The first dot (.) should be ignored e.g. getComponentByExtension("stp")
     * N.B The part name is unique
     *
     * @param extension - the extension
     * @return component builder object
     */
    public ComponentInfoBuilder getComponentByExtension(String extension) {

        List<ComponentInfoBuilder> componentExtension = COMPONENT_REQUEST.getComponents()
            .stream()
            .filter(component -> component.getExtension().equalsIgnoreCase("." + extension)).collect(Collectors.toList());
        Collections.shuffle(componentExtension);

        ComponentInfoBuilder componentInfoExtension = componentExtension.stream().findFirst().get();

        componentInfoExtension.setResourceFile(FileResourceUtil.getS3FileAndSaveWithUniqueName(componentInfoExtension.getComponentName() + componentInfoExtension.getExtension(),
            componentInfoExtension.getProcessGroup()));
        componentInfoExtension.setComponentName(componentInfoExtension.getResourceFile().getName().split("\\.", 2)[0]);
        componentInfoExtension.setScenarioName(new GenerateStringUtil().generateScenarioName());
        componentInfoExtension.setUser(UserUtil.getUser());

        return componentInfoExtension;
    }

    /**
     * Gets random component by process group
     * N.B The part name is unique
     *
     * @param processGroup - the process group
     * @return component builder object
     */
    public ComponentInfoBuilder getComponentByProcessGroup(ProcessGroupEnum processGroup) {

        List<ComponentInfoBuilder> componentPG = COMPONENT_REQUEST.getComponents()
            .stream()
            .filter(component -> component.getProcessGroup().equals(processGroup)).collect(Collectors.toList());
        Collections.shuffle(componentPG);

        ComponentInfoBuilder componentInfoPG = componentPG.stream().findFirst().get();

        componentInfoPG.setResourceFile(FileResourceUtil.getS3FileAndSaveWithUniqueName(componentInfoPG.getComponentName() + componentInfoPG.getExtension(),
            componentInfoPG.getProcessGroup()));
        componentInfoPG.setComponentName(componentInfoPG.getResourceFile().getName().split("\\.", 2)[0]);
        componentInfoPG.setScenarioName(new GenerateStringUtil().generateScenarioName());
        componentInfoPG.setUser(UserUtil.getUser());

        return componentInfoPG;
    }

    /**
     * Gets random component by process group and by user
     * N.B The part name is unique
     *
     * @param processGroup - the process group
     * @param currentUser  - UserCredentials
     * @return component builder object
     */
    public ComponentInfoBuilder getComponentByProcessGroup(ProcessGroupEnum processGroup, UserCredentials currentUser) {

        List<ComponentInfoBuilder> componentPG = COMPONENT_REQUEST.getComponents()
            .stream()
            .filter(component -> component.getProcessGroup().equals(processGroup)).collect(Collectors.toList());
        Collections.shuffle(componentPG);

        ComponentInfoBuilder componentInfoPG = componentPG.stream().findFirst().get();

        componentInfoPG.setResourceFile(FileResourceUtil.getS3FileAndSaveWithUniqueName(componentInfoPG.getComponentName() + componentInfoPG.getExtension(),
            componentInfoPG.getProcessGroup()));
        componentInfoPG.setComponentName(componentInfoPG.getResourceFile().getName().split("\\.", 2)[0]);
        componentInfoPG.setScenarioName(new GenerateStringUtil().generateScenarioName());
        componentInfoPG.setUser(currentUser);

        return componentInfoPG;
    }
}
