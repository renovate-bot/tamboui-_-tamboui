/*
 * Copyright TamboUI Contributors
 * SPDX-License-Identifier: MIT
 */
package dev.tamboui.style;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

class ColorTest {

    @Test
    @DisplayName("Color constants are Named colors with Ansi defaults")
    void colorConstants() {
        assertThat(Color.RED).isInstanceOf(Color.Named.class);
        assertThat(Color.GREEN).isInstanceOf(Color.Named.class);
        assertThat(Color.BLUE).isInstanceOf(Color.Named.class);
        assertThat(Color.BLACK).isInstanceOf(Color.Named.class);
        assertThat(Color.WHITE).isInstanceOf(Color.Named.class);

        // Verify named colors have correct CSS class names
        assertThat(((Color.Named) Color.RED).name()).isEqualTo("red");
        assertThat(((Color.Named) Color.GREEN).name()).isEqualTo("green");
        assertThat(((Color.Named) Color.BLUE).name()).isEqualTo("blue");

        // Verify named colors have Ansi defaults
        assertThat(((Color.Named) Color.RED).defaultValue()).isInstanceOf(Color.Ansi.class);
        assertThat(((Color.Named) Color.GREEN).defaultValue()).isInstanceOf(Color.Ansi.class);
    }

    @Test
    @DisplayName("Color.Ansi wraps AnsiColor")
    void ansiColor() {
        Color.Ansi color = new Color.Ansi(AnsiColor.CYAN);
        assertThat(color.color()).isEqualTo(AnsiColor.CYAN);
    }

    @Test
    @DisplayName("Color.Rgb holds RGB values")
    void rgbColor() {
        Color.Rgb color = new Color.Rgb(255, 128, 64);
        assertThat(color.r()).isEqualTo(255);
        assertThat(color.g()).isEqualTo(128);
        assertThat(color.b()).isEqualTo(64);
    }

    @Test
    @DisplayName("Color.Indexed holds palette index")
    void indexedColor() {
        Color.Indexed color = new Color.Indexed(42);
        assertThat(color.index()).isEqualTo(42);
    }

    @Test
    @DisplayName("Color.Reset is a singleton-like type")
    void resetColor() {
        Color.Reset reset1 = new Color.Reset();
        Color.Reset reset2 = new Color.Reset();
        assertThat(reset1).isEqualTo(reset2);
    }

    @Test
    @DisplayName("Color.rgb factory method")
    void rgbFactory() {
        Color color = Color.rgb(100, 150, 200);
        assertThat(color).isInstanceOf(Color.Rgb.class);
        assertThat(((Color.Rgb) color).r()).isEqualTo(100);
    }

    @Test
    @DisplayName("Color.indexed factory method")
    void indexedFactory() {
        Color color = Color.indexed(128);
        assertThat(color).isInstanceOf(Color.Indexed.class);
        assertThat(((Color.Indexed) color).index()).isEqualTo(128);
    }

    @Test
    @DisplayName("Named color converts to RGB via its default value")
    void namedColorToRgb() {
        Color.Named red = (Color.Named) Color.RED;
        Color.Rgb rgb = red.toRgb();
        // RED's default is Ansi RED which converts to (170, 0, 0)
        assertThat(rgb.r()).isEqualTo(170);
        assertThat(rgb.g()).isEqualTo(0);
        assertThat(rgb.b()).isEqualTo(0);
    }
}
