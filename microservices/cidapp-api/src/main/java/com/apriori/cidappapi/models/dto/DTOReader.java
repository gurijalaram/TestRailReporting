package com.apriori.cidappapi.models.dto;

import com.apriori.builder.ComponentInfoBuilder;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.json.JsonManager;

import org.openqa.selenium.NoSuchElementException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DTOReader {
    private final String jsonFile;

    public DTOReader(String jsonFile) {
        this.jsonFile = jsonFile;
    }

    /**
     * Reads a json file
     *
     * @return component object
     */
    private ComponentDTO readFile() {
        return JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(jsonFile), ComponentDTO.class);
    }

    /**
     * Gets a random small assembly
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getAssembly() {
        ComponentDTO componentDTO = readFile();

        List<ComponentInfoBuilder> listOfComponents = componentDTO.getAssemblies().stream().filter(o -> o.getMetadata().getCategory().equalsIgnoreCase("small")).collect(Collectors.toList());
        Collections.shuffle(listOfComponents);

        return listOfComponents.stream().findFirst().orElseThrow(() -> new RuntimeException("No 'small' assembly was found"));
    }

    /**
     * Gets a random medium assembly
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getMediumAssembly() {
        ComponentDTO componentDTO = readFile();

        List<ComponentInfoBuilder> listOfComponents = componentDTO.getAssemblies().stream().filter(o -> o.getMetadata().getCategory().equalsIgnoreCase("medium")).collect(Collectors.toList());
        Collections.shuffle(listOfComponents);

        return listOfComponents.stream().findFirst().orElseThrow(() -> new RuntimeException("No 'medium' assembly was found"));
    }

    /**
     * Gets a random large assembly
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getLargeAssembly() {
        ComponentDTO componentDTO = readFile();

        List<ComponentInfoBuilder> listOfComponents = componentDTO.getAssemblies().stream().filter(o -> o.getMetadata().getCategory().equalsIgnoreCase("large")).collect(Collectors.toList());
        Collections.shuffle(listOfComponents);

        return listOfComponents.stream().findFirst().orElseThrow(() -> new RuntimeException("No 'large' assembly was found"));
    }

    /**
     * Gets an assembly specified by name
     *
     * @param assembly - the assembly
     * @return component builder object
     */
    public ComponentInfoBuilder getAssembly(String assembly) {
        ComponentDTO componentDTO = readFile();

        return componentDTO.getAssemblies()
            .stream()
            .filter(o -> o.getComponentName().equalsIgnoreCase(assembly))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("The assembly '%s' was not defined in the '%s' file", assembly, jsonFile)));
    }

    /**
     * Gets a list of components
     *
     * @return list of component builder object
     */
    public List<ComponentInfoBuilder> getComponents() {
        return readFile().getComponents();
    }
}
