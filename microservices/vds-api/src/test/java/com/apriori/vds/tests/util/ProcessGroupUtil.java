package com.apriori.vds.tests.util;

import com.apriori.bcs.entity.response.ProcessGroup;
import com.apriori.bcs.entity.response.ProcessGroups;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.access.control.AccessControlGroup;
import com.apriori.vds.entity.response.process.group.associations.ProcessGroupAssociation;
import com.apriori.vds.entity.response.process.group.associations.ProcessGroupAssociationsItems;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterialsItems;

import org.apache.http.HttpStatus;
import org.junit.Assert;

import java.util.List;

public class ProcessGroupUtil extends VDSTestUtil {

    private static String groupIdentity;
    private static String materialIdentity;
    private static String associatedProcessGroupIdentity;
    private static String processGroupIdentity;

    protected static List<ProcessGroupMaterial> getProcessGroupMaterial() {
        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_PROCESS_GROUP_MATERIALS_BY_DF_AND_PG_IDs, ProcessGroupMaterialsItems.class)
                .inlineVariables(getDigitalFactoryIdentity(), getAssociatedProcessGroupIdentity());

        final ResponseWrapper<ProcessGroupMaterialsItems> processGroupMaterialsItems = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            processGroupMaterialsItems.getStatusCode()
        );

        return processGroupMaterialsItems.getResponseEntity().getItems();
    }

    protected static List<ProcessGroup> getProcessGroupsResponse() {
        RequestEntity requestEntity = RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_PROCESS_GROUPS, ProcessGroups.class);

        ResponseWrapper<ProcessGroups> processGroupsResponse = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, processGroupsResponse.getStatusCode());

        return processGroupsResponse.getResponseEntity().getItems();
    }

    public static String getAssociatedProcessGroupIdentity() {
        if (associatedProcessGroupIdentity == null) {
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

    private static AccessControlGroup getSingleGroup() {
        List<AccessControlGroup> accessControlGroups = getAccessControlGroupsResponse();
        Assert.assertNotEquals("To get Access Control Group, response should contain it.", 0, accessControlGroups.size());

        return accessControlGroups.get(0);
    }

    public static String getMaterialIdentity() {
        if (materialIdentity == null) {
            materialIdentity = getProcessGroupMaterial().get(0).getIdentity();
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
