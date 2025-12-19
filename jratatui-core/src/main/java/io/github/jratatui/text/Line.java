/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.text;

import io.github.jratatui.layout.Alignment;
import io.github.jratatui.style.Color;
import io.github.jratatui.style.Style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A single line of text composed of styled spans.
 */
public record Line(List<Span> spans, Optional<Alignment> alignment) {

    public Line {
        spans = List.copyOf(spans);
    }

    public static Line empty() {
        return new Line(List.of(), Optional.empty());
    }

    public static Line from(String text) {
        return new Line(List.of(Span.raw(text)), Optional.empty());
    }

    public static Line from(Span span) {
        return new Line(List.of(span), Optional.empty());
    }

    public static Line from(Span... spans) {
        return new Line(Arrays.asList(spans), Optional.empty());
    }

    public static Line from(List<Span> spans) {
        return new Line(spans, Optional.empty());
    }

    public static Line styled(String text, Style style) {
        return new Line(List.of(Span.styled(text, style)), Optional.empty());
    }

    /**
     * Returns the display width of this line (sum of span widths).
     */
    public int width() {
        return spans.stream().mapToInt(Span::width).sum();
    }

    public boolean isEmpty() {
        return spans.isEmpty() || spans.stream().allMatch(Span::isEmpty);
    }

    public Line alignment(Alignment alignment) {
        return new Line(spans, Optional.of(alignment));
    }

    public Line left() {
        return alignment(Alignment.LEFT);
    }

    public Line centered() {
        return alignment(Alignment.CENTER);
    }

    public Line right() {
        return alignment(Alignment.RIGHT);
    }

    /**
     * Applies a style patch to all spans.
     */
    public Line patchStyle(Style style) {
        var newSpans = spans.stream()
            .map(span -> span.patchStyle(style))
            .toList();
        return new Line(newSpans, alignment);
    }

    public Line fg(Color color) {
        return patchStyle(Style.EMPTY.fg(color));
    }

    public Line bg(Color color) {
        return patchStyle(Style.EMPTY.bg(color));
    }

    public Line bold() {
        return patchStyle(Style.EMPTY.bold());
    }

    public Line italic() {
        return patchStyle(Style.EMPTY.italic());
    }

    public Line underlined() {
        return patchStyle(Style.EMPTY.underlined());
    }

    /**
     * Returns the raw text content without styling.
     */
    public String rawContent() {
        var sb = new StringBuilder();
        for (var span : spans) {
            sb.append(span.content());
        }
        return sb.toString();
    }

    /**
     * Appends another line's spans to this line.
     */
    public Line append(Line other) {
        var newSpans = new ArrayList<>(spans);
        newSpans.addAll(other.spans);
        return new Line(newSpans, alignment);
    }

    /**
     * Appends a span to this line.
     */
    public Line append(Span span) {
        var newSpans = new ArrayList<>(spans);
        newSpans.add(span);
        return new Line(newSpans, alignment);
    }
}
