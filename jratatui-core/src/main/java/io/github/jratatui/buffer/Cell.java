/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.buffer;

import io.github.jratatui.style.Style;

/**
 * A single cell in the terminal buffer.
 */
public record Cell(String symbol, Style style) {

    public static final Cell EMPTY = new Cell(" ", Style.EMPTY);

    public Cell reset() {
        return EMPTY;
    }

    public Cell symbol(String symbol) {
        return new Cell(symbol, this.style);
    }

    public Cell style(Style style) {
        return new Cell(this.symbol, style);
    }

    public Cell patchStyle(Style patch) {
        return new Cell(this.symbol, this.style.patch(patch));
    }

    public boolean isEmpty() {
        return " ".equals(symbol) && style.equals(Style.EMPTY);
    }
}
