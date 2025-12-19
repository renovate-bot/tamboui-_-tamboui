/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.layout;

/**
 * Margin values for top, right, bottom, and left sides.
 */
public record Margin(int top, int right, int bottom, int left) {

    public static final Margin NONE = new Margin(0, 0, 0, 0);

    public static Margin uniform(int value) {
        return new Margin(value, value, value, value);
    }

    public static Margin symmetric(int vertical, int horizontal) {
        return new Margin(vertical, horizontal, vertical, horizontal);
    }

    public static Margin horizontal(int value) {
        return new Margin(0, value, 0, value);
    }

    public static Margin vertical(int value) {
        return new Margin(value, 0, value, 0);
    }

    public int horizontalTotal() {
        return left + right;
    }

    public int verticalTotal() {
        return top + bottom;
    }
}
