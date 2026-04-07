package org.jabref.http.server.cayw.format;

import org.jvnet.hk2.annotations.Service;

@Service
public class BibLatexFormatter extends AbstractCitationCommandFormatter {

    public BibLatexFormatter(String defaultCommand) {
        super(defaultCommand);
    }
}