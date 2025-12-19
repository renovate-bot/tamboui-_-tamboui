/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.widgets.table;

import io.github.jratatui.style.Color;
import io.github.jratatui.style.Style;
import io.github.jratatui.text.Line;
import io.github.jratatui.text.Span;
import io.github.jratatui.text.Text;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

class CellTest {

    @Test
    @DisplayName("Cell.from(String) creates cell with text content")
    void fromString() {
        var cell = Cell.from("Hello");
        assertThat(cell.content().width()).isEqualTo(5);
        assertThat(cell.height()).isEqualTo(1);
        assertThat(cell.style()).isEqualTo(Style.EMPTY);
    }

    @Test
    @DisplayName("Cell.from(Span) creates cell with styled span")
    void fromSpan() {
        var span = Span.raw("Styled").bold();
        var cell = Cell.from(span);
        assertThat(cell.content().width()).isEqualTo(6);
    }

    @Test
    @DisplayName("Cell.from(Line) creates cell with line content")
    void fromLine() {
        var line = Line.from(Span.raw("First"), Span.raw("Second"));
        var cell = Cell.from(line);
        assertThat(cell.content().width()).isEqualTo(11);
    }

    @Test
    @DisplayName("Cell.from(Text) creates cell with multi-line content")
    void fromText() {
        var text = Text.from("Line 1\nLine 2\nLine 3");
        var cell = Cell.from(text);
        assertThat(cell.height()).isEqualTo(3);
        assertThat(cell.width()).isEqualTo(6);
    }

    @Test
    @DisplayName("Cell.empty creates empty cell")
    void emptyCell() {
        var cell = Cell.empty();
        assertThat(cell.width()).isEqualTo(0);
        assertThat(cell.height()).isEqualTo(1);
    }

    @Test
    @DisplayName("Cell.style returns new cell with style")
    void withStyle() {
        var cell = Cell.from("Test");
        var styled = cell.style(Style.EMPTY.fg(Color.RED));

        assertThat(styled.style().fg()).contains(Color.RED);
        // Original is unchanged
        assertThat(cell.style()).isEqualTo(Style.EMPTY);
    }
}
