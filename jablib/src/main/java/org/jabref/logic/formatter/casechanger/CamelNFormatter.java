package org.jabref.logic.formatter.casechanger;

import java.util.stream.Collectors;

import org.jabref.logic.formatter.Formatter;
import org.jabref.logic.l10n.Localization;

import org.jspecify.annotations.NonNull;

public class CamelNFormatter extends Formatter {
    private final int length;

    public CamelNFormatter(int length) {
        this.length = length;
    }

    @Override
    public String getName() {
        return Localization.lang("Camel case - n letters max");
    }

    @Override
    public String getKey() {
        return "camel_case_n";
    }
    private String formatTitle(String input) {
        Title title = new Title(input);

        return title.getWords().stream()
                    .map(word -> {
                        word.toUpperFirst();
                        return word.toString();
                    })
                    .collect(Collectors.joining(""));
    }
    @Override
    public String format(@NonNull String input) {
        return formatTitle(input).length() > length
               ? formatTitle(input).substring(0, length)
               : formatTitle(input);
    }

    @Override
    public String getDescription() {
        return Localization.lang(
                "Returns capitalized and concatenated title to N length.");
    }

    @Override
    public String getExampleInput() {
        return "this is camel formatter";
    }
}
