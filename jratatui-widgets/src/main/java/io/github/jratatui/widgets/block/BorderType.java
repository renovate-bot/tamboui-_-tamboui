/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.widgets.block;

/**
 * Type of border to draw.
 */
public enum BorderType {
    PLAIN(new BorderSet("─", "│", "┌", "┐", "└", "┘")),
    ROUNDED(new BorderSet("─", "│", "╭", "╮", "╰", "╯")),
    DOUBLE(new BorderSet("═", "║", "╔", "╗", "╚", "╝")),
    THICK(new BorderSet("━", "┃", "┏", "┓", "┗", "┛")),
    QUADRANT_INSIDE(new BorderSet("▀", "▌", "▘", "▝", "▖", "▗")),
    QUADRANT_OUTSIDE(new BorderSet("▄", "▐", "▛", "▜", "▙", "▟"));

    private final BorderSet set;

    BorderType(BorderSet set) {
        this.set = set;
    }

    public BorderSet set() {
        return set;
    }
}
