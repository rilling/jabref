package org.jabref.logic.cleanup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jabref.model.FieldChange;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.Date;
import org.jabref.model.entry.event.EntriesEventSource;
import org.jabref.model.entry.field.Field;
import org.jabref.model.entry.field.StandardField;

class TimeStampMigrationHelper {

    private TimeStampMigrationHelper() {
    }

    static List<FieldChange> migrate(BibEntry entry, Field timeStampField, Field targetField) {
        Optional<String> timeStamp = entry.getField(timeStampField);
        if (timeStamp.isEmpty()) {
            return List.of();
        }

        Optional<String> formattedTimeStamp = formatTimeStamp(timeStamp.get());
        if (formattedTimeStamp.isEmpty()) {
            return List.of();
        }

        String formattedValue = formattedTimeStamp.get();

        entry.clearField(timeStampField, EntriesEventSource.CLEANUP_TIMESTAMP);

        List<FieldChange> changeList = new ArrayList<>();
        changeList.add(new FieldChange(entry, StandardField.TIMESTAMP, formattedValue, ""));

        Optional<String> oldTargetValue = entry.getField(targetField);
        entry.setField(targetField, formattedValue, EntriesEventSource.CLEANUP_TIMESTAMP);
        changeList.add(new FieldChange(entry, targetField, oldTargetValue.orElse(""), formattedValue));

        return changeList;
    }

    private static Optional<String> formatTimeStamp(String timeStamp) {
        Optional<Date> parsedDate = Date.parse(timeStamp);
        if (parsedDate.isEmpty()) {
            return Optional.empty();
        }

        Date date = parsedDate.get();
        int year = date.getYear().orElse(1);
        int month = getMonth(date);
        int day = date.getDay().orElse(1);
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, 0, 0);
        localDateTime.truncatedTo(ChronoUnit.SECONDS);
        return Optional.of(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    private static int getMonth(Date date) {
        if (date.getMonth().isPresent()) {
            return date.getMonth().get().getNumber();
        }
        return 1;
    }
}
