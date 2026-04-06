package org.jabref.gui.walkthrough.declarative.step;

import java.util.List;

import org.jabref.gui.walkthrough.declarative.NodeResolver;
import org.jabref.gui.walkthrough.declarative.Trigger;
import org.jabref.gui.walkthrough.declarative.WindowResolver;
import org.jabref.gui.walkthrough.declarative.effect.HighlightEffect;
import org.jabref.gui.walkthrough.declarative.effect.WalkthroughEffect;
import org.jabref.gui.walkthrough.declarative.effect.WindowEffect;
import org.jabref.gui.walkthrough.declarative.richtext.WalkthroughRichTextBlock;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public abstract class BaseStepBuilder<T extends BaseStepBuilder<T>> {

    protected final String title;
    protected List<WalkthroughRichTextBlock> content = List.of();
    protected @Nullable NodeResolver resolver;
    protected @Nullable String continueButtonText;
    protected @Nullable String skipButtonText;
    protected @Nullable String backButtonText;
    protected @Nullable Trigger trigger;
    protected @Nullable Double width;
    protected @Nullable Double height;
    protected @Nullable WalkthroughEffect highlight;
    protected @Nullable WindowResolver activeWindowResolver;
    protected boolean showQuitButton = true;
    protected QuitButtonPosition quitButtonPosition = QuitButtonPosition.AUTO;

    protected BaseStepBuilder(@NonNull String title) {
        this.title = title;
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    public T content(@NonNull WalkthroughRichTextBlock... blocks) {
        this.content = List.of(blocks);
        return self();
    }

    public T content(@NonNull List<WalkthroughRichTextBlock> content) {
        this.content = content;
        return self();
    }

    public T resolver(@NonNull NodeResolver resolver) {
        this.resolver = resolver;
        return self();
    }

    public T continueButton(@NonNull String text) {
        this.continueButtonText = text;
        return self();
    }

    public T skipButton(@NonNull String text) {
        this.skipButtonText = text;
        return self();
    }

    public T backButton(@NonNull String text) {
        this.backButtonText = text;
        return self();
    }

    public T trigger(@NonNull Trigger trigger) {
        this.trigger = trigger;
        return self();
    }

    public T trigger(Trigger.@NonNull Builder triggerBuilder) {
        this.trigger = triggerBuilder.build();
        return self();
    }

    public T width(double width) {
        this.width = width;
        return self();
    }

    public T height(double height) {
        this.height = height;
        return self();
    }

    public T highlight(@NonNull WalkthroughEffect highlight) {
        this.highlight = highlight;
        return self();
    }

    public T highlight(@NonNull WindowEffect effect) {
        return highlight(new WalkthroughEffect(effect));
    }

    public T highlight(@NonNull HighlightEffect effect) {
        return highlight(new WindowEffect(effect));
    }

    public T activeWindow(@NonNull WindowResolver activeWindowResolver) {
        this.activeWindowResolver = activeWindowResolver;
        return self();
    }

    public T showQuitButton(boolean showQuitButton) {
        this.showQuitButton = showQuitButton;
        return self();
    }

    public T quitButtonPosition(@NonNull QuitButtonPosition quitButtonPosition) {
        this.quitButtonPosition = quitButtonPosition;
        return self();
    }
}