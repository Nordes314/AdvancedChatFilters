/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatfilters.config.gui;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;
import fi.dy.masa.malilib.gui.wrappers.TextFieldWrapper;
import io.github.darkkronicle.advancedchatfilters.scripting.ScriptFilter;
import io.github.darkkronicle.advancedchatfilters.scripting.ScriptManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;

public class WidgetListAdvancedFilters
        extends WidgetListBase<ScriptFilter, WidgetAdvancedFilterEntry> {

    protected final List<TextFieldWrapper<? extends GuiTextFieldGeneric>> textFields =
            new ArrayList<>();

    @Override
    protected void reCreateListEntryWidgets() {
        this.textFields.clear();
        super.reCreateListEntryWidgets();
    }

    public WidgetListAdvancedFilters(
            int x,
            int y,
            int width,
            int height,
            ISelectionListener<ScriptFilter> selectionListener,
            Screen parent) {
        super(x, y, width, height, selectionListener);
        this.browserEntryHeight = 22;
        this.setParent(parent);
    }

    public void addTextField(TextFieldWrapper<? extends GuiTextFieldGeneric> text) {
        textFields.add(text);
    }

    @Override
    public boolean onMouseClicked(MouseButtonEvent click, boolean doubled) {
        clearTextFieldFocus();
        return super.onMouseClicked(click, doubled);
    }

    protected void clearTextFieldFocus() {
        for (TextFieldWrapper<? extends GuiTextFieldGeneric> field : this.textFields) {
            GuiTextFieldGeneric textField = field.textField();

            if (textField.isFocused()) {
                textField.setFocused(false);
                break;
            }
        }
    }

    @Override
    public boolean onKeyTyped(KeyEvent input) {
        for (WidgetAdvancedFilterEntry widget : this.listWidgets) {
            if (widget.onKeyTyped(input)) {
                return true;
            }
        }
        return super.onKeyTyped(input);
    }

    @Override
    protected WidgetAdvancedFilterEntry createListEntryWidget(
            int x, int y, int listIndex, boolean isOdd, ScriptFilter entry) {
        return new WidgetAdvancedFilterEntry(
                x,
                y,
                this.browserEntryWidth,
                this.getBrowserEntryHeightFor(entry),
                isOdd,
                entry,
                listIndex,
                this);
    }

    @Override
    protected Collection<ScriptFilter> getAllEntries() {
        List<ScriptFilter> filters = new ArrayList<>(ScriptManager.getInstance().getFilters());
        filters.addAll(ScriptManager.getInstance().getUnimportedFilters());
        return filters;
    }
}
