/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.layout;

/**
 * A size representing width and height dimensions.
 */
public record Size(int width, int height) {

    public static final Size ZERO = new Size(0, 0);

    public int area() {
        return width * height;
    }

    public boolean isEmpty() {
        return width == 0 || height == 0;
    }
}
