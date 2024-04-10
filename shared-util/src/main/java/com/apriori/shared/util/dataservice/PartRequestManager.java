package com.apriori.shared.util.dataservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.json.JsonManager;
import org.openqa.selenium.NoSuchElementException;

public class PartRequestManager {
    private final String jsonFile;

    public PartRequestManager(String jsonFile) {
        this.jsonFile = jsonFile;
    }

    /**
     * Reads a json file
     *
     * @return component object
     */
    private ComponentRequest readFile() {
        return JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(jsonFile), ComponentRequest.class);
    }

    /**
     * Gets a random small assembly
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getAssembly() {
        List<ComponentInfoBuilder> listOfComponents = readFile().getAssemblies().stream().filter(o -> o.getMetadata().getCategory().equalsIgnoreCase("small")).collect(Collectors.toList());
        Collections.shuffle(listOfComponents);

        return listOfComponents.stream().findFirst().orElseThrow(() -> new RuntimeException("No 'small' assembly was found"));
    }

    /**
     * Gets an assembly specified by name
     *
     * @param assembly - the assembly
     * @return component builder object
     */
    public ComponentInfoBuilder getAssembly(String assembly) {
        return readFile().getAssemblies()
            .stream()
            .filter(o -> o.getComponentName().equalsIgnoreCase(assembly))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("The assembly '%s' was not defined in the '%s' file", assembly, jsonFile)));
    }

    /**
     * Gets a random medium assembly
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getMediumAssembly() {
        List<ComponentInfoBuilder> listOfComponents = readFile().getAssemblies().stream().filter(o -> o.getMetadata().getCategory().equalsIgnoreCase("medium")).collect(Collectors.toList());
        Collections.shuffle(listOfComponents);

        return listOfComponents.stream().findFirst().orElseThrow(() -> new RuntimeException("No 'medium' assembly was found"));
    }

    /**
     * Gets a random large assembly
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getLargeAssembly() {
        List<ComponentInfoBuilder> listOfComponents = readFile().getAssemblies().stream().filter(o -> o.getMetadata().getCategory().equalsIgnoreCase("large")).collect(Collectors.toList());
        Collections.shuffle(listOfComponents);

        return listOfComponents.stream().findFirst().orElseThrow(() -> new RuntimeException("No 'large' assembly was found"));
    }

    /**
     * Gets a list of all components from component store
     *
     * @return list of component builder object
     */
    public List<ComponentInfoBuilder> getAllComponents() {
        return new ArrayList<>(new HashSet<>(new ArrayList<>(readFile().getComponents())));
    }

    /**
     * Gets a list of components filtering out Two Model Machining and .skp
     *
     * @return list of component builder object
     */
    public List<ComponentInfoBuilder> getComponents() {
        return getAllComponents().stream()
            .filter(o -> !o.getProcessGroup().equals(ProcessGroupEnum.TWO_MODEL_MACHINING) &&
                !o.getProcessGroup().equals(ProcessGroupEnum.WITHOUT_PG))
            .collect(Collectors.toList());
    }

    /**
     * Gets a list of two model machining components
     *
     * @return list of component builder object
     */
    public List<ComponentInfoBuilder> getTwoModelComponents() {
        return getAllComponents().stream()
            .filter(o -> o.getProcessGroup().equals(ProcessGroupEnum.TWO_MODEL_MACHINING))
            .collect(Collectors.toList());
    }
}
