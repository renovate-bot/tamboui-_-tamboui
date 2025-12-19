/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.text;

import io.github.jratatui.style.Color;
import io.github.jratatui.style.Style;

/**
 * A string with a single style applied. The smallest styled text unit.
 */
public record Span(String content, Style style) {

    public static Span raw(String content) {
        return new Span(content, Style.EMPTY);
    }

    public static Span styled(String content, Style style) {
        return new Span(content, style);
    }

    /**
     * Returns the display width of this span (simplified - counts code points).
     * For proper Unicode width handling, a library like ICU4J would be needed.
     */
    public int width() {
        return content.codePointCount(0, content.length());
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    // Style builder methods

    public Span style(Style newStyle) {
        return new Span(content, newStyle);
    }

    public Span patchStyle(Style patch) {
        return new Span(content, style.patch(patch));
    }

    public Span fg(Color color) {
        return new Span(content, style.fg(color));
    }

    public Span bg(Color color) {
        return new Span(content, style.bg(color));
    }

    public Span bold() {
        return new Span(content, style.bold());
    }

    public Span italic() {
        return new Span(content, style.italic());
    }

    public Span underlined() {
        return new Span(content, style.underlined());
    }

    public Span dim() {
        return new Span(content, style.dim());
    }

    public Span reversed() {
        return new Span(content, style.reversed());
    }

    public Span crossedOut() {
        return new Span(content, style.crossedOut());
    }

    // Color convenience methods

    public Span black() {
        return fg(Color.BLACK);
    }

    public Span red() {
        return fg(Color.RED);
    }

    public Span green() {
        return fg(Color.GREEN);
    }

    public Span yellow() {
        return fg(Color.YELLOW);
    }

    public Span blue() {
        return fg(Color.BLUE);
    }

    public Span magenta() {
        return fg(Color.MAGENTA);
    }

    public Span cyan() {
        return fg(Color.CYAN);
    }

    public Span white() {
        return fg(Color.WHITE);
    }

    public Span gray() {
        return fg(Color.GRAY);
    }
}
