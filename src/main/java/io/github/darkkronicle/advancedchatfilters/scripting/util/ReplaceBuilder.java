/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatfilters.scripting.util;

import io.github.darkkronicle.advancedchatcore.util.StringInsert;
import io.github.darkkronicle.advancedchatcore.util.StringMatch;
import java.util.HashMap;
import java.util.Map;

import io.github.darkkronicle.advancedchatcore.util.TextUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class ReplaceBuilder {

    private final Map<StringMatch, StringInsert> replacements = new HashMap<>();

    public ReplaceBuilder() {}

    /**
     * Applies replacements to a {@link FluidText}
     *
     * @param filter Component to apply it to
     * @return Filtered {@link FluidText}
     */
    public Component build(Component filter) {
        Component text = filter.copy();
        text = TextUtil.replaceStrings(text, replacements);
        return text;
    }

    /**
     * Add's a replacement to trigger on build.
     *
     * <p>Replacement will inherit the style of whatever it is replacing
     *
     * @param match {@link StringMatch} match data
     * @param replacement String to replace to
     */
    public ReplaceBuilder addReplacement(StringMatch match, String replacement) {
        replacements.put(
                match, (current, match1) -> Component.literal(replacement).setStyle(current.getStyle()));
        return this;
    }

    /**
     * Add's a replacement to trigger on build.
     *
     * @param match {@link StringMatch} match data
     * @param text Component to replace to
     */
    public ReplaceBuilder addReplacement(StringMatch match, Component text) {
        replacements.put(match, (current, match1) -> (MutableComponent) text);
        return this;
    }
}
