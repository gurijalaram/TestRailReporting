package com.apriori.utils;

import java.time.format.DateTimeFormatter;

public class DateFormattingUtils {

    public static final DateTimeFormatter dtf_yyyyMMdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter dtf_Mddyyyy_slash = DateTimeFormatter.ofPattern("M/dd/yyyy");
    public static final DateTimeFormatter dtf_MMddyyyy_slash = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public static final DateTimeFormatter dtf_MMMdyyyy = DateTimeFormatter.ofPattern("MMM d, yyyy");
    public static final DateTimeFormatter dtf_MMM_d_yyyy = DateTimeFormatter.ofPattern("MMM d yyyy");
    public static final DateTimeFormatter dtf_hhaaMMMd = DateTimeFormatter.ofPattern("hh a, MMM d");
    public static final DateTimeFormatter dtf_Mdyyhhmmaa = DateTimeFormatter.ofPattern("M/d/yy hh:mm a");
    public static final DateTimeFormatter dtf_yyyyMMddTHHmm = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    public static final DateTimeFormatter dtf_EEEddMMMyyyy = DateTimeFormatter.ofPattern("EEE dd.MMM.yyyy");
    public static final DateTimeFormatter dtf_yyyyMMddTHHmmss = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    public static final DateTimeFormatter dtf_yyyyMMddTHHmmssSSS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    public static final DateTimeFormatter dtf_MMddyyyyHHmmAMPM = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
    public static final DateTimeFormatter dtf_MMM = DateTimeFormatter.ofPattern("MMM");
    public static final DateTimeFormatter dtf_yyyyMMddTHHmmssZ = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public static final DateTimeFormatter dtf_yyyyMMddTHHmmZ = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'");
    public static final DateTimeFormatter dtf_yyyyMMddTHHmmssSSSZ = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static final DateTimeFormatter dtf_MMddyyyyHHmmss_slash = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
}
