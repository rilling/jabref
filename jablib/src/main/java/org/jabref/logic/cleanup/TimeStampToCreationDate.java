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

/// This class handles the migration from timestamp field to creationdate and modificationdate fields.
///
/// If the old updateTimestamp setting is enabled, the timestamp field for each entry are migrated to the date-modified field.
/// Otherwise it is migrated to the date-added field.
public class TimeStampToCreationDate extends AbstractTimeStampCleanup{

    public TimeStampToCreationDate(TimestampPreferences timestampPreferences) {
        super(timestampPreferences);
    }

    @Override
    protected StandardField getTargetField() {
        return StandardField.CREATIONDATE;
    }
}
