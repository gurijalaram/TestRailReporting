package com.apriori.acs.api.tests;

import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DESIGNER;

import com.apriori.acs.api.enums.acs.AcsApiEnum;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.api.models.response.acs.materialmetadata.MaterialMetadataResponse;
import com.apriori.acs.api.utils.acs.AcsResources;
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
public class MaterialMetadataTests extends TestUtil {
    private final UserCredentials userCredentials = UserUtil.getUser(APRIORI_DESIGNER);
    private final SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(id = {31138})
    @Description("Test Get Material Metadata endpoint")
    public void testGetMaterialMetadata() {
        AcsResources acsResources = new AcsResources(userCredentials);
        MaterialMetadataResponse materialMetadataResponse = acsResources
            .getMaterialMetadata(
                "aPriori Canada",
                ProcessGroupEnum.STOCK_MACHINING.getProcessGroup(),
                MaterialMetadataResponse.class).getResponseEntity();

        softAssertions.assertThat(materialMetadataResponse.size()).isGreaterThan(0);
        softAssertions.assertThat(materialMetadataResponse.get(1).getName()).isEqualTo("rectangularBarCostPerKG");
        softAssertions.assertThat(materialMetadataResponse.get(1).getType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {31139})
    @Description("Test Get Material Metadata endpoint with Revision")
    public void testGetMaterialMetadataWithRevision() {
        AcsResources acsResources = new AcsResources(userCredentials);
        MaterialMetadataResponse materialMetadataResponse = acsResources
            .getMaterialMetadataWithRevision(
                "aPriori USA", "aP2023R1_SP00_F00_(2022-07)",
                ProcessGroupEnum.STOCK_MACHINING.getProcessGroup(),
                MaterialMetadataResponse.class).getResponseEntity();

        softAssertions.assertThat(materialMetadataResponse.size()).isGreaterThan(0);
        softAssertions.assertThat(materialMetadataResponse.get(2).getName()).isEqualTo("rectangularBarCostPerUnit");
        softAssertions.assertThat(materialMetadataResponse.get(2).getType()).isEqualTo("DOUBLE");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {31140})
    @Description("Get Material Metadata endpoint with Invalid Digital Factory")
    public void testGetMaterialMetadataWithInvalidDF() {
        AcsResources acsResources = new AcsResources(userCredentials);
        GenericErrorResponse genericErrorResponse = acsResources.getEndpointInvalidDigitalFactory(AcsApiEnum.MATERIAL_METADATA);

        assertOnInvalidResponse(genericErrorResponse);
    }

    @Test
    @TestRail(id = {31141})
    @Description("Get Material Metadata endpoint with Invalid Process Group")
    public void testGetMaterialMetadataWithInvalidPG() {
        AcsResources acsResources = new AcsResources(userCredentials);
        GenericErrorResponse genericErrorResponse = acsResources.getEndpointInvalidProcessGroup(AcsApiEnum.MATERIAL_METADATA);

        assertOnInvalidResponse(genericErrorResponse);
    }

    private void assertOnInvalidResponse(GenericErrorResponse genericErrorResponse) {
        softAssertions.assertThat((genericErrorResponse.getErrorCode()).equals(HttpStatus.SC_NOT_FOUND));
        softAssertions.assertAll();
    }
}
