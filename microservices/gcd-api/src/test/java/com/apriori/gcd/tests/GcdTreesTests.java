package com.apriori.gcd.tests;

import com.apriori.gcd.controller.GcdTreeController;
import com.apriori.gcd.entity.response.GcdTree;
import com.apriori.gcd.entity.response.GcdsAdded;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GcdTreesTests {
    private GcdTreeController gcdTreeController = new GcdTreeController();
    private SoftAssertions soft = new SoftAssertions();

    @Test
    @TestRail()
    @Description()
    public void testDifferentGcdTrees() {
        UserCredentials currentUser = UserUtil.getUser();

        String gcdJson = FileResourceUtil.readFileToString("DifferentTrees.json");
        GcdTree gcdTree = gcdTreeController.getGcdTree(gcdJson, currentUser).getResponseEntity();

        List<String> addedNames = Arrays.asList("SharpEdge:5", "SimpleHole:3");

        soft.assertThat(gcdTree.getGcdsAdded().stream().map(GcdsAdded::getGcdName).collect(Collectors.toList())).containsAll(addedNames);

        // TODO: 19/05/2023 negative test so please do not put in production
        soft.assertThat(gcdTree.getGcdsAdded().stream().map(GcdsAdded::getGcdName).collect(Collectors.toList())).doesNotContain("Metalbowl:5", "Alan:3");

        soft.assertAll();
    }
}
