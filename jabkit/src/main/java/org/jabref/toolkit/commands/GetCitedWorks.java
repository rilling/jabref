package org.jabref.toolkit.commands;

import java.util.concurrent.Callable;

import org.jabref.logic.importer.fetcher.citation.CitationFetcherType;
import org.jabref.toolkit.converter.CitationFetcherTypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "get-cited-works", description = "Outputs a list of works cited (\"bibliography\")")
class GetCitedWorks implements Callable<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetCitedWorks.class);

    @CommandLine.ParentCommand
    private JabKit argumentProcessor;

    @CommandLine.Mixin
    private JabKit.SharedOptions sharedOptions = new JabKit.SharedOptions();

    @CommandLine.Option(
            names = "--provider",
            converter = CitationFetcherTypeConverter.class,
            description = "Metadata provider: ${COMPLETION-CANDIDATES}"
    )
    private CitationFetcherType citationFetcherType = CitationFetcherType.OPEN_CITATIONS;

    @CommandLine.Parameters(description = "DOI to check")
    private String doi;

    @Override
    public Integer call() {
        return CitationCommandHelper.fetchAndOutputEntries(
                argumentProcessor,
                citationFetcherType,
                doi,
                LOGGER,
                false
        );
    }
}
