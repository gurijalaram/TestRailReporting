package com.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;

@AllArgsConstructor
@Data
public class MultiUpload {
    File resourceFile;
    String scenarioName;
}
