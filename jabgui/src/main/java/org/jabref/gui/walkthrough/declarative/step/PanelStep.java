package org.jabref.gui.walkthrough.declarative.step;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import org.jabref.gui.walkthrough.declarative.NodeResolver;
import org.jabref.gui.walkthrough.declarative.Trigger;
import org.jabref.gui.walkthrough.declarative.WindowResolver;
import org.jabref.gui.walkthrough.declarative.effect.HighlightEffect;
import org.jabref.gui.walkthrough.declarative.effect.WalkthroughEffect;
import org.jabref.gui.walkthrough.declarative.effect.WindowEffect;
import org.jabref.gui.walkthrough.declarative.richtext.WalkthroughRichTextBlock;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record PanelStep(@NonNull String title,
    @NonNull List<WalkthroughRichTextBlock> content,
    @Nullable NodeResolver resolverValue,
    @Nullable String continueButtonTextValue,
    @Nullable String skipButtonTextValue,
    @Nullable String backButtonTextValue,
    @Nullable Trigger triggerValue,
    @NonNull PanelPosition position,
    @Nullable Double widthValue,
    @Nullable Double heightValue,
    @Nullable WalkthroughEffect highlightValue,
    @Nullable WindowResolver activeWindowResolverValue,
    boolean showQuitButtonValue,
    @NonNull QuitButtonPosition quitButtonPositionValue) implements VisibleComponent {

    @Override
    public Optional<NodeResolver> nodeResolver() {
        return Optional.ofNullable(resolverValue);
    }

    @Override
    public Optional<String> continueButtonText() {
        return Optional.ofNullable(continueButtonTextValue);
    }

    @Override
    public Optional<String> skipButtonText() {
        return Optional.ofNullable(skipButtonTextValue);
    }

    @Override
    public Optional<String> backButtonText() {
        return Optional.ofNullable(backButtonTextValue);
    }

    @Override
    public Optional<Trigger> trigger() {
        return Optional.ofNullable(triggerValue);
    }

    @Override
    public OptionalDouble maxWidth() {
        return widthValue != null ? OptionalDouble.of(widthValue) : OptionalDouble.empty();
    }

    @Override
    public OptionalDouble maxHeight() {
        return heightValue != null ? OptionalDouble.of(heightValue) : OptionalDouble.empty();
    }

    @Override
    public Optional<WalkthroughEffect> highlight() {
        return Optional.ofNullable(highlightValue);
    }

    @Override
    public Optional<WindowResolver> windowResolver() {
        return Optional.ofNullable(activeWindowResolverValue);
    }

    @Override
    public boolean showQuitButton() {
        return showQuitButtonValue;
    }

    @Override
    public QuitButtonPosition quitButtonPosition() {
        return quitButtonPositionValue;
    }

    public static Builder builder(@NonNull String title) {
        return new Builder(title);
    }

    public static class Builder extends BaseStepBuilder<Builder> {

        private final String title;

        private PanelPosition position = PanelPosition.LEFT;

        private Builder(String title) {
            super(title);
        }

        public Builder position(@NonNull PanelPosition position) {
            this.position = position;
            return this;
        }

        public PanelStep build() {
            if (height != null && (position == PanelPosition.LEFT || position == PanelPosition.RIGHT)) {
                throw new IllegalArgumentException("Height is not applicable for left/right positioned panels.");
            }
            if (width != null && (position == PanelPosition.TOP || position == PanelPosition.BOTTOM)) {
                throw new IllegalArgumentException("Width is not applicable for top/bottom positioned panels.");
            }
            return new PanelStep(title,
                                 content,
                                 resolver,
                                 continueButtonText,
                                 skipButtonText,
                                 backButtonText,
                                 trigger,
                                 position,
                                 width,
                                 height,
                                 highlight,
                                 activeWindowResolver,
                                 showQuitButton,
                                 quitButtonPosition);
        }
    }
}
