/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.layout;

/**
 * A position in 2D space, representing x and y coordinates.
 */
public record Position(int x, int y) {

    public static final Position ORIGIN = new Position(0, 0);

    public Position offset(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }
}
