/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.tui.event;

/**
 * Represents a terminal window resize event.
 * <p>
 * This event is triggered when the terminal window size changes,
 * typically due to the user resizing the terminal window.
 *
 * @param width  the new terminal width in columns
 * @param height the new terminal height in rows
 */
public record ResizeEvent(int width, int height) implements Event {

    /**
     * Creates a resize event with the given dimensions.
     */
    public static ResizeEvent of(int width, int height) {
        return new ResizeEvent(width, height);
    }
}
