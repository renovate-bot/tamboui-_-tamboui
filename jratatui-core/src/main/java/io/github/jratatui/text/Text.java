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
 * Multi-line styled text, composed of Lines.
 */
public record Text(List<Line> lines, Optional<Alignment> alignment) {

    public Text {
        lines = List.copyOf(lines);
    }

    public static Text empty() {
        return new Text(List.of(), Optional.empty());
    }

    public static Text raw(String text) {
        var linesList = text.lines()
            .map(Line::from)
            .toList();
        return new Text(linesList, Optional.empty());
    }

    public static Text from(String text) {
        return raw(text);
    }

    public static Text from(Line line) {
        return new Text(List.of(line), Optional.empty());
    }

    public static Text from(Line... lines) {
        return new Text(Arrays.asList(lines), Optional.empty());
    }

    public static Text from(List<Line> lines) {
        return new Text(lines, Optional.empty());
    }

    public static Text from(Span span) {
        return new Text(List.of(Line.from(span)), Optional.empty());
    }

    public static Text styled(String text, Style style) {
        var linesList = text.lines()
            .map(line -> Line.styled(line, style))
            .toList();
        return new Text(linesList, Optional.empty());
    }

    /**
     * Returns the height (number of lines).
     */
    public int height() {
        return lines.size();
    }

    /**
     * Returns the maximum width of any line.
     */
    public int width() {
        return lines.stream()
            .mapToInt(Line::width)
            .max()
            .orElse(0);
    }

    public boolean isEmpty() {
        return lines.isEmpty() || lines.stream().allMatch(Line::isEmpty);
    }

    public Text alignment(Alignment alignment) {
        return new Text(lines, Optional.of(alignment));
    }

    public Text left() {
        return alignment(Alignment.LEFT);
    }

    public Text centered() {
        return alignment(Alignment.CENTER);
    }

    public Text right() {
        return alignment(Alignment.RIGHT);
    }

    /**
     * Applies a style patch to all lines.
     */
    public Text patchStyle(Style style) {
        var newLines = lines.stream()
            .map(line -> line.patchStyle(style))
            .toList();
        return new Text(newLines, alignment);
    }

    public Text fg(Color color) {
        return patchStyle(Style.EMPTY.fg(color));
    }

    public Text bg(Color color) {
        return patchStyle(Style.EMPTY.bg(color));
    }

    public Text bold() {
        return patchStyle(Style.EMPTY.bold());
    }

    public Text italic() {
        return patchStyle(Style.EMPTY.italic());
    }

    public Text underlined() {
        return patchStyle(Style.EMPTY.underlined());
    }

    /**
     * Returns the raw text content without styling.
     */
    public String rawContent() {
        var sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            if (i > 0) {
                sb.append('\n');
            }
            sb.append(lines.get(i).rawContent());
        }
        return sb.toString();
    }

    /**
     * Appends another text's lines to this text.
     */
    public Text append(Text other) {
        var newLines = new ArrayList<>(lines);
        newLines.addAll(other.lines);
        return new Text(newLines, alignment);
    }

    /**
     * Appends a line to this text.
     */
    public Text append(Line line) {
        var newLines = new ArrayList<>(lines);
        newLines.add(line);
        return new Text(newLines, alignment);
    }

    /**
     * Extends the last line with the given span, or adds a new line if empty.
     */
    public Text push(Span span) {
        if (lines.isEmpty()) {
            return append(Line.from(span));
        }
        var newLines = new ArrayList<>(lines);
        var lastLine = newLines.removeLast();
        newLines.add(lastLine.append(span));
        return new Text(newLines, alignment);
    }
}
