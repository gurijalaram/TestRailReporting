package com.apriori.gcd.tests;

import com.apriori.gcd.controller.GcdTreeController;
import com.apriori.gcd.entity.response.GcdTree;
import com.apriori.gcd.entity.response.GcdsAdded;
import com.apriori.gcd.entity.response.GcdsRemoved;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GcdTreesTests {
    private GcdTreeController gcdTreeController = new GcdTreeController();
    private SoftAssertions soft = new SoftAssertions();

    @Test
    @TestRail(testCaseId = {"24114"})
    @Description("Validate difference is returned when different trees are submitted in request")
    public void testDifferentGcdTrees() {
        UserCredentials currentUser = UserUtil.getUser();
        String gcdJson = FileResourceUtil.readFileToString("DifferentTrees.json");

        GcdTree gcdTree = gcdTreeController.postGcdTree(gcdJson, currentUser, HttpStatus.SC_OK, GcdTree.class).getResponseEntity();

        List<String> addedNames = Arrays.asList("SharpEdge:5", "SimpleHole:3");
        String removedName = "PlanarFace:4";

        soft.assertThat(gcdTree.getGcdsAdded().stream().map(GcdsAdded::getGcdName).collect(Collectors.toList())).containsAll(addedNames);
        soft.assertThat(gcdTree.getGcdsRemoved().stream().map(GcdsRemoved::getGcdName).collect(Collectors.toList())).contains(removedName);

        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"24115"})
    @Description("Validate that no additions or deletions are returned when same trees are submitted in request")
    public void testSameGcdTrees() {
        validateGCDTrees("SameTrees.json");
    }

    @Test
    @TestRail(testCaseId = {"24116"})
    @Description("Validate that no additions or deletions are returned when same GCDs that have different trees are submitted in request")
    public void testDifferentTreesSameGcds() {
        validateGCDTrees("DifferentTreesSameGcds.json");
    }

    @Test
    @TestRail(testCaseId = {"24117"})
    @Description("Validate input validation with first tree missing in request")
    public void testMissingFirstTree() {
        validateGCDTreeMissing("MissingFirstTree.json");
    }

    @Test
    @TestRail(testCaseId = {"24118"})
    @Description("Validate input validation with second tree missing in request")
    public void testMissingSecondTree() {
        validateGCDTreeMissing("MissingSecondTree.json");
    }

    public void validateGCDTrees(final String fileName) {
        UserCredentials currentUser = UserUtil.getUser();
        String gcdJson = FileResourceUtil.readFileToString(fileName);
        GcdTree gcdTree = gcdTreeController.postGcdTree(gcdJson, currentUser, HttpStatus.SC_OK, GcdTree.class).getResponseEntity();
        soft.assertThat(gcdTree.getGcdsAdded().stream().map(GcdsAdded::getGcdName).collect(Collectors.toList())).isEmpty();
        soft.assertThat(gcdTree.getGcdsRemoved().stream().map(GcdsRemoved::getGcdName).collect(Collectors.toList())).isEmpty();
    }

    public void validateGCDTreeMissing(final String fileName) {
        UserCredentials currentUser = UserUtil.getUser();
        String gcdJson = FileResourceUtil.readFileToString(fileName);
        ErrorMessage gcdTree = gcdTreeController.postGcdTree(gcdJson, currentUser, HttpStatus.SC_BAD_REQUEST, ErrorMessage.class).getResponseEntity();
        assert (gcdTree.getMessage()).contains("should not be null");
    }
}
