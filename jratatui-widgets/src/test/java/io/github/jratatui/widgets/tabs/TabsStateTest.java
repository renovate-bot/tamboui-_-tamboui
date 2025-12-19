/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.widgets.tabs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

class TabsStateTest {

    @Test
    @DisplayName("Initial state has no selection")
    void initialState() {
        var state = new TabsState();
        assertThat(state.selected()).isNull();
    }

    @Test
    @DisplayName("Constructor with index sets selection")
    void constructorWithIndex() {
        var state = new TabsState(2);
        assertThat(state.selected()).isEqualTo(2);
    }

    @Test
    @DisplayName("Constructor clamps negative index")
    void constructorClampsNegative() {
        var state = new TabsState(-5);
        assertThat(state.selected()).isEqualTo(0);
    }

    @Test
    @DisplayName("select sets the selected tab")
    void select() {
        var state = new TabsState();
        state.select(3);
        assertThat(state.selected()).isEqualTo(3);
    }

    @Test
    @DisplayName("select clamps to zero")
    void selectClampsToZero() {
        var state = new TabsState();
        state.select(-10);
        assertThat(state.selected()).isEqualTo(0);
    }

    @Test
    @DisplayName("clearSelection removes selection")
    void clearSelection() {
        var state = new TabsState(2);
        state.clearSelection();
        assertThat(state.selected()).isNull();
    }

    @Test
    @DisplayName("selectFirst selects first tab")
    void selectFirst() {
        var state = new TabsState();
        state.selectFirst();
        assertThat(state.selected()).isEqualTo(0);
    }

    @Test
    @DisplayName("selectLast selects last tab")
    void selectLast() {
        var state = new TabsState();
        state.selectLast(5);
        assertThat(state.selected()).isEqualTo(4);
    }

    @Test
    @DisplayName("selectLast does nothing with zero tabs")
    void selectLastEmpty() {
        var state = new TabsState();
        state.selectLast(0);
        assertThat(state.selected()).isNull();
    }

    @Test
    @DisplayName("selectNext moves to next tab")
    void selectNext() {
        var state = new TabsState(1);
        state.selectNext(5);
        assertThat(state.selected()).isEqualTo(2);
    }

    @Test
    @DisplayName("selectNext wraps to first at end")
    void selectNextWraps() {
        var state = new TabsState(4);
        state.selectNext(5);
        assertThat(state.selected()).isEqualTo(0);
    }

    @Test
    @DisplayName("selectNext selects first when nothing selected")
    void selectNextFromNull() {
        var state = new TabsState();
        state.selectNext(5);
        assertThat(state.selected()).isEqualTo(0);
    }

    @Test
    @DisplayName("selectNext does nothing with zero tabs")
    void selectNextEmpty() {
        var state = new TabsState();
        state.selectNext(0);
        assertThat(state.selected()).isNull();
    }

    @Test
    @DisplayName("selectPrevious moves to previous tab")
    void selectPrevious() {
        var state = new TabsState(3);
        state.selectPrevious(5);
        assertThat(state.selected()).isEqualTo(2);
    }

    @Test
    @DisplayName("selectPrevious wraps to last at beginning")
    void selectPreviousWraps() {
        var state = new TabsState(0);
        state.selectPrevious(5);
        assertThat(state.selected()).isEqualTo(4);
    }

    @Test
    @DisplayName("selectPrevious selects last when nothing selected")
    void selectPreviousFromNull() {
        var state = new TabsState();
        state.selectPrevious(5);
        assertThat(state.selected()).isEqualTo(4);
    }

    @Test
    @DisplayName("selectPrevious does nothing with zero tabs")
    void selectPreviousEmpty() {
        var state = new TabsState();
        state.selectPrevious(0);
        assertThat(state.selected()).isNull();
    }
}
