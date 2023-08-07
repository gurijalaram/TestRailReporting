package com.apriori.util;

import static org.junit.jupiter.api.Assertions.fail;

import com.apriori.bcs.models.response.ProcessGroup;
import com.apriori.bcs.models.response.ProcessGroups;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.vds.enums.VDSAPIEnum;
import com.apriori.vds.models.response.access.control.AccessControlGroup;
import com.apriori.vds.models.response.process.group.associations.ProcessGroupAssociation;
import com.apriori.vds.models.response.process.group.associations.ProcessGroupAssociationsItems;
import com.apriori.vds.models.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.models.response.process.group.materials.ProcessGroupMaterialsItems;
import com.apriori.vds.models.response.process.group.materials.stock.ProcessGroupMaterialStock;
import com.apriori.vds.models.response.process.group.materials.stock.ProcessGroupMaterialsStocksItems;

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

    protected static List<ProcessGroupMaterialStock> getProcessGroupMaterialStocks() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.GET_PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_IDs, ProcessGroupMaterialsStocksItems.class)
                .inlineVariables(getDigitalFactoryIdentity(), getAssociatedProcessGroupIdentity(), getMaterialIdentity())
                .expectedResponseCode(HttpStatus.SC_OK);

        final ResponseWrapper<ProcessGroupMaterialsStocksItems> materialStocksItems = HTTPRequest.build(requestEntity).get();

        return materialStocksItems.getResponseEntity().getItems();
    }

    protected static List<ProcessGroupMaterialStock> getMaterialsStocksWithItems() {
        for (ProcessGroupMaterial material : getProcessGroupMaterial()) {
            RequestEntity requestEntity =
                RequestEntityUtil.init(VDSAPIEnum.GET_PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_IDs, ProcessGroupMaterialsStocksItems.class)
                    .inlineVariables(getDigitalFactoryIdentity(), getAssociatedProcessGroupIdentity(), material.getIdentity())
                    .expectedResponseCode(HttpStatus.SC_OK);

            ResponseWrapper<ProcessGroupMaterialsStocksItems> processGroupMaterialStocksResponse = HTTPRequest.build(requestEntity).get();

            List<ProcessGroupMaterialStock> processGroupMaterialStocks = processGroupMaterialStocksResponse.getResponseEntity().getItems();

            if (!processGroupMaterialStocks.isEmpty()) {
                return processGroupMaterialStocks;
            }
        }

        fail("Materials don't contain materials stocks");

        return null;
    }

    protected static ResponseWrapper<ProcessGroupMaterialStock> getMaterialStockById(List<ProcessGroupMaterialStock> processGroupMaterialStocks) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.GET_SPECIFIC_PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_IDs, ProcessGroupMaterialStock.class)
                .inlineVariables(
                    getDigitalFactoryIdentity(),
                    getAssociatedProcessGroupIdentity(),
                    getMaterialIdentity(),
                    processGroupMaterialStocks.get(0).getIdentity()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
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
