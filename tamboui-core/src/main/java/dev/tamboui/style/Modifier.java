/*
 * Copyright TamboUI Contributors
 * SPDX-License-Identifier: MIT
 */
package dev.tamboui.style;

/**
 * Text style modifiers (SGR attributes).
 */
public enum Modifier {
    NORMAL(0),
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
     * Returns the implicit style name for this modifier.
     * <p>
     * Maps each modifier to the conventional style name used
     * for style resolution (e.g., BOLD → "bold", CROSSED_OUT → "strikethrough").
     *
     * @return the style name
     */
    public String implicitStyleName() {
        return name().toLowerCase().replace('_', '-');
    }

}
