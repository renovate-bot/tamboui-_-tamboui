/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.style;

/**
 * Text style modifiers (SGR attributes).
 */
public enum Modifier {
    BOLD(1),
    DIM(2),
    ITALIC(3),
    UNDERLINED(4),
    SLOW_BLINK(5),
    RAPID_BLINK(6),
    REVERSED(7),
    HIDDEN(8),
    CROSSED_OUT(9);

    private final int code;

    Modifier(int code) {
        this.code = code;
    }

    /**
     * Returns the ANSI SGR code to enable this modifier.
     */
    public int code() {
        return code;
    }

    /**
     * Returns the ANSI SGR code to disable this modifier.
     */
    public int resetCode() {
        return switch (this) {
            case BOLD, DIM -> 22;  // Both reset by 22
            case ITALIC -> 23;
            case UNDERLINED -> 24;
            case SLOW_BLINK, RAPID_BLINK -> 25;
            case REVERSED -> 27;
            case HIDDEN -> 28;
            case CROSSED_OUT -> 29;
        };
    }
}
