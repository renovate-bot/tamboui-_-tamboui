/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.widgets.list;

import io.github.jratatui.style.Style;
import io.github.jratatui.text.Line;
import io.github.jratatui.text.Text;

/**
 * An item in a list widget.
 */
public record ListItem(Text content, Style style) {

    public static ListItem from(String text) {
        return new ListItem(Text.from(text), Style.EMPTY);
    }

    public static ListItem from(Line line) {
        return new ListItem(Text.from(line), Style.EMPTY);
    }

    public static ListItem from(Text text) {
        return new ListItem(text, Style.EMPTY);
    }

    public ListItem style(Style style) {
        return new ListItem(content, style);
    }

    public int height() {
        return content.height();
    }
}
