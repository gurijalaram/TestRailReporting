package com.apriori.vds.tests.util;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.bcs.entity.response.ProcessGroup;
import com.apriori.bcs.entity.response.ProcessGroups;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.digital.factories.DigitalFactoriesItems;
import com.apriori.vds.entity.response.digital.factories.DigitalFactory;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterialsItems;
import com.apriori.vds.entity.response.process.group.site.variable.SiteVariable;
import org.apache.http.HttpStatus;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

public abstract class VDSTestUtil extends TestUtil {
    private static DigitalFactory digitalFactory;
    private static String digitalFactoryIdentity;
    private static String associatedProcessGroupIdentity;
    private static String processGroupIdentity;
    private static String materialIdentity;

    protected static DigitalFactory getDigitalFactoriesResponse() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_DIGITAL_FACTORIES, DigitalFactoriesItems.class);
        ResponseWrapper<DigitalFactoriesItems> digitalFactoriesItemsResponseWrapper = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            digitalFactoriesItemsResponseWrapper.getStatusCode()
        );

        List<DigitalFactory> digitalFactories = digitalFactoriesItemsResponseWrapper.getResponseEntity().getItems();
        Assert.assertNotEquals("To get Digital Factory, response should contain it.", 0, digitalFactories.size());

        return findDigitalFactoryByLocation(digitalFactories, "Germany");
    }

    protected static ProcessGroupMaterial getProcessGroupMaterial() {
        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_PROCESS_GROUP_MATERIALS_BY_DF_AND_PG_IDs, ProcessGroupMaterialsItems.class)
                .inlineVariables(Arrays.asList(getDigitalFactoryIdentity(), getAssociatedProcessGroupIdentity()));

        final ResponseWrapper<ProcessGroupMaterialsItems> processGroupMaterialsItems = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            processGroupMaterialsItems.getStatusCode()
        );

        List<ProcessGroupMaterial> processGroupMaterials = processGroupMaterialsItems.getResponseEntity().getItems();

        Assert.assertNotEquals("To get Material, response should contain it.", 0, processGroupMaterials.size());

        return findMaterialByAltName1(processGroupMaterials, "Galv. Steel, Hot Worked, AISI 1020");
    }

    protected static List<ProcessGroup> getProcessGroupsResponse() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_PROCESS_GROUPS, ProcessGroups.class);

        ResponseWrapper<ProcessGroups> processGroupsResponse = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, processGroupsResponse.getStatusCode());

        return processGroupsResponse.getResponseEntity().getItems();
    }

    private static DigitalFactory findDigitalFactoryByLocation(List<DigitalFactory> digitalFactories, final String location) {
        return digitalFactories.stream()
            .filter(digitalFactory -> location.equals(digitalFactory.getLocation()))
            .findFirst()
            .orElseThrow(
                () -> new IllegalArgumentException(String.format("Digital Factory with location: %s, was not found.", location))
            );
    }

    private static ProcessGroupMaterial findMaterialByAltName1(List<ProcessGroupMaterial> processGroupMaterials, final String materialAltName1) {
        return processGroupMaterials.stream()
            .filter(material -> materialAltName1.equals(material.getAltName1()))
            .findFirst()
            .orElseThrow(
                () -> new IllegalArgumentException(String.format("Material with altName1: %s, was not found.", materialAltName1))
            );
    }

    public static DigitalFactory getDigitalFactory() {
        if(digitalFactory == null) {
            digitalFactory = getDigitalFactoriesResponse();
        }
        return digitalFactory;
    }

    public static String getDigitalFactoryIdentity() {
        if(digitalFactoryIdentity == null) {
            digitalFactoryIdentity = getDigitalFactory().getIdentity();
        }
        return digitalFactoryIdentity;
    }

    public static String getAssociatedProcessGroupIdentity() {
        if(associatedProcessGroupIdentity == null) {
            associatedProcessGroupIdentity = getDigitalFactory().getProcessGroupAssociations().getSheetMetal().getProcessGroupIdentity();
        }
        return associatedProcessGroupIdentity;
    }


    public static String getProcessGroupIdentity() {
        if(processGroupIdentity == null) {
            processGroupIdentity = getProcessGroupsResponse().get(0).getIdentity();
        }
        return processGroupIdentity;
    }

    public static String getMaterialIdentity() {
        if(materialIdentity == null) {
            materialIdentity = getProcessGroupMaterial().getIdentity();
        }
        return materialIdentity;
    }
}
