/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.layout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

class LayoutTest {

    @Test
    @DisplayName("Vertical layout with fixed lengths")
    void verticalFixedLengths() {
        var area = new Rect(0, 0, 100, 100);
        var layout = Layout.vertical()
            .constraints(
                Constraint.length(20),
                Constraint.length(30),
                Constraint.length(50)
            );

        var rects = layout.split(area);

        assertThat(rects).hasSize(3);
        assertThat(rects.get(0)).isEqualTo(new Rect(0, 0, 100, 20));
        assertThat(rects.get(1)).isEqualTo(new Rect(0, 20, 100, 30));
        assertThat(rects.get(2)).isEqualTo(new Rect(0, 50, 100, 50));
    }

    @Test
    @DisplayName("Horizontal layout with fixed lengths")
    void horizontalFixedLengths() {
        var area = new Rect(0, 0, 100, 50);
        var layout = Layout.horizontal()
            .constraints(
                Constraint.length(30),
                Constraint.length(70)
            );

        var rects = layout.split(area);

        assertThat(rects).hasSize(2);
        assertThat(rects.get(0)).isEqualTo(new Rect(0, 0, 30, 50));
        assertThat(rects.get(1)).isEqualTo(new Rect(30, 0, 70, 50));
    }

    @Test
    @DisplayName("Layout with percentages")
    void percentages() {
        var area = new Rect(0, 0, 100, 100);
        var layout = Layout.vertical()
            .constraints(
                Constraint.percentage(25),
                Constraint.percentage(75)
            );

        var rects = layout.split(area);

        assertThat(rects).hasSize(2);
        assertThat(rects.get(0).height()).isEqualTo(25);
        assertThat(rects.get(1).height()).isEqualTo(75);
    }

    @Test
    @DisplayName("Layout with fill constraints")
    void fillConstraints() {
        var area = new Rect(0, 0, 100, 100);
        var layout = Layout.vertical()
            .constraints(
                Constraint.length(20),
                Constraint.fill()
            );

        var rects = layout.split(area);

        assertThat(rects).hasSize(2);
        assertThat(rects.get(0).height()).isEqualTo(20);
        assertThat(rects.get(1).height()).isEqualTo(80);
    }

    @Test
    @DisplayName("Layout with margin")
    void withMargin() {
        var area = new Rect(0, 0, 100, 100);
        var layout = Layout.vertical()
            .margin(Margin.uniform(10))
            .constraints(Constraint.fill());

        var rects = layout.split(area);

        assertThat(rects).hasSize(1);
        assertThat(rects.get(0)).isEqualTo(new Rect(10, 10, 80, 80));
    }

    @Test
    @DisplayName("Layout with spacing")
    void withSpacing() {
        var area = new Rect(0, 0, 100, 100);
        var layout = Layout.vertical()
            .spacing(10)
            .constraints(
                Constraint.length(40),
                Constraint.length(40)
            );

        var rects = layout.split(area);

        assertThat(rects).hasSize(2);
        assertThat(rects.get(0)).isEqualTo(new Rect(0, 0, 100, 40));
        assertThat(rects.get(1)).isEqualTo(new Rect(0, 50, 100, 40));
    }

    @Test
    @DisplayName("Layout with min constraint")
    void minConstraint() {
        var area = new Rect(0, 0, 100, 100);
        var layout = Layout.vertical()
            .constraints(
                Constraint.min(30),
                Constraint.fill()
            );

        var rects = layout.split(area);

        assertThat(rects).hasSize(2);
        assertThat(rects.get(0).height()).isGreaterThanOrEqualTo(30);
    }

    @Test
    @DisplayName("Layout with ratio constraint")
    void ratioConstraint() {
        var area = new Rect(0, 0, 100, 100);
        var layout = Layout.vertical()
            .constraints(
                Constraint.ratio(1, 3),
                Constraint.ratio(2, 3)
            );

        var rects = layout.split(area);

        assertThat(rects).hasSize(2);
        assertThat(rects.get(0).height()).isEqualTo(33);
        assertThat(rects.get(1).height()).isEqualTo(66);
    }
}
