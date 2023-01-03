package com.apriori.cic.tests;

import entity.response.PlmPart;
import entity.response.PlmParts;
import enums.PlmPartsSearch;
import enums.PlmWCType;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CicApiTestUtil;
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
        PlmPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        softAssertions.assertThat(plmPart.getId()).isNotNull();
    }

    @Test
    public void testSearchPlmPartByPartNumber() {
        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(), "0000001059"))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        softAssertions.assertThat(plmPart.getId()).isNotNull();
    }

    @Test
    public void testSearchPlmParts() {
        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NAME_ENDS_WITH.getFilterKey(), "prt"))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmParts plmParts = CicApiTestUtil.searchPlmWindChillParts(searchFilter);
        softAssertions.assertThat(plmParts.getItems().size()).isGreaterThan(0);
    }

    @After
    public void testCleanup() {
        softAssertions.assertAll();
    }
}
