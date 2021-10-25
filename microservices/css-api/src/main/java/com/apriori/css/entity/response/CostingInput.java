package com.apriori.css.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostingInput {
    private String identity;
    private Integer annualVolume;
    private Integer batchSize;
    private CustomAttributes customAttributes;
    private String machiningMode;
    private String materialMode;
    private String materialName;
    private String processGroupName;
    private Boolean usePrimaryDigitalFactoryAsDefaultForSecondaryDigitalFactories;
    private String vpeName;
    private List<Object> scenarioCustomAttributes;
    private List<Object> scenarioDesignIssues;
    private ProcessSetupOptions processSetupOptions;
    private Integer productionLife;
    private SecondaryProcesses secondaryProcesses;
    private SecondaryDigitalFactories secondaryDigitalFactories;
    private Threads threads;
    private Tolerances tolerances;
    private Thumbnail thumbnail;

    public static class CustomAttributes {
        @JsonProperty("UserList")
        private String userList;

        @JsonProperty("UDARegion")
        private String udaRegion;

        @JsonProperty("BoxMaterial")
        private String boxMaterial;

        @JsonProperty("ShippingCompany")
        private String shippingCompany;

        @JsonProperty("UserListMulti")
        private String[] userListMulti;

        @JsonProperty("StringPredefDefault_1")
        private String[] stringPredefDefault1;
    }

    public static class ProcessSetupOptions {
    }

    public static class SecondaryProcesses {
    }

    public static class SecondaryDigitalFactories {
    }

    public static class Threads {
    }

    public static class Tolerances {
    }
}

