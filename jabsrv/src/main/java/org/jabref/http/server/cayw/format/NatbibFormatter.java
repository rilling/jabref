package org.jabref.http.server.cayw.format;

import org.jvnet.hk2.annotations.Service;

@Service
public class NatbibFormatter extends AbstractCitationCommandFormatter {

    public NatbibFormatter(String defaultCommand) {
        super(defaultCommand);
    }
}