/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.widgets.tabs;

import io.github.jratatui.buffer.Buffer;
import io.github.jratatui.layout.Rect;
import io.github.jratatui.style.Color;
import io.github.jratatui.style.Style;
import io.github.jratatui.text.Line;
import io.github.jratatui.text.Span;
import io.github.jratatui.widgets.block.Block;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

class TabsTest {

    @Test
    @DisplayName("Tabs renders basic content")
    void rendersBasicContent() {
        var tabs = Tabs.from("Tab1", "Tab2", "Tab3");
        var area = new Rect(0, 0, 30, 1);
        var buffer = Buffer.empty(area);
        var state = new TabsState();

        tabs.render(area, buffer, state);

        assertThat(buffer.get(0, 0).symbol()).isEqualTo("T");
        assertThat(buffer.get(1, 0).symbol()).isEqualTo("a");
        assertThat(buffer.get(2, 0).symbol()).isEqualTo("b");
        assertThat(buffer.get(3, 0).symbol()).isEqualTo("1");
    }

    @Test
    @DisplayName("Tabs renders with divider")
    void rendersWithDivider() {
        var tabs = Tabs.builder()
            .titles("A", "B")
            .divider(" | ")
            .build();
        var area = new Rect(0, 0, 20, 1);
        var buffer = Buffer.empty(area);
        var state = new TabsState();

        tabs.render(area, buffer, state);

        // "A | B"
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("A");
        assertThat(buffer.get(1, 0).symbol()).isEqualTo(" ");
        assertThat(buffer.get(2, 0).symbol()).isEqualTo("|");
        assertThat(buffer.get(3, 0).symbol()).isEqualTo(" ");
        assertThat(buffer.get(4, 0).symbol()).isEqualTo("B");
    }

    @Test
    @DisplayName("Tabs renders with custom divider")
    void rendersWithCustomDivider() {
        var tabs = Tabs.builder()
            .titles("X", "Y")
            .divider(" - ")
            .build();
        var area = new Rect(0, 0, 20, 1);
        var buffer = Buffer.empty(area);
        var state = new TabsState();

        tabs.render(area, buffer, state);

        // "X - Y"
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("X");
        assertThat(buffer.get(2, 0).symbol()).isEqualTo("-");
    }

    @Test
    @DisplayName("Tabs renders with selection")
    void rendersWithSelection() {
        var highlightStyle = Style.EMPTY.fg(Color.YELLOW);
        var tabs = Tabs.builder()
            .titles("First", "Second")
            .highlightStyle(highlightStyle)
            .divider("|")
            .build();
        var area = new Rect(0, 0, 20, 1);
        var buffer = Buffer.empty(area);
        var state = new TabsState(1); // Select second tab

        tabs.render(area, buffer, state);

        // Second tab should have highlight style
        // "First|Second" - Second starts at position 6
        assertThat(buffer.get(6, 0).style().fg()).contains(Color.YELLOW);
    }

    @Test
    @DisplayName("Tabs with block")
    void withBlock() {
        var tabs = Tabs.builder()
            .titles("Tab1")
            .block(Block.bordered())
            .divider("")
            .build();
        var area = new Rect(0, 0, 15, 3);
        var buffer = Buffer.empty(area);
        var state = new TabsState();

        tabs.render(area, buffer, state);

        // Block border
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("┌");
        // Tab inside block
        assertThat(buffer.get(1, 1).symbol()).isEqualTo("T");
    }

    @Test
    @DisplayName("Tabs with padding")
    void withPadding() {
        var tabs = Tabs.builder()
            .titles("A", "B")
            .divider("|")
            .padding(" ", " ")
            .build();
        var area = new Rect(0, 0, 20, 1);
        var buffer = Buffer.empty(area);
        var state = new TabsState();

        tabs.render(area, buffer, state);

        // " A | B "
        assertThat(buffer.get(0, 0).symbol()).isEqualTo(" ");
        assertThat(buffer.get(1, 0).symbol()).isEqualTo("A");
        assertThat(buffer.get(2, 0).symbol()).isEqualTo(" ");
        assertThat(buffer.get(3, 0).symbol()).isEqualTo("|");
    }

    @Test
    @DisplayName("Tabs size returns correct count")
    void sizeReturnsCount() {
        var tabs = Tabs.from("A", "B", "C", "D");
        assertThat(tabs.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("Tabs titles accessor")
    void titlesAccessor() {
        var tabs = Tabs.from("First", "Second");
        assertThat(tabs.titles()).hasSize(2);
    }

    @Test
    @DisplayName("Tabs from Line array")
    void fromLineArray() {
        var tabs = Tabs.from(
            Line.from(Span.raw("Tab1").bold()),
            Line.from("Tab2")
        );
        assertThat(tabs.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Empty tabs renders nothing")
    void emptyTabs() {
        var tabs = Tabs.builder().build();
        var area = new Rect(0, 0, 20, 1);
        var buffer = Buffer.empty(area);
        var state = new TabsState();

        tabs.render(area, buffer, state);

        assertThat(buffer.get(0, 0).symbol()).isEqualTo(" ");
    }

    @Test
    @DisplayName("Tabs truncates when area too small")
    void truncatesWhenTooSmall() {
        var tabs = Tabs.builder()
            .titles("VeryLongTabName", "Another")
            .divider("|")
            .build();
        var area = new Rect(0, 0, 10, 1);
        var buffer = Buffer.empty(area);
        var state = new TabsState();

        tabs.render(area, buffer, state);

        // Should not crash, renders what fits
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("V");
    }

    @Test
    @DisplayName("Tabs with styled divider")
    void styledDivider() {
        var dividerSpan = Span.raw(" | ").fg(Color.DARK_GRAY);
        var tabs = Tabs.builder()
            .titles("A", "B")
            .divider(dividerSpan)
            .build();
        var area = new Rect(0, 0, 20, 1);
        var buffer = Buffer.empty(area);
        var state = new TabsState();

        tabs.render(area, buffer, state);

        // Divider should have style
        assertThat(buffer.get(2, 0).style().fg()).contains(Color.DARK_GRAY);
    }

    @Test
    @DisplayName("Tabs builder addTitle")
    void builderAddTitle() {
        var tabs = Tabs.builder()
            .addTitle("First")
            .addTitle("Second")
            .addTitle(Line.from("Third"))
            .build();
        assertThat(tabs.size()).isEqualTo(3);
    }
}
