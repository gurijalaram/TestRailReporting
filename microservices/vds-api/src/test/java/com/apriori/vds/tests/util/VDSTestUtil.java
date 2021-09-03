package com.apriori.vds.tests.util;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.bcs.entity.response.ProcessGroup;
import com.apriori.bcs.entity.response.ProcessGroups;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.access.control.AccessControlGroup;
import com.apriori.vds.entity.response.access.control.AccessControlGroupItems;
import com.apriori.vds.entity.response.digital.factories.DigitalFactoriesItems;
import com.apriori.vds.entity.response.digital.factories.DigitalFactory;
import com.apriori.vds.entity.response.process.group.associations.ProcessGroupAssociation;
import com.apriori.vds.entity.response.process.group.associations.ProcessGroupAssociationsItems;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterialsItems;

import org.apache.http.HttpStatus;
import org.junit.Assert;

import java.util.List;

public abstract class VDSTestUtil extends TestUtil {
    protected static final String customerId =  PropertiesContext.get("${env}.customer_identity");
    protected static final String userId = PropertiesContext.get("${env}.user_identity");

    private static DigitalFactory digitalFactory;

    private static String groupIdentity;

    private static String digitalFactoryIdentity;
    private static String associatedProcessGroupIdentity;
    private static String processGroupIdentity;
    private static String materialIdentity;

    protected static List<AccessControlGroup> getGroupsResponse() {
        RequestEntity requestEntity = RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_GROUPS, AccessControlGroupItems.class);

        ResponseWrapper<AccessControlGroupItems> accessControlGroupsResponse = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            accessControlGroupsResponse.getStatusCode()
        );

        return accessControlGroupsResponse.getResponseEntity().getItems();
    }

    private static AccessControlGroup getSingleGroup() {
        List<AccessControlGroup> accessControlGroups = getGroupsResponse();
        Assert.assertNotEquals("To get Access Control Group, response should contain it.", 0, accessControlGroups.size());

        return accessControlGroups.get(0);
    }

    protected static DigitalFactory getDigitalFactoriesResponse() {
        RequestEntity requestEntity = RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_DIGITAL_FACTORIES, DigitalFactoriesItems.class);
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
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_PROCESS_GROUP_MATERIALS_BY_DF_AND_PG_IDs, ProcessGroupMaterialsItems.class)
                .inlineVariables(getDigitalFactoryIdentity(), getAssociatedProcessGroupIdentity());

        final ResponseWrapper<ProcessGroupMaterialsItems> processGroupMaterialsItems = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            processGroupMaterialsItems.getStatusCode()
        );

        List<ProcessGroupMaterial> processGroupMaterials = processGroupMaterialsItems.getResponseEntity().getItems();
        Assert.assertNotEquals("To get Material, response should contain it.", 0, processGroupMaterials.size());

        //TODO z: update
        //        findMaterialByAltName1()
//        return processGroupMaterials.get(0);
//        return findMaterialByAltName1(processGroupMaterials, "");
        return findMaterialByAltName1(processGroupMaterials, "Galv. Steel, Hot Worked, AISI 1020");
    }

    protected static List<ProcessGroup> getProcessGroupsResponse() {
        RequestEntity requestEntity = RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_PROCESS_GROUPS, ProcessGroups.class);

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
            //TODO z: update
//            .filter(material -> materialAltName1.equals(material.getAltName1()))
            .filter(material -> "386C8B1184MI".equals(material.getIdentity()))
            .findFirst()
            .orElseThrow(
                () -> new IllegalArgumentException(String.format("Material with altName1: %s, was not found.", materialAltName1))
            );
    }


    protected static ProcessGroupAssociation getFirstGroupAssociation() {
        List<ProcessGroupAssociation> processGroupAssociations =  getProcessGroupAssociations();
        Assert.assertNotEquals("To get process group association it should present.", processGroupAssociations.size(), 0);

        return processGroupAssociations.get(0);
    }

    protected static List<ProcessGroupAssociation> getProcessGroupAssociations() {
        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_PG_ASSOCIATIONS, ProcessGroupAssociationsItems.class);

        ResponseWrapper<ProcessGroupAssociationsItems> responseWrapper = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        return responseWrapper.getResponseEntity()
            .getItems();
    }

    public static DigitalFactory getDigitalFactory() {
        if (digitalFactory == null) {
            digitalFactory = getDigitalFactoriesResponse();
        }
        return digitalFactory;
    }

    public static String getDigitalFactoryIdentity() {
        if (digitalFactoryIdentity == null) {
            digitalFactoryIdentity = getDigitalFactory().getIdentity();
        }
        return digitalFactoryIdentity;
    }

    public static String getAssociatedProcessGroupIdentity() {
        if (associatedProcessGroupIdentity == null) {
            //TODO z: replace with a new functionality regarding Process group associations.
            //associatedProcessGroupIdentity = getDigitalFactory().getProcessGroupAssociations().getSheetMetal().getProcessGroupIdentity();
            associatedProcessGroupIdentity = getPGAssociationIdByPGName(ProcessGroupEnum.CASTING_DIE);
        }
        return associatedProcessGroupIdentity;
    }

    private static String getPGAssociationIdByPGName(ProcessGroupEnum processGroup) {
        return  getProcessGroupAssociations().stream()
            .filter(pgAssociation ->
                pgAssociation.getProcessGroupName().equals(processGroup.getProcessGroup()))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Missed Process Group Association for " + processGroup))
            .getProcessGroupIdentity();
    }


    public static String getProcessGroupIdentity() {
        if (processGroupIdentity == null) {
            processGroupIdentity = getProcessGroupsResponse().get(0).getIdentity();
        }
        return processGroupIdentity;
    }

    public static String getMaterialIdentity() {
        if (materialIdentity == null) {
            materialIdentity = getProcessGroupMaterial().getIdentity();
        }
        return materialIdentity;
    }

    public static String getGroupIdentity() {
        if (groupIdentity == null) {
            groupIdentity = getSingleGroup().getIdentity();
        }
        return groupIdentity;
    }

}
