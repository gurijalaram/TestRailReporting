package utils;

import com.apriori.utils.PageUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class UIUtils {
    private PageUtils pageUtils;

    /**
     * Generate a serial date string and concatenate to the supplied string.
     * This assures the returned string is unique.
     *
     * @param str The base string
     * @return string with concatenated serial date string
     */
    public static String saltString(String str) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String strDate = dateFormat.format(date);
        return str.concat("_" + strDate);
    }

    /**
     * Generate a random string
     *
     * @param max the maximum number of characters
     * @return random string
     */
    public static String generateString(int max) {
        Random random = new Random();
        return random.ints(97, 123)
                .limit(max)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * Generate a random string with special characters
     *
     * @param max the maximum number of characters
     * @param specialCharacters an array of special characters to concatenate to the string
     * @return random string
     */
    public static String generateString(int max, char[] specialCharacters) {
        String str = generateString(max);
        for (char c : specialCharacters) {
            str.concat(Character.toString(c));
        }

        return str;
    }
}
