/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.widgets.gauge;

import io.github.jratatui.buffer.Buffer;
import io.github.jratatui.layout.Rect;
import io.github.jratatui.style.Color;
import io.github.jratatui.style.Style;
import io.github.jratatui.widgets.block.Block;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

class GaugeTest {

    @Test
    @DisplayName("Gauge renders at 0%")
    void rendersAtZeroPercent() {
        var gauge = Gauge.percent(0);
        var area = new Rect(0, 0, 10, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        // Label "0%" should be centered - in 10 chars, "0%" (2 chars) starts at position 4
        assertThat(buffer.get(4, 0).symbol()).isEqualTo("0");
        assertThat(buffer.get(5, 0).symbol()).isEqualTo("%");
        // No filled blocks at 0%
        assertThat(buffer.get(0, 0).symbol()).isEqualTo(" ");
    }

    @Test
    @DisplayName("Gauge renders at 100%")
    void rendersAtHundredPercent() {
        var gauge = Gauge.percent(100);
        var area = new Rect(0, 0, 10, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        // All cells should have full block (except where label overwrites)
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("█");
        assertThat(buffer.get(9, 0).symbol()).isEqualTo("█");
    }

    @Test
    @DisplayName("Gauge renders at 50%")
    void rendersAtFiftyPercent() {
        var gauge = Gauge.builder()
            .percent(50)
            .label("") // Empty label to check fill without interference
            .build();
        var area = new Rect(0, 0, 10, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        // First 5 cells should be filled
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("█");
        assertThat(buffer.get(4, 0).symbol()).isEqualTo("█");
        // Cells after position 5 should be empty
        assertThat(buffer.get(5, 0).symbol()).isEqualTo(" ");
    }

    @Test
    @DisplayName("Gauge with ratio")
    void withRatio() {
        var gauge = Gauge.ratio(0.25);
        var area = new Rect(0, 0, 20, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        // First 5 cells (25% of 20) should be filled
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("█");
        assertThat(buffer.get(4, 0).symbol()).isEqualTo("█");
    }

    @Test
    @DisplayName("Gauge with custom label")
    void withCustomLabel() {
        var gauge = Gauge.builder()
            .percent(50)
            .label("Loading...")
            .build();
        var area = new Rect(0, 0, 20, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        // "Loading..." is 10 chars, centered in 20 = starts at position 5
        assertThat(buffer.get(5, 0).symbol()).isEqualTo("L");
        assertThat(buffer.get(6, 0).symbol()).isEqualTo("o");
    }

    @Test
    @DisplayName("Gauge with gauge style")
    void withGaugeStyle() {
        var gaugeStyle = Style.EMPTY.fg(Color.GREEN);
        var gauge = Gauge.builder()
            .percent(100)
            .gaugeStyle(gaugeStyle)
            .build();
        var area = new Rect(0, 0, 10, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        assertThat(buffer.get(0, 0).style().fg()).contains(Color.GREEN);
    }

    @Test
    @DisplayName("Gauge with block")
    void withBlock() {
        var gauge = Gauge.builder()
            .percent(50)
            .block(Block.bordered())
            .build();
        var area = new Rect(0, 0, 12, 3);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        // Block border should be rendered
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("┌");
        assertThat(buffer.get(11, 0).symbol()).isEqualTo("┐");
        // Gauge renders inside block
        assertThat(buffer.get(1, 1).symbol()).isEqualTo("█");
    }

    @Test
    @DisplayName("Gauge without unicode uses only full blocks")
    void withoutUnicode() {
        var gauge = Gauge.builder()
            .ratio(0.15) // 15% of 10 = 1.5 cells
            .useUnicode(false)
            .build();
        var area = new Rect(0, 0, 10, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        // Only first cell should be full block, no partial
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("█");
        // Second cell should not have partial block
        assertThat(buffer.get(1, 0).symbol()).isNotEqualTo("█");
    }

    @Test
    @DisplayName("Gauge with unicode uses partial blocks")
    void withUnicode() {
        var gauge = Gauge.builder()
            .ratio(0.15) // 15% of 10 = 1.5 cells
            .useUnicode(true)
            .label("") // Empty label to not interfere
            .build();
        var area = new Rect(0, 0, 10, 1);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        // First cell should be full block
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("█");
        // Second cell should have partial block (4/8 = ▌)
        assertThat(buffer.get(1, 0).symbol()).isEqualTo("▌");
    }

    @Test
    @DisplayName("Gauge percent validation")
    void percentValidation() {
        assertThatThrownBy(() -> Gauge.builder().percent(-1))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Gauge.builder().percent(101))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Gauge ratio validation")
    void ratioValidation() {
        assertThatThrownBy(() -> Gauge.builder().ratio(-0.1))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Gauge.builder().ratio(1.1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Gauge renders in multi-line area")
    void multiLineArea() {
        var gauge = Gauge.percent(50);
        var area = new Rect(0, 0, 10, 3);
        var buffer = Buffer.empty(area);

        gauge.render(area, buffer);

        // All rows should have the gauge
        assertThat(buffer.get(0, 0).symbol()).isEqualTo("█");
        assertThat(buffer.get(0, 1).symbol()).isEqualTo("█");
        assertThat(buffer.get(0, 2).symbol()).isEqualTo("█");
    }
}
