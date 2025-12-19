/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.tui.event;

import java.time.Duration;

/**
 * Represents an animation timer tick event.
 * <p>
 * Tick events are generated at regular intervals when animation mode is enabled
 * via {@link io.github.jratatui.tui.TuiConfig#tickRate()}.
 *
 * @param frameCount the number of frames since the application started
 * @param elapsed    the time elapsed since the last tick
 */
public record TickEvent(long frameCount, Duration elapsed) implements Event {

    /**
     * Creates a tick event with the given frame count and elapsed time.
     */
    public static TickEvent of(long frameCount, Duration elapsed) {
        return new TickEvent(frameCount, elapsed);
    }

    /**
     * Returns the elapsed time in milliseconds.
     */
    public long elapsedMillis() {
        return elapsed.toMillis();
    }

    /**
     * Returns the elapsed time in seconds as a double.
     */
    public double elapsedSeconds() {
        return elapsed.toNanos() / 1_000_000_000.0;
    }
}
