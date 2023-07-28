package main;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
/**Class used for timezone conversion usage.*/
public class Timezone {
    /**
     * Time conversion to UTC.
     * @param timeAndDate
     * @return utc
     */
    public static String timeAndDateToUTC(String timeAndDate) {
        Timestamp timeNow = Timestamp.valueOf(String.valueOf(timeAndDate));
        LocalDateTime dateTimeLocal = timeNow.toLocalDateTime();
        ZonedDateTime dateTimeZone = dateTimeLocal.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime dateTimeUTC = dateTimeZone.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime localEV = dateTimeUTC.toLocalDateTime();
        String outputUTC = localEV.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return outputUTC;

    }
}
