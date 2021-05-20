package com.apriori.vds.tests.util;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.digital.factories.DigitalFactoriesItems;
import com.apriori.vds.entity.response.digital.factories.DigitalFactory;

import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterialsItems;
import org.apache.http.HttpStatus;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

public class VDSTestUtil extends TestUtil {
    protected final DigitalFactory digitalFactory = this.getDigitalFactoriesResponse();
    protected final String digitalFactoryIdentity = digitalFactory.getIdentity();
    protected final String processGroupIdentity = digitalFactory.getProcessGroupAssociations().getBarTubeFab().getProcessGroupIdentity();


    protected ProcessGroupMaterial getProcessGroupMaterial() {
        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_PROCESS_GROUP_MATERIALS_BY_DF_AND_PG_IDs, ProcessGroupMaterialsItems.class)
                .inlineVariables(Arrays.asList(digitalFactoryIdentity, processGroupIdentity));

        final ResponseWrapper<ProcessGroupMaterialsItems> processGroupMaterialsItems = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            processGroupMaterialsItems.getStatusCode()
        );

        List<ProcessGroupMaterial> processGroupMaterials = processGroupMaterialsItems.getResponseEntity().getItems();

        Assert.assertNotEquals("To get Material, response should contain it.", 0, processGroupMaterials.size());

        return processGroupMaterials.get(0);
    }

    protected DigitalFactory getDigitalFactoriesResponse() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_DIGITAL_FACTORIES, DigitalFactoriesItems.class);

        ResponseWrapper<DigitalFactoriesItems> digitalFactoriesItemsResponse = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            digitalFactoriesItemsResponse.getStatusCode()
        );

        return digitalFactoriesItemsResponse.getResponseEntity().getItems().get(0);
    }

}
