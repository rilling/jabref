package org.jabref.logic.cleanup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jabref.logic.preferences.TimestampPreferences;
import org.jabref.model.FieldChange;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.Date;
import org.jabref.model.entry.event.EntriesEventSource;
import org.jabref.model.entry.field.Field;
import org.jabref.model.entry.field.StandardField;

/// Shared cleanup logic for migrating the legacy timestamp field to a target date field.
public abstract class AbstractTimeStampCleanup implements CleanupJob {

    private final Field timeStampField;

    protected AbstractTimeStampCleanup(TimestampPreferences timestampPreferences) {
        this.timeStampField = timestampPreferences.getTimestampField();
    }

    /// Returns the target field to which the timestamp should be migrated.
    protected abstract StandardField getTargetField();

    /// Formats the time stamp into the local date and time format.
    /// If the existing timestamp could not be parsed, the day/month/year "1" is used.
    /// For the time portion 00:00:00 is used.
    private Optional<String> formatTimeStamp(String timeStamp) {
        Optional<Date> parsedDate = Date.parse(timeStamp);
        if (parsedDate.isEmpty()) {
            // In case the given timestamp could not be parsed
            return Optional.empty();
        } else {
            Date date = parsedDate.get();
            int year = date.getYear().orElse(1);
            int month = getMonth(date);
            int day = date.getDay().orElse(1);
            LocalDateTime localDateTime = LocalDateTime.of(year, month, day, 0, 0);
            // Remove any time units smaller than seconds
            localDateTime = localDateTime.truncatedTo(ChronoUnit.SECONDS);
            return Optional.of(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
    }

    /// Returns the month value of the passed date if available.
    /// Otherwise returns 1.
    private int getMonth(Date date) {
        if (date.getMonth().isPresent()) {
            return date.getMonth().get().getNumber();
        }
        return 1;
    }

    @Override
    public List<FieldChange> cleanup(BibEntry entry) {
        Optional<String> timestampValue = entry.getField(timeStampField);
        if (timestampValue.isEmpty()) {
            return List.of();
        }

        Optional<String> formattedTimeStamp = formatTimeStamp(timestampValue.get());
        if (formattedTimeStamp.isEmpty()) {
            // In case the timestamp could not be parsed, do nothing to not lose data
            return List.of();
        }

        String newValue = formattedTimeStamp.get();
        StandardField targetField = getTargetField();
        String oldTargetValue = entry.getField(targetField).orElse("");

        // Setting the EventSource is necessary to circumvent the update of the modification date during timestamp migration
        entry.clearField(timeStampField, EntriesEventSource.CLEANUP_TIMESTAMP);

        List<FieldChange> changeList = new ArrayList<>();

        // Add removal of timestamp field
        changeList.add(new FieldChange(entry, StandardField.TIMESTAMP, newValue, ""));

        entry.setField(targetField, newValue, EntriesEventSource.CLEANUP_TIMESTAMP);
        changeList.add(new FieldChange(entry, targetField, oldTargetValue, newValue));

        return changeList;
    }
}