package com.apriori;

import com.apriori.cic.enums.PlmPartsSearch;
import com.apriori.cic.enums.PlmWCType;
import com.apriori.cic.models.request.PlmFieldDefinitions;
import com.apriori.cic.models.response.PlmPartResponse;
import com.apriori.cic.models.response.PlmSearchPart;
import com.apriori.cic.models.response.PlmSearchResponse;
import com.apriori.cic.utils.CicApiTestUtil;
import com.apriori.cic.utils.PlmApiTestUtil;
import com.apriori.cic.utils.SearchFilter;
import com.apriori.rules.TestRulesApi;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesApi.class)
public class CicPlmSearchPartTest {
    private static SoftAssertions softAssertions;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
    }

    @Test
    public void testSearchPlmPartByPartExt() {
        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NAME_ENDS_WITH.getFilterKey(), "prt"))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmSearchPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        softAssertions.assertThat(plmPart.getId()).isNotNull();
    }

    @Test
    public void testSearchPlmPartByPartNumber() {
        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(), "0000001059"))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmSearchPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        softAssertions.assertThat(plmPart.getId()).isNotNull();
    }

    @Test
    public void testSearchPlmParts() {
        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NAME_ENDS_WITH.getFilterKey(), "prt"))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmSearchResponse plmParts = CicApiTestUtil.searchPlmWindChillParts(searchFilter);
        softAssertions.assertThat(plmParts.getItems().size()).isGreaterThan(0);
    }

    @Test
    public void testPlmGetPart() {
        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(), "0000001040"))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmSearchPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);

        PlmFieldDefinitions plmFieldDefinitions = new PlmFieldDefinitions();
        plmFieldDefinitions.setBatchSize(10);

        PlmPartResponse plmPartResponse = new PlmApiTestUtil().updatePartInfoToPlm(plmPart.getId(), plmFieldDefinitions, "Updated batch size");

        softAssertions.assertThat(plmPartResponse).isNotNull();
    }

    @AfterEach
    public void testCleanup() {
        softAssertions.assertAll();
    }
}
