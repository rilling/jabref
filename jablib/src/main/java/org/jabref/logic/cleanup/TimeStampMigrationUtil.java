package org.jabref.logic.cleanup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.jabref.model.entry.Date;

public final class TimeStampMigrationUtil {

    private TimeStampMigrationUtil() {
    }

    /// Formats the time stamp into the local date and time format.
    /// If the existing timestamp could not be parsed, an empty Optional is returned.
    /// For missing date parts, the value 1 is used.
    /// For the time portion 00:00:00 is used.
    public static Optional<String> formatTimeStamp(String timeStamp) {
        Optional<Date> parsedDate = Date.parse(timeStamp);
        if (parsedDate.isEmpty()) {
            return Optional.empty();
        }

        Date date = parsedDate.get();
        int year = date.getYear().orElse(1);
        int month = getMonth(date);
        int day = date.getDay().orElse(1);

        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, 0, 0)
                                                   .truncatedTo(ChronoUnit.SECONDS);

        return Optional.of(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    private static int getMonth(Date date) {
        return date.getMonth()
                   .map(month -> month.getNumber())
                   .orElse(1);
    }
}
