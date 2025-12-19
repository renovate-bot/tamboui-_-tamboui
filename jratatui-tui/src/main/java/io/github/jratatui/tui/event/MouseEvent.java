/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.tui.event;

/**
 * Represents a mouse input event.
 *
 * @param kind      the type of mouse event
 * @param button    the mouse button involved
 * @param x         the column position (0-indexed)
 * @param y         the row position (0-indexed)
 * @param modifiers the modifier keys that were pressed
 */
public record MouseEvent(
    MouseEventKind kind,
    MouseButton button,
    int x,
    int y,
    KeyModifiers modifiers
) implements Event {

    /**
     * Creates a mouse press event.
     */
    public static MouseEvent press(MouseButton button, int x, int y) {
        return new MouseEvent(MouseEventKind.PRESS, button, x, y, KeyModifiers.NONE);
    }

    /**
     * Creates a mouse release event.
     */
    public static MouseEvent release(MouseButton button, int x, int y) {
        return new MouseEvent(MouseEventKind.RELEASE, button, x, y, KeyModifiers.NONE);
    }

    /**
     * Creates a mouse move event.
     */
    public static MouseEvent move(int x, int y) {
        return new MouseEvent(MouseEventKind.MOVE, MouseButton.NONE, x, y, KeyModifiers.NONE);
    }

    /**
     * Creates a mouse drag event.
     */
    public static MouseEvent drag(MouseButton button, int x, int y) {
        return new MouseEvent(MouseEventKind.DRAG, button, x, y, KeyModifiers.NONE);
    }

    /**
     * Creates a scroll up event.
     */
    public static MouseEvent scrollUp(int x, int y) {
        return new MouseEvent(MouseEventKind.SCROLL_UP, MouseButton.NONE, x, y, KeyModifiers.NONE);
    }

    /**
     * Creates a scroll down event.
     */
    public static MouseEvent scrollDown(int x, int y) {
        return new MouseEvent(MouseEventKind.SCROLL_DOWN, MouseButton.NONE, x, y, KeyModifiers.NONE);
    }

    /**
     * Returns true if this is a press event.
     */
    public boolean isPress() {
        return kind == MouseEventKind.PRESS;
    }

    /**
     * Returns true if this is a release event.
     */
    public boolean isRelease() {
        return kind == MouseEventKind.RELEASE;
    }

    /**
     * Returns true if this is a left button event.
     */
    public boolean isLeftButton() {
        return button == MouseButton.LEFT;
    }

    /**
     * Returns true if this is a right button event.
     */
    public boolean isRightButton() {
        return button == MouseButton.RIGHT;
    }

    /**
     * Returns true if this is a scroll event.
     */
    public boolean isScroll() {
        return kind == MouseEventKind.SCROLL_UP || kind == MouseEventKind.SCROLL_DOWN;
    }
}
