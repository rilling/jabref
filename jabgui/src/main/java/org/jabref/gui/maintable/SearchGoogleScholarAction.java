package org.jabref.gui.maintable;

import java.util.Optional;

import org.jabref.gui.DialogService;
import org.jabref.gui.StateManager;
import org.jabref.gui.preferences.GuiPreferences;
import org.jabref.model.entry.BibEntry;

public class SearchGoogleScholarAction extends AbstractSearchAction {

    public SearchGoogleScholarAction(DialogService dialogService,
                                     StateManager stateManager,
                                     GuiPreferences preferences) {
        super(dialogService, stateManager, preferences);
    }

    @Override
    protected Optional<String> getSearchUrl(BibEntry entry) {
        return externalLinkCreator.getGoogleScholarSearchURL(entry);
    }

    @Override
    protected String getServiceName() {
        return "Google Scholar";
    }
}
