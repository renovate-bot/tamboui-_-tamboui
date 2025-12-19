/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.style;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

class StyleTest {

    @Test
    @DisplayName("Style.EMPTY has no colors or modifiers")
    void emptyStyle() {
        assertThat(Style.EMPTY.fg()).isEmpty();
        assertThat(Style.EMPTY.bg()).isEmpty();
        assertThat(Style.EMPTY.addModifiers()).isEmpty();
    }

    @Test
    @DisplayName("Style fg sets foreground color")
    void fgColor() {
        var style = Style.EMPTY.fg(Color.RED);
        assertThat(style.fg()).contains(Color.RED);
        assertThat(style.bg()).isEmpty();
    }

    @Test
    @DisplayName("Style bg sets background color")
    void bgColor() {
        var style = Style.EMPTY.bg(Color.BLUE);
        assertThat(style.bg()).contains(Color.BLUE);
        assertThat(style.fg()).isEmpty();
    }

    @Test
    @DisplayName("Style bold adds BOLD modifier")
    void boldModifier() {
        var style = Style.EMPTY.bold();
        assertThat(style.addModifiers()).contains(Modifier.BOLD);
    }

    @Test
    @DisplayName("Style italic adds ITALIC modifier")
    void italicModifier() {
        var style = Style.EMPTY.italic();
        assertThat(style.addModifiers()).contains(Modifier.ITALIC);
    }

    @Test
    @DisplayName("Style underlined adds UNDERLINED modifier")
    void underlinedModifier() {
        var style = Style.EMPTY.underlined();
        assertThat(style.addModifiers()).contains(Modifier.UNDERLINED);
    }

    @Test
    @DisplayName("Style chaining")
    void chaining() {
        var style = Style.EMPTY
            .fg(Color.RED)
            .bg(Color.BLACK)
            .bold()
            .italic();

        assertThat(style.fg()).contains(Color.RED);
        assertThat(style.bg()).contains(Color.BLACK);
        assertThat(style.addModifiers()).contains(Modifier.BOLD, Modifier.ITALIC);
    }

    @Test
    @DisplayName("Style patch merges styles")
    void patch() {
        var base = Style.EMPTY.fg(Color.RED).bold();
        var patch = Style.EMPTY.bg(Color.BLUE).italic();

        var merged = base.patch(patch);

        assertThat(merged.fg()).contains(Color.RED);
        assertThat(merged.bg()).contains(Color.BLUE);
        assertThat(merged.addModifiers()).contains(Modifier.BOLD, Modifier.ITALIC);
    }

    @Test
    @DisplayName("Style patch overwrites colors")
    void patchOverwrites() {
        var base = Style.EMPTY.fg(Color.RED);
        var patch = Style.EMPTY.fg(Color.GREEN);

        var merged = base.patch(patch);

        assertThat(merged.fg()).contains(Color.GREEN);
    }
}
