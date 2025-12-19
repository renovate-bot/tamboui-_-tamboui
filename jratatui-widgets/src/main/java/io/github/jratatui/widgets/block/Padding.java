/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.widgets.block;

/**
 * Padding inside a block.
 */
public record Padding(int top, int right, int bottom, int left) {

    public static final Padding NONE = new Padding(0, 0, 0, 0);

    public static Padding uniform(int value) {
        return new Padding(value, value, value, value);
    }

    public static Padding symmetric(int vertical, int horizontal) {
        return new Padding(vertical, horizontal, vertical, horizontal);
    }

    public static Padding horizontal(int value) {
        return new Padding(0, value, 0, value);
    }

    public static Padding vertical(int value) {
        return new Padding(value, 0, value, 0);
    }

    public int horizontalTotal() {
        return left + right;
    }

    public int verticalTotal() {
        return top + bottom;
    }
}
