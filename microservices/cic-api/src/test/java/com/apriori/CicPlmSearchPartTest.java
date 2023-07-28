package com.apriori;

import entity.request.PlmFieldDefinitions;
import entity.response.PlmPartResponse;
import entity.response.PlmSearchPart;
import entity.response.PlmSearchResponse;
import enums.PlmPartsSearch;
import enums.PlmWCType;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CicApiTestUtil;
import utils.PlmApiTestUtil;
import utils.SearchFilter;

public class CicPlmSearchPartTest {
    private static SoftAssertions softAssertions;

    @Before
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

    @After
    public void testCleanup() {
        softAssertions.assertAll();
    }
}
