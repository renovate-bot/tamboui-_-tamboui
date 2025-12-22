/*
 * Copyright (c) 2025 TamboUI Contributors
 * SPDX-License-Identifier: MIT
 */
package dev.tamboui.toolkit.elements;

import dev.tamboui.toolkit.element.RenderContext;
import dev.tamboui.toolkit.element.StyledElement;
import dev.tamboui.layout.Rect;
import dev.tamboui.terminal.Frame;
import dev.tamboui.text.Line;
import dev.tamboui.text.Span;
import dev.tamboui.widgets.paragraph.Paragraph;

/**
 * A simple text element that displays styled text.
 */
public final class TextElement extends StyledElement<TextElement> {

    private final String content;

    public TextElement(String content) {
        this.content = content != null ? content : "";
    }

    public TextElement(Object value) {
        this.content = value != null ? String.valueOf(value) : "";
    }

    /**
     * Returns the text content.
     */
    public String content() {
        return content;
    }

    @Override
    public void render(Frame frame, Rect area, RenderContext context) {
        if (area.isEmpty() || content.isEmpty()) {
            return;
        }

        // Create a styled span and render as a paragraph
        Span span = Span.styled(content, style);
        Paragraph paragraph = Paragraph.from(Line.from(span));
        frame.renderWidget(paragraph, area);
    }
}
