package org.jabref.logic.cleanup;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jabref.logic.preferences.TimestampPreferences;
import org.jabref.model.FieldChange;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.event.EntriesEventSource;
import org.jabref.model.entry.field.Field;
import org.jabref.model.entry.field.StandardField;

/// This class handles the migration from timestamp field to creationdate and modificationdate fields.
///
/// If the old updateTimestamp setting is enabled, the timestamp field for each entry are migrated to the date-modified field.
/// Otherwise it is migrated to the date-added field.
public class TimeStampToModificationDate implements CleanupJob {

    private final Field timeStampField;

    public TimeStampToModificationDate(TimestampPreferences timestampPreferences) {
        timeStampField = timestampPreferences.getTimestampField();
    }

    @Override
    public List<FieldChange> cleanup(BibEntry entry) {
        // Query entries for their timestamp field entries
        if (entry.getField(timeStampField).isPresent()) {
            Optional<String> formattedTimeStamp = TimeStampMigrationUtil.formatTimeStamp(entry.getField(timeStampField).get());
            if (formattedTimeStamp.isEmpty()) {
                // In case the timestamp could not be parsed, do nothing to not lose data
                return List.of();
            }
            // Setting the EventSource is necessary to circumvent the update of the modification date during timestamp migration
            entry.clearField(timeStampField, EntriesEventSource.CLEANUP_TIMESTAMP);
            List<FieldChange> changeList = new ArrayList<>();
            FieldChange changeTo;
            // Add removal of timestamp field
            changeList.add(new FieldChange(entry, StandardField.TIMESTAMP, formattedTimeStamp.get(), ""));
            entry.setField(StandardField.MODIFICATIONDATE, formattedTimeStamp.get(), EntriesEventSource.CLEANUP_TIMESTAMP);
            changeTo = new FieldChange(entry, StandardField.MODIFICATIONDATE, entry.getField(StandardField.MODIFICATIONDATE).orElse(""), formattedTimeStamp.get());
            changeList.add(changeTo);
            return changeList;
        }
        return List.of();
    }
}
