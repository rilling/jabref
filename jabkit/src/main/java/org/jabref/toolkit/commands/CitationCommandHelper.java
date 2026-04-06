package org.jabref.toolkit.commands;

import java.util.List;

import org.jabref.logic.ai.AiService;
import org.jabref.logic.importer.FetcherException;
import org.jabref.logic.importer.fetcher.citation.CitationFetcher;
import org.jabref.logic.importer.fetcher.citation.CitationFetcherType;
import org.jabref.logic.l10n.Localization;
import org.jabref.logic.preferences.CliPreferences;
import org.jabref.logic.util.CurrentThreadTaskExecutor;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;

import org.slf4j.Logger;

final class CitationCommandHelper {

    private CitationCommandHelper() {
    }

    static int fetchAndOutputEntries(JabKit argumentProcessor,
                                     CitationFetcherType citationFetcherType,
                                     String doi,
                                     Logger logger,
                                     boolean fetchCitations) {
        CliPreferences preferences = argumentProcessor.cliPreferences;

        AiService aiService = new AiService(
                preferences.getAiPreferences(),
                preferences.getFilePreferences(),
                preferences.getCitationKeyPatternPreferences(),
                logger::info,
                new CurrentThreadTaskExecutor());

        CitationFetcher citationFetcher = CitationFetcherType.getCitationFetcher(
                citationFetcherType,
                preferences.getImporterPreferences(),
                preferences.getImportFormatPreferences(),
                preferences.getCitationKeyPatternPreferences(),
                preferences.getGrobidPreferences(),
                aiService
        );

        List<BibEntry> entries;

        try {
            BibEntry entry = new BibEntry().withField(StandardField.DOI, doi);
            entries = fetchCitations
                      ? citationFetcher.getCitations(entry)
                      : citationFetcher.getReferences(entry);
        } catch (FetcherException e) {
            logger.error("Could not fetch citation information based on DOI", e);
            System.err.print(Localization.lang("No data was found for the identifier"));
            System.err.println(" - " + doi);
            System.err.println(e.getLocalizedMessage());
            System.err.println();
            return 2;
        }

        return JabKit.outputEntries(argumentProcessor.cliPreferences, entries);
    }
}
