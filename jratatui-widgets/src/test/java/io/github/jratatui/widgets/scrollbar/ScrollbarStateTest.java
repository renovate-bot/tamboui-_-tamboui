/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.widgets.scrollbar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

class ScrollbarStateTest {

    @Test
    @DisplayName("Default state has zero content length")
    void defaultState() {
        var state = new ScrollbarState();

        assertThat(state.contentLength()).isEqualTo(0);
        assertThat(state.position()).isEqualTo(0);
        assertThat(state.viewportContentLength()).isEqualTo(0);
    }

    @Test
    @DisplayName("Constructor with content length")
    void constructorWithContentLength() {
        var state = new ScrollbarState(100);

        assertThat(state.contentLength()).isEqualTo(100);
        assertThat(state.position()).isEqualTo(0);
    }

    @Test
    @DisplayName("Content length cannot be negative")
    void contentLengthNotNegative() {
        var state = new ScrollbarState(-5);
        assertThat(state.contentLength()).isEqualTo(0);

        state.contentLength(-10);
        assertThat(state.contentLength()).isEqualTo(0);
    }

    @Test
    @DisplayName("Fluent setters return state for chaining")
    void fluentSetters() {
        var state = new ScrollbarState()
            .contentLength(50)
            .position(10)
            .viewportContentLength(20);

        assertThat(state.contentLength()).isEqualTo(50);
        assertThat(state.position()).isEqualTo(10);
        assertThat(state.viewportContentLength()).isEqualTo(20);
    }

    @Test
    @DisplayName("Position is clamped to valid range")
    void positionClamped() {
        var state = new ScrollbarState(10);

        state.position(-5);
        assertThat(state.position()).isEqualTo(0);

        state.position(100);
        assertThat(state.position()).isEqualTo(9); // max is contentLength - 1
    }

    @Test
    @DisplayName("Position is clamped when content length shrinks")
    void positionClampedOnShrink() {
        var state = new ScrollbarState(100).position(50);

        state.contentLength(30);
        assertThat(state.position()).isEqualTo(29);
    }

    @Test
    @DisplayName("first() scrolls to position 0")
    void first() {
        var state = new ScrollbarState(100).position(50);

        state.first();
        assertThat(state.position()).isEqualTo(0);
    }

    @Test
    @DisplayName("last() scrolls to last position")
    void last() {
        var state = new ScrollbarState(100).position(0);

        state.last();
        assertThat(state.position()).isEqualTo(99);
    }

    @Test
    @DisplayName("last() does nothing for empty content")
    void lastWithEmptyContent() {
        var state = new ScrollbarState(0);

        state.last();
        assertThat(state.position()).isEqualTo(0);
    }

    @Test
    @DisplayName("next() scrolls forward by one")
    void next() {
        var state = new ScrollbarState(10).position(5);

        state.next();
        assertThat(state.position()).isEqualTo(6);
    }

    @Test
    @DisplayName("next() stops at end")
    void nextStopsAtEnd() {
        var state = new ScrollbarState(10).position(9);

        state.next();
        assertThat(state.position()).isEqualTo(9);
    }

    @Test
    @DisplayName("prev() scrolls backward by one")
    void prev() {
        var state = new ScrollbarState(10).position(5);

        state.prev();
        assertThat(state.position()).isEqualTo(4);
    }

    @Test
    @DisplayName("prev() stops at start")
    void prevStopsAtStart() {
        var state = new ScrollbarState(10).position(0);

        state.prev();
        assertThat(state.position()).isEqualTo(0);
    }

    @Test
    @DisplayName("scrollBy with positive amount")
    void scrollByPositive() {
        var state = new ScrollbarState(100).position(10);

        state.scrollBy(5);
        assertThat(state.position()).isEqualTo(15);
    }

    @Test
    @DisplayName("scrollBy with negative amount")
    void scrollByNegative() {
        var state = new ScrollbarState(100).position(10);

        state.scrollBy(-3);
        assertThat(state.position()).isEqualTo(7);
    }

    @Test
    @DisplayName("scrollBy is clamped")
    void scrollByClamped() {
        var state = new ScrollbarState(10).position(5);

        state.scrollBy(100);
        assertThat(state.position()).isEqualTo(9);

        state.scrollBy(-100);
        assertThat(state.position()).isEqualTo(0);
    }

    @Test
    @DisplayName("pageDown uses viewport length")
    void pageDown() {
        var state = new ScrollbarState(100)
            .viewportContentLength(10)
            .position(0);

        state.pageDown();
        assertThat(state.position()).isEqualTo(10);
    }

    @Test
    @DisplayName("pageUp uses viewport length")
    void pageUp() {
        var state = new ScrollbarState(100)
            .viewportContentLength(10)
            .position(50);

        state.pageUp();
        assertThat(state.position()).isEqualTo(40);
    }

    @Test
    @DisplayName("pageDown/pageUp default to 1 without viewport")
    void pageDefaultsToOne() {
        var state = new ScrollbarState(100).position(50);

        state.pageDown();
        assertThat(state.position()).isEqualTo(51);

        state.pageUp();
        assertThat(state.position()).isEqualTo(50);
    }

    @Test
    @DisplayName("isAtStart returns true at position 0")
    void isAtStart() {
        var state = new ScrollbarState(100).position(0);
        assertThat(state.isAtStart()).isTrue();

        state.position(1);
        assertThat(state.isAtStart()).isFalse();
    }

    @Test
    @DisplayName("isAtEnd returns true at last position")
    void isAtEnd() {
        var state = new ScrollbarState(100).position(99);
        assertThat(state.isAtEnd()).isTrue();

        state.position(98);
        assertThat(state.isAtEnd()).isFalse();
    }

    @Test
    @DisplayName("isAtEnd returns true for empty content")
    void isAtEndWithEmptyContent() {
        var state = new ScrollbarState(0);
        assertThat(state.isAtEnd()).isTrue();
    }

    @Test
    @DisplayName("scrollPercentage calculates correctly")
    void scrollPercentage() {
        var state = new ScrollbarState(100);

        state.position(0);
        assertThat(state.scrollPercentage()).isEqualTo(0.0);

        state.position(99);
        assertThat(state.scrollPercentage()).isEqualTo(1.0);

        state.position(49);
        assertThat(state.scrollPercentage()).isCloseTo(0.495, within(0.01));
    }

    @Test
    @DisplayName("scrollPercentage returns 0 for single item")
    void scrollPercentageSingleItem() {
        var state = new ScrollbarState(1);
        assertThat(state.scrollPercentage()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("scrollPercentage returns 0 for empty content")
    void scrollPercentageEmptyContent() {
        var state = new ScrollbarState(0);
        assertThat(state.scrollPercentage()).isEqualTo(0.0);
    }
}
