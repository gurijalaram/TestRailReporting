package com.utils;

import com.apriori.utils.FileResourceUtil;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class Constants {

    public static final String EVALUATE_TOOLTIP_TEXT = "There is no active scenario.\n- Select one in Explore\nOR\n- Use New to create a new one.";
    public static final String COMPARE_TOOLTIP_TEXT = "There is no active comparison.\n- Select one in Explore\nOR\n- Use New to create a new one.";
    public static final String ARROW_DOWN = "arrow_down";
    public static final String PAGE_DOWN = "page_down";
    public static final String HORIZONTAL_SCROLL = "horizontal_scroll";
}
