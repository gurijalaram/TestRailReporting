package com.apriori.bcs.utils;

import java.util.HashMap;
import java.util.Map;

public class Materials {
    Map<String, String> processGroupMaterial = new HashMap<>();
    
    public Materials() {
        initProcessGroupMaterial();
    }
    
    public void initProcessGroupMaterial() {
        processGroupMaterial.put("Additive Manufacturing", "Aluminum AlSi10Mg");
        processGroupMaterial.put("Bar & Tube Fab", "Steel, Hot Worked, AISI 1010");
        processGroupMaterial.put("Casting", "Aluminum, Cast, ANSI AL380.0");
        processGroupMaterial.put("Casting - Die", "Aluminum, Cast, ANSI AL380.0");
        processGroupMaterial.put("Casting - Sand", "Aluminum, Cast, ANSI AL380.0");
        processGroupMaterial.put("Composites", "Generic Ply Material");
        processGroupMaterial.put("Forging", "Steel, Cold Worked, AISI 1010");
        processGroupMaterial.put("Plastic Molding", "ABS");
        processGroupMaterial.put("Powder Metal", "F-0005");
        processGroupMaterial.put("Roto & Blow Molding", "Polyethylene, High Density (HDPE)");
        processGroupMaterial.put("Sheet Metal", "Steel, Cold Worked, AISI 1020");
        processGroupMaterial.put("Sheet Metal - Hydroforming", "Aluminum, Stock, ANSI 2017");
        processGroupMaterial.put("Sheet Metal - Stretch Forming", "Aluminum, Stock, ANSI 2024");
        processGroupMaterial.put("Sheet Metal - Transfer Die", "Steel, Cold Worked, AISI 1020");
        processGroupMaterial.put("Sheet Plastic", "HDPE Extrusion Sheet");
        processGroupMaterial.put("Stock Machining", "Steel, Hot Worked, AISI 1010");
    }

    public String getProcessGroupMaterial(String processGroup) {
        return processGroupMaterial.get(processGroup);
    }
}
