/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.widgets.block;

import io.github.jratatui.buffer.Buffer;
import io.github.jratatui.layout.Rect;
import io.github.jratatui.style.Color;
import io.github.jratatui.style.Style;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

class BlockTest {

    @Test
    @DisplayName("Block.bordered creates block with all borders")
    void bordered() {
        var block = Block.bordered();
        var area = new Rect(0, 0, 10, 5);
        var buffer = Buffer.empty(area);

        block.render(area, buffer);

        // Check corners (Plain border type is default)
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("┌");
        assertThat(buffer.get(9, 0).symbol()).isEqualTo("┐");
        assertThat(buffer.get(0, 4).symbol()).isEqualTo("└");
        assertThat(buffer.get(9, 4).symbol()).isEqualTo("┘");
    }

    @Test
    @DisplayName("Block inner area calculation with borders")
    void innerWithBorders() {
        var block = Block.bordered();
        var area = new Rect(0, 0, 10, 10);

        var inner = block.inner(area);

        assertThat(inner).isEqualTo(new Rect(1, 1, 8, 8));
    }

    @Test
    @DisplayName("Block inner area with padding")
    void innerWithPadding() {
        var block = Block.builder()
            .borders(Borders.ALL)
            .padding(Padding.uniform(2))
            .build();
        var area = new Rect(0, 0, 20, 20);

        var inner = block.inner(area);

        // 1 for border + 2 for padding on each side
        assertThat(inner.x()).isEqualTo(3);
        assertThat(inner.y()).isEqualTo(3);
        assertThat(inner.width()).isEqualTo(14); // 20 - 2*3
        assertThat(inner.height()).isEqualTo(14);
    }

    @Test
    @DisplayName("Block without borders")
    void noBorders() {
        var block = Block.builder().build();
        var area = new Rect(0, 0, 10, 5);
        var buffer = Buffer.empty(area);

        block.render(area, buffer);

        // No border characters should be drawn
        assertThat(buffer.get(0, 0).symbol()).isEqualTo(" ");
    }

    @Test
    @DisplayName("Block with title")
    void withTitle() {
        var block = Block.builder()
            .borders(Borders.ALL)
            .title(Title.from("Test"))
            .build();
        var area = new Rect(0, 0, 20, 5);
        var buffer = Buffer.empty(area);

        block.render(area, buffer);

        // Title should appear in top border
        assertThat(buffer.get(1, 0).symbol()).isEqualTo("T");
        assertThat(buffer.get(2, 0).symbol()).isEqualTo("e");
        assertThat(buffer.get(3, 0).symbol()).isEqualTo("s");
        assertThat(buffer.get(4, 0).symbol()).isEqualTo("t");
    }

    @Test
    @DisplayName("Block with border style")
    void withBorderStyle() {
        var style = Style.EMPTY.fg(Color.RED);
        var block = Block.builder()
            .borders(Borders.ALL)
            .borderStyle(style)
            .build();
        var area = new Rect(0, 0, 10, 5);
        var buffer = Buffer.empty(area);

        block.render(area, buffer);

        assertThat(buffer.get(0, 0).style().fg()).contains(Color.RED);
    }

    @Test
    @DisplayName("Block with different border types")
    void borderTypes() {
        var area = new Rect(0, 0, 5, 3);

        // Plain border
        var plainBlock = Block.builder()
            .borders(Borders.ALL)
            .borderType(BorderType.PLAIN)
            .build();
        var plainBuffer = Buffer.empty(area);
        plainBlock.render(area, plainBuffer);
        assertThat(plainBuffer.get(0, 0).symbol()).isEqualTo("┌");

        // Double border
        var doubleBlock = Block.builder()
            .borders(Borders.ALL)
            .borderType(BorderType.DOUBLE)
            .build();
        var doubleBuffer = Buffer.empty(area);
        doubleBlock.render(area, doubleBuffer);
        assertThat(doubleBuffer.get(0, 0).symbol()).isEqualTo("╔");
    }
}
