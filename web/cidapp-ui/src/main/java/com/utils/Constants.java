package com.utils;

import com.apriori.utils.FileResourceUtil;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class Constants {

    public static final List<String> INPUT_VALUES = Arrays.asList("annual volume", "batch size", "material", "production life", "component name", "description", "notes",
        "scenario name", "tolerance count", "fully burdened cost", "material cost", "piece part cost", "total capital investment",
        "cycle time", "finish mass", "process routing", "utilization");
    public static final List<String> TOGGLE_VALUES = Arrays.asList("cad connected", "locked", "published");
    public static final List<String> DATE_VALUES = Arrays.asList("created at", "last updated at");
    public static final List<String> TYPE_INPUT_VALUES = Arrays.asList("process group", "vpe", "assignee", "component type", "cost maturity", "created by", "last updated by",
        "state", "status", "dfm");
}
