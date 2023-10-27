package com.apriori.cidappapi.models.dto;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.json.JsonManager;

import org.openqa.selenium.NoSuchElementException;

import java.util.Collections;
import java.util.List;

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
     * Gets a random assembly
     *
     * @return component builder object
     */
    public ComponentInfoBuilder getAssembly() {
        ComponentDTO componentDTO = readFile();

        List<ComponentInfoBuilder> listOfComponents = componentDTO.getAssemblies();
        Collections.shuffle(listOfComponents);

        return listOfComponents.stream().findFirst().get();
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
