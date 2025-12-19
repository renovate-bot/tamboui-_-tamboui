/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.widgets.list;

import io.github.jratatui.buffer.Buffer;
import io.github.jratatui.layout.Rect;
import io.github.jratatui.style.Color;
import io.github.jratatui.style.Style;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ListWidgetTest {

    @Test
    @DisplayName("ListWidget renders items")
    void rendersItems() {
        var items = List.of(
            ListItem.from("Item 1"),
            ListItem.from("Item 2"),
            ListItem.from("Item 3")
        );
        var list = ListWidget.builder()
            .items(items)
            .highlightSymbol("") // No highlight symbol for simple rendering
            .build();
        var area = new Rect(0, 0, 20, 3);
        var buffer = Buffer.empty(area);
        var state = new ListState();

        list.render(area, buffer, state);

        assertThat(buffer.get(0, 0).symbol()).isEqualTo("I");
        assertThat(buffer.get(0, 1).symbol()).isEqualTo("I");
        assertThat(buffer.get(0, 2).symbol()).isEqualTo("I");
    }

    @Test
    @DisplayName("ListWidget with selection")
    void withSelection() {
        var items = List.of(
            ListItem.from("Item 1"),
            ListItem.from("Item 2")
        );
        var highlightStyle = Style.EMPTY.fg(Color.YELLOW);
        var list = ListWidget.builder()
            .items(items)
            .highlightStyle(highlightStyle)
            .build();
        var area = new Rect(0, 0, 20, 2);
        var buffer = Buffer.empty(area);
        var state = new ListState();
        state.select(1);

        list.render(area, buffer, state);

        // Second item should have highlight style
        assertThat(buffer.get(0, 1).style().fg()).contains(Color.YELLOW);
    }

    @Test
    @DisplayName("ListWidget with highlight symbol")
    void withHighlightSymbol() {
        var items = List.of(
            ListItem.from("Item 1"),
            ListItem.from("Item 2")
        );
        var list = ListWidget.builder()
            .items(items)
            .highlightSymbol("> ")
            .build();
        var area = new Rect(0, 0, 20, 2);
        var buffer = Buffer.empty(area);
        var state = new ListState();
        state.select(0);

        list.render(area, buffer, state);

        assertThat(buffer.get(0, 0).symbol()).isEqualTo(">");
        assertThat(buffer.get(1, 0).symbol()).isEqualTo(" ");
        assertThat(buffer.get(2, 0).symbol()).isEqualTo("I");
    }

    @Test
    @DisplayName("ListState selection navigation")
    void stateNavigation() {
        var state = new ListState();

        assertThat(state.selected()).isNull();

        state.select(0);
        assertThat(state.selected()).isEqualTo(0);

        state.selectNext(5);
        assertThat(state.selected()).isEqualTo(1);

        state.selectPrevious();
        assertThat(state.selected()).isEqualTo(0);

        // Should not go below 0
        state.selectPrevious();
        assertThat(state.selected()).isEqualTo(0);
    }

    @Test
    @DisplayName("ListState selectNext wraps or stops at end")
    void selectNextAtEnd() {
        var state = new ListState();
        state.select(4);

        state.selectNext(5);
        assertThat(state.selected()).isEqualTo(4); // stays at end
    }
}
