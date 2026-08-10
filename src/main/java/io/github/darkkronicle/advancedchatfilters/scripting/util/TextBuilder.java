/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatfilters.scripting.util;

import java.net.URI;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.*;
import net.minecraft.resources.Identifier;

/** Utility class to make creating {@link Component} easier for scripts. */
@Environment(EnvType.CLIENT)
public class TextBuilder {

    private MutableComponent text;

    /** Create's a new instance with the only text being an empty string. */
    public TextBuilder() {
        this("");
    }

    /**
     * Create's a new instance with empty style
     *
     * @param content Content of the text
     */
    public TextBuilder(String content) {
        text = Component.literal(content);
    }

    /**
     *
     * @return Component that was created
     */
    public MutableComponent build() {
        return text;
    }

    private void applyStyle(Function<Style, Style> styleSupplier) {
        text = Component.literal(text.getContents().toString()).setStyle(styleSupplier.apply(text.getStyle()));
        for (Component t : text.getSiblings()) {
            for (Component te : t.toFlatList(styleSupplier.apply(t.getStyle()))) {
                text.append(te);
            }
        }
    }

    /**
     * Set's the color based off of an integer. Set's it through *all parts* - Color should be
     * hexadecimal
     *
     * @param color Color to set the style to
     */
    public TextBuilder setColor(int color) {
        applyStyle(style -> style.withColor(color));
        return this;
    }

    /**
     * Set's a style from another one to *all parts*
     *
     * @param style Style to set
     */
    public TextBuilder setStyle(Style style) {
        applyStyle(oldStyle -> style);
        return this;
    }

    /**
     * Enforces bold or unbold on *all parts*
     *
     * @param bold If bold or unbold
     */
    public TextBuilder setBold(boolean bold) {
        applyStyle(style -> style.withBold(bold));
        return this;
    }

    /**
     * Enforces italic or not on *all parts*
     *
     * @param italic If italic or not
     */
    public TextBuilder setItalic(boolean italic) {
        applyStyle(style -> style.withItalic(italic));
        return this;
    }

    /**
     * Set's an insertion string that will trigger on click. Will apply to the textbox.
     *
     * @param insertion The insertion that will get placed
     */
    public TextBuilder setInsertion(String insertion) {
        applyStyle(style -> style.withInsertion(insertion));
        return this;
    }

    /**
     * Enforces underlined or not on *all parts*
     *
     * @param underline If underlined or not
     */
    public TextBuilder setUnderline(boolean underline) {
        applyStyle(style -> style.withUnderlined(underline));
        return this;
    }

    /**
     * Enforces strikethrough or not on *all parts*
     *
     * @param strikethrough If strikethrough or not
     */
    public TextBuilder setStrikethrough(boolean strikethrough) {
        applyStyle(style -> style.withStrikethrough(strikethrough));
        return this;
    }

    /**
     * Set's the font using a namespace.
     *
     * <p>namespace:name
     *
     * @param namespace Namespace of the identifier
     * @param name Name of the identifier
     */
    public TextBuilder setFont(String namespace, String name) {
        // Dunno if this will do anything or how it works
        applyStyle(style -> style.withFont(new FontDescription.Resource(Identifier.fromNamespaceAndPath(namespace, name))));
        return this;
    }

    /**
     * Add's a string to the last piece of text. Will inherit the style of the last one.
     *
     * @param content Content to add
     */
    public TextBuilder concatenate(String content) {
        text.append(Component.literal(content));
        return this;
    }

    /**
     * Set's the ClickEvent of the text to *all parts*
     *
     * <p>Possible actions are: - open_url - run_command - suggest_command - change_page -
     * copy_to_clipboard
     *
     * @param action Action that will be set
     * @param value Content of that action
     */
    public TextBuilder setClickEvent(String action, String value) {
        ClickEvent.Action clickAction = ClickEvent.Action.valueOf(action);
        if (!clickAction.isAllowedFromServer()) {
            return this;
        }
        ClickEvent event = switch(clickAction) {
            case OPEN_URL -> new ClickEvent.OpenUrl(URI.create(value));
            case OPEN_FILE -> new ClickEvent.OpenFile(value);
            case RUN_COMMAND -> new ClickEvent.RunCommand(value);
            case SUGGEST_COMMAND -> new ClickEvent.SuggestCommand(value);
            case SHOW_DIALOG -> null;
            case CHANGE_PAGE -> new ClickEvent.ChangePage(Integer.parseInt(value));
            case COPY_TO_CLIPBOARD -> new ClickEvent.CopyToClipboard(value);
            case CUSTOM -> null;
        };
        applyStyle(style -> style.withClickEvent(event));
        return this;
    }

    /**
     * Set's the {@link Component} that will be shown on hover.
     *
     * @param hoverText Component for hover
     */
    public TextBuilder setHoverText(Component hoverText) {
        HoverEvent hover = new HoverEvent.ShowText(hoverText);
        applyStyle(style -> style.withHoverEvent(hover));
        return this;
    }
}
