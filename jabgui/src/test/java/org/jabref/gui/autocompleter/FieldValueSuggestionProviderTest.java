package org.jabref.gui.autocompleter;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;

import org.jabref.logic.journals.JournalAbbreviationRepository;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;

import org.junit.jupiter.api.Test;

import static org.jabref.gui.autocompleter.AutoCompleterUtil.getRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FieldValueSuggestionProviderTest extends AbstractAutoCompleterTest {

    @Override
    protected SuggestionProvider<String> createAutoCompleter() {
        return new FieldValueSuggestionProvider(StandardField.TITLE, database);
    }

    @Test
    void completeOnIgnoredFieldReturnsNothing() {
        AutoCompletePreferences autoCompletePreferences = mock(AutoCompletePreferences.class);
        JournalAbbreviationRepository journalAbbreviationRepository = mock(JournalAbbreviationRepository.class);
        when(autoCompletePreferences.getCompleteFields()).thenReturn(FXCollections.observableSet(Set.of(StandardField.AUTHOR)));
        SuggestionProviders suggestionProviders = new SuggestionProviders(database, journalAbbreviationRepository, autoCompletePreferences);

        SuggestionProvider<String> autoCompleter = (SuggestionProvider<String>) suggestionProviders.getForField(StandardField.TITLE);

        BibEntry entry = new BibEntry();
        entry.setField(StandardField.TITLE, "testValue");
        database.insertEntry(entry);

        Collection<String> result = autoCompleter.provideSuggestions(getRequest("testValue"));
        assertEquals(List.of(), result);
    }

    @Test
    void completeBeginnigOfValueReturnsValue() {
        BibEntry entry = new BibEntry();
        entry.setField(StandardField.TITLE, "testValue");
        database.insertEntry(entry);

        Collection<String> result = autoCompleter.provideSuggestions(getRequest("test"));
        assertEquals(List.of("testValue"), result);
    }

    @Test
    void completeShortStringReturnsFieldValue() {
        BibEntry entry = new BibEntry();
        entry.setField(StandardField.TITLE, "val");
        database.insertEntry(entry);

        Collection<String> result = autoCompleter.provideSuggestions(getRequest("va"));
        assertEquals(List.of("val"), result);
    }

    @Test
    void completeBeginnigOfSecondWordReturnsWholeFieldValue() {
        BibEntry entry = new BibEntry();
        entry.setField(StandardField.TITLE, "test value");
        database.insertEntry(entry);

        Collection<String> result = autoCompleter.provideSuggestions(getRequest("val"));
        assertEquals(List.of("test value"), result);
    }

    @Test
    void completePartOfWordReturnsWholeFieldValue() {
        BibEntry entry = new BibEntry();
        entry.setField(StandardField.TITLE, "test value");
        database.insertEntry(entry);

        Collection<String> result = autoCompleter.provideSuggestions(getRequest("lue"));
        assertEquals(List.of("test value"), result);
    }

    @Test
    void completeReturnsWholeFieldValue() {
        BibEntry entry = new BibEntry();
        entry.setField(StandardField.TITLE, "test value");
        database.insertEntry(entry);

        Collection<String> result = autoCompleter.provideSuggestions(getRequest("te"));
        assertEquals(List.of("test value"), result);
    }
}
