/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.tui.event;

/**
 * Represents keyboard modifier keys (Ctrl, Alt, Shift).
 *
 * @param ctrl  true if Ctrl was pressed
 * @param alt   true if Alt was pressed
 * @param shift true if Shift was pressed
 */
public record KeyModifiers(boolean ctrl, boolean alt, boolean shift) {

    /** No modifiers pressed. */
    public static final KeyModifiers NONE = new KeyModifiers(false, false, false);

    /** Only Ctrl pressed. */
    public static final KeyModifiers CTRL = new KeyModifiers(true, false, false);

    /** Only Alt pressed. */
    public static final KeyModifiers ALT = new KeyModifiers(false, true, false);

    /** Only Shift pressed. */
    public static final KeyModifiers SHIFT = new KeyModifiers(false, false, true);

    /**
     * Creates modifiers with the specified flags.
     */
    public static KeyModifiers of(boolean ctrl, boolean alt, boolean shift) {
        if (!ctrl && !alt && !shift) {
            return NONE;
        }
        return new KeyModifiers(ctrl, alt, shift);
    }

    /**
     * Returns true if no modifiers are pressed.
     */
    public boolean isEmpty() {
        return !ctrl && !alt && !shift;
    }

    /**
     * Returns true if any modifier is pressed.
     */
    public boolean hasAny() {
        return ctrl || alt || shift;
    }
}
