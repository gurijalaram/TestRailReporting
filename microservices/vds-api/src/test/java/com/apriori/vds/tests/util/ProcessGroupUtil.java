package com.apriori.vds.tests.util;

import com.apriori.bcs.entity.response.ProcessGroup;
import com.apriori.bcs.entity.response.ProcessGroups;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.access.control.AccessControlGroup;
import com.apriori.vds.entity.response.process.group.associations.ProcessGroupAssociation;
import com.apriori.vds.entity.response.process.group.associations.ProcessGroupAssociationsItems;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterialsItems;

import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;

import java.util.List;

public class ProcessGroupUtil extends VDSTestUtil {

    private static String groupIdentity;
    private static String materialIdentity;
    private static String associatedProcessGroupIdentity;
    private static String processGroupIdentity;

    protected static List<ProcessGroupMaterial> getProcessGroupMaterial() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.GET_PROCESS_GROUP_MATERIALS_BY_DF_AND_PG_IDs, ProcessGroupMaterialsItems.class)
                .inlineVariables(getDigitalFactoryIdentity(), getAssociatedProcessGroupIdentity())
                .expectedResponseCode(HttpStatus.SC_OK);

        final ResponseWrapper<ProcessGroupMaterialsItems> processGroupMaterialsItems = HTTPRequest.build(requestEntity).get();

        return processGroupMaterialsItems.getResponseEntity().getItems();
    }

    protected static List<ProcessGroup> getProcessGroupsResponse() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_PROCESS_GROUPS, ProcessGroups.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ProcessGroups> processGroupsResponse = HTTPRequest.build(requestEntity).get();

        return processGroupsResponse.getResponseEntity().getItems();
    }

    public static String getAssociatedProcessGroupIdentity() {
        if (associatedProcessGroupIdentity == null) {
            associatedProcessGroupIdentity = getPGAssociationIdByPGName(ProcessGroupEnum.STOCK_MACHINING);
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

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(processGroupAssociations.size()).isNotZero();
        softAssertions.assertAll();

        return processGroupAssociations.get(0);
    }

    protected static List<ProcessGroupAssociation> getProcessGroupAssociations() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.GET_PG_ASSOCIATIONS, ProcessGroupAssociationsItems.class)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ProcessGroupAssociationsItems> responseWrapper = HTTPRequest.build(requestEntity).get();

        return responseWrapper.getResponseEntity()
            .getItems();
    }

    private static AccessControlGroup getSingleGroup() {
        List<AccessControlGroup> accessControlGroups = getAccessControlGroupsResponse();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(accessControlGroups.size()).isNotZero();
        softAssertions.assertAll();

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
