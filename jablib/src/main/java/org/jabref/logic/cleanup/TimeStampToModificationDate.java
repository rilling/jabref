package org.jabref.logic.cleanup;

import java.util.List;

import org.jabref.logic.preferences.TimestampPreferences;
import org.jabref.model.FieldChange;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.Field;
import org.jabref.model.entry.field.StandardField;

/// This class handles the migration from timestamp field to creationdate and modificationdate fields.
///
/// If the old updateTimestamp setting is enabled, the timestamp field for each entry are migrated to the date-modified field.
/// Otherwise it is migrated to the date-added field.
public class TimeStampToModificationDate implements CleanupJob {

    private final Field timeStampField;

    public TimeStampToModificationDate(TimestampPreferences timestampPreferences) {
        this.timeStampField = timestampPreferences.getTimestampField();
    }

    @Override
    public List<FieldChange> cleanup(BibEntry entry) {
        return TimeStampMigrationHelper.migrate(entry, timeStampField, StandardField.MODIFICATIONDATE);
    }
}
