/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.style;

/**
 * Terminal colors supporting ANSI 16, 256-color indexed, and RGB true color modes.
 */
public sealed interface Color permits
    Color.Reset,
    Color.Ansi,
    Color.Indexed,
    Color.Rgb {

    /**
     * Reset to default terminal color.
     */
    record Reset() implements Color {}

    /**
     * Standard ANSI 16 colors.
     */
    record Ansi(AnsiColor color) implements Color {}

    /**
     * 256-color palette index (0-255).
     */
    record Indexed(int index) implements Color {
        public Indexed {
            if (index < 0 || index > 255) {
                throw new IllegalArgumentException("Color index must be 0-255: " + index);
            }
        }
    }

    /**
     * RGB true color (24-bit).
     */
    record Rgb(int r, int g, int b) implements Color {
        public Rgb {
            if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
                throw new IllegalArgumentException(
                    String.format("RGB values must be 0-255: (%d, %d, %d)", r, g, b));
            }
        }

        public static Rgb fromHex(String hex) {
            String h = hex.startsWith("#") ? hex.substring(1) : hex;
            if (h.length() != 6) {
                throw new IllegalArgumentException("Hex color must be 6 characters: " + hex);
            }
            int r = Integer.parseInt(h.substring(0, 2), 16);
            int g = Integer.parseInt(h.substring(2, 4), 16);
            int b = Integer.parseInt(h.substring(4, 6), 16);
            return new Rgb(r, g, b);
        }
    }

    // Singleton for reset
    Color RESET = new Reset();

    // Standard ANSI colors
    Color BLACK = new Ansi(AnsiColor.BLACK);
    Color RED = new Ansi(AnsiColor.RED);
    Color GREEN = new Ansi(AnsiColor.GREEN);
    Color YELLOW = new Ansi(AnsiColor.YELLOW);
    Color BLUE = new Ansi(AnsiColor.BLUE);
    Color MAGENTA = new Ansi(AnsiColor.MAGENTA);
    Color CYAN = new Ansi(AnsiColor.CYAN);
    Color WHITE = new Ansi(AnsiColor.WHITE);
    Color GRAY = new Ansi(AnsiColor.BRIGHT_BLACK);

    // Bright ANSI colors
    Color DARK_GRAY = new Ansi(AnsiColor.BRIGHT_BLACK);
    Color LIGHT_RED = new Ansi(AnsiColor.BRIGHT_RED);
    Color LIGHT_GREEN = new Ansi(AnsiColor.BRIGHT_GREEN);
    Color LIGHT_YELLOW = new Ansi(AnsiColor.BRIGHT_YELLOW);
    Color LIGHT_BLUE = new Ansi(AnsiColor.BRIGHT_BLUE);
    Color LIGHT_MAGENTA = new Ansi(AnsiColor.BRIGHT_MAGENTA);
    Color LIGHT_CYAN = new Ansi(AnsiColor.BRIGHT_CYAN);
    Color BRIGHT_WHITE = new Ansi(AnsiColor.BRIGHT_WHITE);

    // Factory methods
    static Color ansi(AnsiColor color) {
        return new Ansi(color);
    }

    static Color indexed(int index) {
        return new Indexed(index);
    }

    static Color rgb(int r, int g, int b) {
        return new Rgb(r, g, b);
    }

    static Color hex(String hex) {
        return Rgb.fromHex(hex);
    }
}
