package com.apriori.apibase.services.bcs.objects;

import java.util.List;

public class ProcessGroup {
    private String name;
    private String defaultVpeName;
    private String defaultMaterialName;
    private Boolean supportsMaterials;
    private Boolean supportsCompositesMapping;
    private List<String> vpeNames;
    private String vpeName;
    private String processGroupName;

    public String getVpeName() {
        return vpeName;
    }

    public ProcessGroup setVpeName(String vpeName) {
        this.vpeName = vpeName;
        return this;
    }

    public String getProcessGroupName() {
        return processGroupName;
    }

    public ProcessGroup setProcessGroupName(String processGroupName) {
        this.processGroupName = processGroupName;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultVpeName() {
        return defaultVpeName;
    }

    public void setDefaultVpeName(String defaultVpeName) {
        this.defaultVpeName = defaultVpeName;
    }

    public String getDefaultMaterialName() {
        return defaultMaterialName;
    }

    public void setDefaultMaterialName(String defaultMaterialName) {
        this.defaultMaterialName = defaultMaterialName;
    }

    public Boolean getSupportsMaterials() {
        return supportsMaterials;
    }

    public void setSupportsMaterials(Boolean supportsMaterials) {
        this.supportsMaterials = supportsMaterials;
    }

    public Boolean getSupportsCompositesMapping() {
        return supportsCompositesMapping;
    }

    public void setSupportsCompositesMapping(Boolean supportsCompositesMapping) {
        this.supportsCompositesMapping = supportsCompositesMapping;
    }

    public List<String> getVpeNames() {
        return vpeNames;
    }

    public void setVpeNames(List<String> vpeNames) {
        this.vpeNames = vpeNames;
    }
}
