package pl.anicos.nmp.publish.json;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class ValuesGenerator {
    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public String getCurrentDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
