package org.jabref.gui.maintable;

import java.util.Optional;

import org.jabref.gui.DialogService;
import org.jabref.gui.StateManager;
import org.jabref.gui.preferences.GuiPreferences;
import org.jabref.model.entry.BibEntry;

public class SearchShortScienceAction extends AbstractSearchAction {

    public SearchShortScienceAction(DialogService dialogService,
                                    StateManager stateManager,
                                    GuiPreferences preferences) {
        super(dialogService, stateManager, preferences);
    }

    @Override
    protected Optional<String> getSearchUrl(BibEntry entry) {
        return externalLinkCreator.getShortScienceSearchURL(entry);
    }

    @Override
    protected String getServiceName() {
        return "ShortScience";
    }
}
