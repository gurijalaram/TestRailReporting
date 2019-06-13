package main.java.json.deserializers;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author kpatel
 */
public class DateTimeKeyDeserializer_yyyyMMddTHHmmssSSSZ extends KeyDeserializer {

    private static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Override
    public Object deserializeKey(String s, DeserializationContext deserializationContext) {
        return LocalDateTime.parse(s, formatter);
    }
}
