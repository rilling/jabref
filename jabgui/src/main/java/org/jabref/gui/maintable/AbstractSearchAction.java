package org.jabref.gui.maintable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.beans.binding.BooleanExpression;

import org.jabref.gui.DialogService;
import org.jabref.gui.StateManager;
import org.jabref.gui.actions.SimpleCommand;
import org.jabref.gui.desktop.os.NativeDesktop;
import org.jabref.gui.preferences.GuiPreferences;
import org.jabref.logic.l10n.Localization;
import org.jabref.logic.util.ExternalLinkCreator;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.jabref.gui.actions.ActionHelper.isFieldSetForSelectedEntry;
import static org.jabref.gui.actions.ActionHelper.needsEntriesSelected;

public abstract class AbstractSearchAction extends SimpleCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSearchAction.class);

    protected final DialogService dialogService;
    protected final StateManager stateManager;
    protected final GuiPreferences preferences;
    protected final ExternalLinkCreator externalLinkCreator;

    protected AbstractSearchAction(DialogService dialogService,
                                   StateManager stateManager,
                                   GuiPreferences preferences) {
        this.dialogService = dialogService;
        this.stateManager = stateManager;
        this.preferences = preferences;
        this.externalLinkCreator = new ExternalLinkCreator(preferences.getImporterPreferences());

        BooleanExpression fieldIsSet = isFieldSetForSelectedEntry(StandardField.TITLE, stateManager);
        this.executable.bind(needsEntriesSelected(1, stateManager).and(fieldIsSet));
    }

    @Override
    public void execute() {
        stateManager.getActiveDatabase().ifPresent(databaseContext -> {
            final List<BibEntry> bibEntries = stateManager.getSelectedEntries();
            BibEntry entry = bibEntries.getFirst();

            getSearchUrl(entry).ifPresent(url -> {
                try {
                    NativeDesktop.openExternalViewer(databaseContext, preferences, url,
                            StandardField.URL, dialogService, entry);
                } catch (IOException ex) {
                    LOGGER.warn("Could not open {}", getServiceName(), ex);
                    dialogService.notify(
                            Localization.lang("Unable to open %0.", getServiceName())
                                    + " " + ex.getMessage());
                }
            });
        });
    }

    /**
     * Returns the search URL for the given entry on the specific service.
     */
    protected abstract Optional<String> getSearchUrl(BibEntry entry);

    /**
     * Returns the display name of the search service (used in error messages).
     */
    protected abstract String getServiceName();
}
