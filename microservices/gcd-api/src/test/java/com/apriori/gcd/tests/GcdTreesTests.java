package com.apriori.gcd.tests;

import com.apriori.gcd.controller.GcdTreeController;
import com.apriori.gcd.entity.response.GcdTree;
import com.apriori.gcd.entity.response.GcdsAdded;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GcdTreesTests {
    private GcdTreeController gcdTreeController = new GcdTreeController();

    @Test
    @TestRail()
    @Description()
    public void testDifferentGcdTrees() throws IOException {
        UserCredentials currentUser = UserUtil.getUser();
        String gcdJson = FileUtils.readFileToString(FileResourceUtil.getResourceAsFile("DifferentTrees.json"), StandardCharsets.UTF_8);

        GcdTree gcdTree = gcdTreeController.getGcdTree(gcdJson, currentUser).getResponseEntity();

        SoftAssertions soft = new SoftAssertions();

        List<String> addedNames = Arrays.asList("SharpEdge:5", "SimpleHole:3");

        soft.assertThat(gcdTree.getGcdsAdded().stream().map(GcdsAdded::getGcdName).collect(Collectors.toList())).containsAll(addedNames);
        soft.assertAll();
    }
}
