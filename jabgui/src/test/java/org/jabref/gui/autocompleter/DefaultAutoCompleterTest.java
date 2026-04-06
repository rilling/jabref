package org.jabref.gui.autocompleter;

import java.util.Collection;
import java.util.List;

import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;

import org.junit.jupiter.api.Test;

import static org.jabref.gui.autocompleter.AutoCompleterUtil.getRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultAutoCompleterTest extends AbstractAutoCompleterTest {

    @Override
    protected SuggestionProvider<String> createAutoCompleter() {
        return new WordSuggestionProvider(StandardField.TITLE, database);
    }

    @Test
    void completeBeginningOfValueReturnsValue() {
        BibEntry entry = new BibEntry();
        entry.setField(StandardField.TITLE, "testValue");
        database.insertEntry(entry);

        Collection<String> result = autoCompleter.provideSuggestions(getRequest("test"));
        assertEquals(List.of("testValue"), result);
    }

    @Test
    void completeShortStringReturnsValue() {
        BibEntry entry = new BibEntry();
        entry.setField(StandardField.TITLE, "val");
        database.insertEntry(entry);

        Collection<String> result = autoCompleter.provideSuggestions(getRequest("va"));
        assertEquals(List.of("val"), result);
    }

    @Test
    void completeBeginnigOfSecondWordReturnsWord() {
        BibEntry entry = new BibEntry();
        entry.setField(StandardField.TITLE, "test value");
        database.insertEntry(entry);

        Collection<String> result = autoCompleter.provideSuggestions(getRequest("val"));
        assertEquals(List.of("value"), result);
    }

    @Test
    void completePartOfWordReturnsValue() {
        BibEntry entry = new BibEntry();
        entry.setField(StandardField.TITLE, "test value");
        database.insertEntry(entry);

        Collection<String> result = autoCompleter.provideSuggestions(getRequest("lue"));
        assertEquals(List.of("value"), result);
    }
}
