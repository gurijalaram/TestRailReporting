package com.apriori.acs.api.tests;

import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DESIGNER;

import com.apriori.acs.api.enums.acs.AcsApiEnum;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.api.models.response.acs.materialstockmetadata.MaterialStockMetadataResponse;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class MaterialStockMetadataTests extends TestUtil {
    private final UserCredentials userCredentials = UserUtil.getUser(APRIORI_DESIGNER);
    private final SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(id = {31243})
    @Description("Test Get Material Stock Metadata")
    public void testGetMaterialStockMetadata() {
        AcsResources acsResources = new AcsResources(userCredentials);
        MaterialStockMetadataResponse materialStockMetadataResponse = acsResources.getMaterialOrStockMetadata(
            AcsApiEnum.MATERIAL_STOCK_METADATA,
            MaterialStockMetadataResponse.class,
            DigitalFactoryEnum.APRIORI_CANADA.getDigitalFactory(),
            ProcessGroupEnum.STOCK_MACHINING.getProcessGroup()
        ).getResponseEntity();

        softAssertions.assertThat(materialStockMetadataResponse.size()).isGreaterThan(0);
        softAssertions.assertThat(materialStockMetadataResponse.get(1).getName()).isEqualTo("costPerUnit");
        softAssertions.assertThat(materialStockMetadataResponse.get(1).getForm()).isEqualTo("HEX_BAR");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {31244})
    @Description("Test Get Material Stock Metadata endpoint with Revision")
    public void testGetMaterialStockMetadataWithRevision() {
        AcsResources acsResources = new AcsResources(userCredentials);
        MaterialStockMetadataResponse materialStockMetadataResponse = acsResources.getMaterialOrStockMetadata(
            AcsApiEnum.MATERIAL_STOCK_METADATA_REVISION,
            MaterialStockMetadataResponse.class,
            DigitalFactoryEnum.APRIORI_USA.getDigitalFactory(),
            "aP2023R1_SP00_F00_(2022-07)",
            ProcessGroupEnum.STOCK_MACHINING.getProcessGroup()
        ).getResponseEntity();

        softAssertions.assertThat(materialStockMetadataResponse.size()).isGreaterThan(0);
        softAssertions.assertThat(materialStockMetadataResponse.get(2).getName()).isEqualTo("length");
        softAssertions.assertThat(materialStockMetadataResponse.get(2).getForm()).isEqualTo("HEX_BAR");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {31245})
    @Description("Get Material Metadata Stock endpoint with Invalid Digital Factory")
    public void testGetMaterialtockMetadataWithInvalidDF() {
        AcsResources acsResources = new AcsResources(userCredentials);
        GenericErrorResponse genericErrorResponse = acsResources.getEndpointInvalidParameter(
            AcsApiEnum.MATERIAL_STOCK_METADATA,
            "aPriori Fake",
            ProcessGroupEnum.FORGING.getProcessGroup()
        );

        assertOnInvalidResponse(genericErrorResponse);
    }

    @Test
    @TestRail(id = {31246})
    @Description("Get Material Metadata Stock endpoint with Invalid Process Group")
    public void testGetMaterialStockMetadataWithInvalidPG() {
        AcsResources acsResources = new AcsResources(userCredentials);
        GenericErrorResponse genericErrorResponse = acsResources.getEndpointInvalidParameter(
            AcsApiEnum.MATERIAL_STOCK_METADATA,
            DigitalFactoryEnum.APRIORI_USA.getDigitalFactory(),
            "Fake Process"
        );

        assertOnInvalidResponse(genericErrorResponse);
    }

    @Test
    @TestRail(id = {31248})
    @Description("Get Material Metadata Stock endpoint with Process Group that does not have Stock")
    public void testGetStockMaterialStockMetadataWithNoStock() {
        AcsResources acsResources = new AcsResources(userCredentials);
        GenericErrorResponse genericErrorResponse = acsResources.getEndpointInvalidParameter(
            AcsApiEnum.MATERIAL_STOCK_METADATA,
            DigitalFactoryEnum.APRIORI_USA.getDigitalFactory(),
            ProcessGroupEnum.ASSEMBLY.getProcessGroup()
        );

        softAssertions.assertThat(genericErrorResponse.getErrorCode()).isEqualTo(HttpStatus.SC_CONFLICT);
        softAssertions.assertAll();
    }

    private void assertOnInvalidResponse(GenericErrorResponse genericErrorResponse) {
        softAssertions.assertThat(genericErrorResponse.getErrorCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
        softAssertions.assertAll();
    }
}
