package com.apriori.vds.api.tests.util;

import static org.junit.jupiter.api.Assertions.fail;

import com.apriori.bcs.api.models.response.ProcessGroup;
import com.apriori.bcs.api.models.response.ProcessGroups;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.vds.api.enums.VDSAPIEnum;
import com.apriori.vds.api.models.response.access.control.AccessControlGroup;
import com.apriori.vds.api.models.response.process.group.associations.ProcessGroupAssociation;
import com.apriori.vds.api.models.response.process.group.associations.ProcessGroupAssociationsItems;
import com.apriori.vds.api.models.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.api.models.response.process.group.materials.ProcessGroupMaterialsItems;
import com.apriori.vds.api.models.response.process.group.materials.stock.ProcessGroupMaterialStock;
import com.apriori.vds.api.models.response.process.group.materials.stock.ProcessGroupMaterialsStocksItems;

import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;

import java.util.List;

public class ProcessGroupUtil {
    private RequestEntityUtil requestEntityUtil;
    private static String groupIdentity;
    private static String materialIdentity;
    private static String associatedProcessGroupIdentity;
    private static String processGroupIdentity;

    public ProcessGroupUtil(RequestEntityUtil requestEntityUtil) {
        this.requestEntityUtil = requestEntityUtil;
    }

    public List<ProcessGroupMaterial> getProcessGroupMaterial() {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PROCESS_GROUP_MATERIALS_BY_DF_AND_PG_ID, ProcessGroupMaterialsItems.class)
                .inlineVariables(new VDSTestUtil(requestEntityUtil).getDigitalFactories().getIdentity(), getAssociatedProcessGroupIdentity())
                .expectedResponseCode(HttpStatus.SC_OK);

        final ResponseWrapper<ProcessGroupMaterialsItems> processGroupMaterialsItems = HTTPRequest.build(requestEntity).get();

        return processGroupMaterialsItems.getResponseEntity().getItems();
    }

    public List<ProcessGroupMaterialStock> getProcessGroupMaterialStocks() {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_ID, ProcessGroupMaterialsStocksItems.class)
                .inlineVariables(new VDSTestUtil(requestEntityUtil).getDigitalFactories().getIdentity(), getAssociatedProcessGroupIdentity(), getMaterialIdentity())
                .expectedResponseCode(HttpStatus.SC_OK);

        final ResponseWrapper<ProcessGroupMaterialsStocksItems> materialStocksItems = HTTPRequest.build(requestEntity).get();

        return materialStocksItems.getResponseEntity().getItems();
    }

    public List<ProcessGroupMaterialStock> getMaterialsStocksWithItems() {
        for (ProcessGroupMaterial material : getProcessGroupMaterial()) {
            RequestEntity requestEntity =
                requestEntityUtil.init(VDSAPIEnum.PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_ID, ProcessGroupMaterialsStocksItems.class)
                    .inlineVariables(new VDSTestUtil(requestEntityUtil).getDigitalFactories().getIdentity(), getAssociatedProcessGroupIdentity(), material.getIdentity())
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

    public ResponseWrapper<ProcessGroupMaterialStock> getMaterialStockById(List<ProcessGroupMaterialStock> processGroupMaterialStocks) {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_STOCK_ID, ProcessGroupMaterialStock.class)
                .inlineVariables(
                    new VDSTestUtil(requestEntityUtil).getDigitalFactories().getIdentity(),
                    getAssociatedProcessGroupIdentity(),
                    getMaterialIdentity(),
                    processGroupMaterialStocks.get(0).getIdentity()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
    }

    public List<ProcessGroup> getProcessGroupsResponse() {
        RequestEntity requestEntity = requestEntityUtil.init(VDSAPIEnum.PROCESS_GROUPS, ProcessGroups.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ProcessGroups> processGroupsResponse = HTTPRequest.build(requestEntity).get();

        return processGroupsResponse.getResponseEntity().getItems();
    }

    public String getAssociatedProcessGroupIdentity() {
        if (associatedProcessGroupIdentity == null) {
            associatedProcessGroupIdentity = getPGAssociationIdByPGName(ProcessGroupEnum.STOCK_MACHINING);
        }
        return associatedProcessGroupIdentity;
    }

    private String getPGAssociationIdByPGName(ProcessGroupEnum processGroup) {
        return  getProcessGroupAssociations().stream()
            .filter(pgAssociation ->
                pgAssociation.getProcessGroupName().equals(processGroup.getProcessGroup()))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Missed Process Group Association for " + processGroup))
            .getProcessGroupIdentity();
    }

    public String getProcessGroupIdentity() {
        if (processGroupIdentity == null) {
            processGroupIdentity = getProcessGroupsResponse().get(0).getIdentity();
        }
        return processGroupIdentity;
    }

    public ProcessGroupAssociation getFirstGroupAssociation() {
        List<ProcessGroupAssociation> processGroupAssociations =  getProcessGroupAssociations();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(processGroupAssociations.size()).isNotZero();
        softAssertions.assertAll();

        return processGroupAssociations.get(0);
    }

    public List<ProcessGroupAssociation> getProcessGroupAssociations() {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PG_ASSOCIATIONS, ProcessGroupAssociationsItems.class)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ProcessGroupAssociationsItems> responseWrapper = HTTPRequest.build(requestEntity).get();

        return responseWrapper.getResponseEntity()
            .getItems();
    }

    private AccessControlGroup getSingleGroup() {
        List<AccessControlGroup> accessControlGroups = new VDSTestUtil(requestEntityUtil).getAccessControlGroups();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(accessControlGroups.size()).isNotZero();
        softAssertions.assertAll();

        return accessControlGroups.get(0);
    }

    public String getMaterialIdentity() {
        if (materialIdentity == null) {
            materialIdentity = getProcessGroupMaterial().get(0).getIdentity();
        }
        return materialIdentity;
    }

    public String getGroupIdentity() {
        if (groupIdentity == null) {
            groupIdentity = getSingleGroup().getIdentity();
        }
        return groupIdentity;
    }

}
