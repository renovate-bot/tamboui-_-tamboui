/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.widgets.gauge;

import io.github.jratatui.buffer.Buffer;
import io.github.jratatui.layout.Rect;
import io.github.jratatui.style.Color;
import io.github.jratatui.style.Style;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

class LineGaugeTest {

    @Test
    @DisplayName("LineGauge renders at 0%")
    void rendersAtZeroPercent() {
        var gauge = LineGauge.percent(0);
        var area = new Rect(0, 0, 10, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        // All unfilled
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("─");
        assertThat(buffer.get(9, 0).symbol()).isEqualTo("─");
    }

    @Test
    @DisplayName("LineGauge renders at 100%")
    void rendersAtHundredPercent() {
        var gauge = LineGauge.percent(100);
        var area = new Rect(0, 0, 10, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        // All filled
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("━");
        assertThat(buffer.get(9, 0).symbol()).isEqualTo("━");
    }

    @Test
    @DisplayName("LineGauge renders at 50%")
    void rendersAtFiftyPercent() {
        var gauge = LineGauge.percent(50);
        var area = new Rect(0, 0, 10, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        // First half filled
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("━");
        assertThat(buffer.get(4, 0).symbol()).isEqualTo("━");
        // Second half unfilled
        assertThat(buffer.get(5, 0).symbol()).isEqualTo("─");
        assertThat(buffer.get(9, 0).symbol()).isEqualTo("─");
    }

    @Test
    @DisplayName("LineGauge with ratio")
    void withRatio() {
        var gauge = LineGauge.ratio(0.25);
        var area = new Rect(0, 0, 20, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        // First 5 cells (25% of 20) filled
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("━");
        assertThat(buffer.get(4, 0).symbol()).isEqualTo("━");
        // Rest unfilled
        assertThat(buffer.get(5, 0).symbol()).isEqualTo("─");
    }

    @Test
    @DisplayName("LineGauge with label")
    void withLabel() {
        var gauge = LineGauge.builder()
            .percent(50)
            .label("CPU: ")
            .build();
        var area = new Rect(0, 0, 15, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        // Label at start
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("C");
        assertThat(buffer.get(1, 0).symbol()).isEqualTo("P");
        assertThat(buffer.get(2, 0).symbol()).isEqualTo("U");
        assertThat(buffer.get(3, 0).symbol()).isEqualTo(":");
        assertThat(buffer.get(4, 0).symbol()).isEqualTo(" ");
        // Gauge starts after label (position 5)
        // Remaining width is 10, 50% = 5 filled
        assertThat(buffer.get(5, 0).symbol()).isEqualTo("━");
        assertThat(buffer.get(9, 0).symbol()).isEqualTo("━");
        assertThat(buffer.get(10, 0).symbol()).isEqualTo("─");
    }

    @Test
    @DisplayName("LineGauge with filled style")
    void withFilledStyle() {
        var filledStyle = Style.EMPTY.fg(Color.GREEN);
        var gauge = LineGauge.builder()
            .percent(100)
            .filledStyle(filledStyle)
            .build();
        var area = new Rect(0, 0, 10, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        assertThat(buffer.get(0, 0).style().fg()).contains(Color.GREEN);
    }

    @Test
    @DisplayName("LineGauge with unfilled style")
    void withUnfilledStyle() {
        var unfilledStyle = Style.EMPTY.fg(Color.DARK_GRAY);
        var gauge = LineGauge.builder()
            .percent(0)
            .unfilledStyle(unfilledStyle)
            .build();
        var area = new Rect(0, 0, 10, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        assertThat(buffer.get(0, 0).style().fg()).contains(Color.DARK_GRAY);
    }

    @Test
    @DisplayName("LineGauge with thick line set")
    void withThickLineSet() {
        var gauge = LineGauge.builder()
            .percent(50)
            .lineSet(LineGauge.THICK)
            .build();
        var area = new Rect(0, 0, 10, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        // Both filled and unfilled use thick lines
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("━");
        assertThat(buffer.get(9, 0).symbol()).isEqualTo("━");
    }

    @Test
    @DisplayName("LineGauge with double line set")
    void withDoubleLineSet() {
        var gauge = LineGauge.builder()
            .percent(50)
            .lineSet(LineGauge.DOUBLE)
            .build();
        var area = new Rect(0, 0, 10, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        assertThat(buffer.get(0, 0).symbol()).isEqualTo("═");
        assertThat(buffer.get(9, 0).symbol()).isEqualTo("═");
    }

    @Test
    @DisplayName("LineGauge with custom line set")
    void withCustomLineSet() {
        var customSet = new LineGauge.LineSet(".", "#");
        var gauge = LineGauge.builder()
            .percent(50)
            .lineSet(customSet)
            .build();
        var area = new Rect(0, 0, 10, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        assertThat(buffer.get(0, 0).symbol()).isEqualTo("#");
        assertThat(buffer.get(9, 0).symbol()).isEqualTo(".");
    }

    @Test
    @DisplayName("LineGauge percent validation")
    void percentValidation() {
        assertThatThrownBy(() -> LineGauge.builder().percent(-1))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> LineGauge.builder().percent(101))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("LineGauge ratio validation")
    void ratioValidation() {
        assertThatThrownBy(() -> LineGauge.builder().ratio(-0.1))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> LineGauge.builder().ratio(1.1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("LineSet validation")
    void lineSetValidation() {
        // Empty strings should throw IllegalArgumentException
        assertThatThrownBy(() -> new LineGauge.LineSet("", "#"))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new LineGauge.LineSet(".", ""))
            .isInstanceOf(IllegalArgumentException.class);
        // Valid LineSet should work
        var validSet = new LineGauge.LineSet(".", "#");
        assertThat(validSet.unfilled()).isEqualTo(".");
        assertThat(validSet.filled()).isEqualTo("#");
    }
}
