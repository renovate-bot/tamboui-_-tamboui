/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.tui.event;

/**
 * Represents a keyboard input event.
 *
 * @param code      the key code (for special keys) or {@link KeyCode#CHAR} for printable characters
 * @param modifiers the modifier keys that were pressed
 * @param character the character for {@link KeyCode#CHAR} events, or '\0' otherwise
 */
public record KeyEvent(KeyCode code, KeyModifiers modifiers, char character) implements Event {

    /**
     * Creates a key event for a printable character.
     */
    public static KeyEvent ofChar(char c) {
        return new KeyEvent(KeyCode.CHAR, KeyModifiers.NONE, c);
    }

    /**
     * Creates a key event for a printable character with modifiers.
     */
    public static KeyEvent ofChar(char c, KeyModifiers modifiers) {
        return new KeyEvent(KeyCode.CHAR, modifiers, c);
    }

    /**
     * Creates a key event for a special key.
     */
    public static KeyEvent ofKey(KeyCode code) {
        return new KeyEvent(code, KeyModifiers.NONE, '\0');
    }

    /**
     * Creates a key event for a special key with modifiers.
     */
    public static KeyEvent ofKey(KeyCode code, KeyModifiers modifiers) {
        return new KeyEvent(code, modifiers, '\0');
    }

    /**
     * Returns true if this is a character event matching the given character.
     */
    public boolean isChar(char c) {
        return code == KeyCode.CHAR && character == c;
    }

    /**
     * Returns true if this is a key event matching the given key code.
     */
    public boolean isKey(KeyCode keyCode) {
        return code == keyCode;
    }

    /**
     * Returns true if Ctrl modifier was pressed.
     */
    public boolean hasCtrl() {
        return modifiers.ctrl();
    }

    /**
     * Returns true if Alt modifier was pressed.
     */
    public boolean hasAlt() {
        return modifiers.alt();
    }

    /**
     * Returns true if Shift modifier was pressed.
     */
    public boolean hasShift() {
        return modifiers.shift();
    }

    /**
     * Returns true if this is a Ctrl+C event (common quit signal).
     */
    public boolean isCtrlC() {
        return hasCtrl() && isChar('c');
    }
}
