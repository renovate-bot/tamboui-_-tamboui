/*
 * Copyright (c) 2025 TamboUI Contributors
 * SPDX-License-Identifier: MIT
 */
package dev.tamboui.css.property;

import dev.tamboui.style.Color;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ColorConverterTest {

    private final ColorConverter converter = new ColorConverter();

    @Test
    void convertsNamedColors() {
        assertThat(converter.convert("red", Collections.<String, String>emptyMap()))
                .hasValue(Color.RED);
        assertThat(converter.convert("blue", Collections.<String, String>emptyMap()))
                .hasValue(Color.BLUE);
        assertThat(converter.convert("green", Collections.<String, String>emptyMap()))
                .hasValue(Color.GREEN);
        assertThat(converter.convert("black", Collections.<String, String>emptyMap()))
                .hasValue(Color.BLACK);
        assertThat(converter.convert("white", Collections.<String, String>emptyMap()))
                .hasValue(Color.WHITE);
    }

    @Test
    void convertsNamedColorsCaseInsensitive() {
        assertThat(converter.convert("RED", Collections.<String, String>emptyMap()))
                .hasValue(Color.RED);
        assertThat(converter.convert("Red", Collections.<String, String>emptyMap()))
                .hasValue(Color.RED);
    }

    @Test
    void convertsHexColor6Digits() {
        Optional<Color> color = converter.convert("#ff0000", Collections.<String, String>emptyMap());

        assertThat(color).isPresent();
        assertThat(color.get()).isInstanceOf(Color.Rgb.class);
        Color.Rgb rgb = (Color.Rgb) color.get();
        assertThat(rgb.r()).isEqualTo(255);
        assertThat(rgb.g()).isEqualTo(0);
        assertThat(rgb.b()).isEqualTo(0);
    }

    @Test
    void convertsHexColor3Digits() {
        Optional<Color> color = converter.convert("#f00", Collections.<String, String>emptyMap());

        assertThat(color).isPresent();
        assertThat(color.get()).isInstanceOf(Color.Rgb.class);
        Color.Rgb rgb = (Color.Rgb) color.get();
        assertThat(rgb.r()).isEqualTo(255);
        assertThat(rgb.g()).isEqualTo(0);
        assertThat(rgb.b()).isEqualTo(0);
    }

    @Test
    void convertsRgbFunction() {
        Optional<Color> color = converter.convert("rgb(100, 150, 200)", Collections.<String, String>emptyMap());

        assertThat(color).isPresent();
        assertThat(color.get()).isInstanceOf(Color.Rgb.class);
        Color.Rgb rgb = (Color.Rgb) color.get();
        assertThat(rgb.r()).isEqualTo(100);
        assertThat(rgb.g()).isEqualTo(150);
        assertThat(rgb.b()).isEqualTo(200);
    }

    @Test
    void convertsIndexedColor() {
        Optional<Color> color = converter.convert("indexed(42)", Collections.<String, String>emptyMap());

        assertThat(color).isPresent();
        assertThat(color.get()).isInstanceOf(Color.Indexed.class);
        assertThat(((Color.Indexed) color.get()).index()).isEqualTo(42);
    }

    @Test
    void resolvesVariableReference() {
        Map<String, String> variables = new HashMap<>();
        variables.put("primary", "blue");

        Optional<Color> color = converter.convert("$primary", variables);

        assertThat(color).hasValue(Color.BLUE);
    }

    @Test
    void returnsEmptyForInvalidColor() {
        assertThat(converter.convert("invalid", Collections.<String, String>emptyMap())).isEmpty();
        assertThat(converter.convert("", Collections.<String, String>emptyMap())).isEmpty();
        assertThat(converter.convert(null, Collections.<String, String>emptyMap())).isEmpty();
    }

    @Test
    void convertsBrightColors() {
        assertThat(converter.convert("light-red", Collections.<String, String>emptyMap()))
                .hasValue(Color.LIGHT_RED);
        assertThat(converter.convert("bright-blue", Collections.<String, String>emptyMap()))
                .hasValue(Color.LIGHT_BLUE);
    }
}
