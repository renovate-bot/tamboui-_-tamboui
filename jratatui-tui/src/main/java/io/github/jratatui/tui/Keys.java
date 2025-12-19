/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.tui;

import io.github.jratatui.tui.event.Event;
import io.github.jratatui.tui.event.KeyCode;
import io.github.jratatui.tui.event.KeyEvent;

/**
 * Utility class with common key binding helpers.
 * <p>
 * Provides convenient methods for checking common key patterns,
 * including vim-style navigation and standard actions.
 *
 * <pre>{@code
 * import static io.github.jratatui.tui.Keys.*;
 *
 * tui.run(
 *     (event, runner) -> {
 *         if (isQuit(event)) { runner.quit(); return false; }
 *         if (isUp(event)) { state.up(); return true; }
 *         if (isDown(event)) { state.down(); return true; }
 *         return false;
 *     },
 *     frame -> render(frame)
 * );
 * }</pre>
 */
public final class Keys {

    private Keys() {}

    // ========== Quit ==========

    /**
     * Returns true if this is a quit event (q, Q, or Ctrl+C).
     */
    public static boolean isQuit(Event event) {
        if (!(event instanceof KeyEvent k)) {
            return false;
        }
        return k.isChar('q') || k.isChar('Q') || k.isCtrlC();
    }

    // ========== Navigation (Vim + Arrows) ==========

    /**
     * Returns true if this is an up navigation event (Up arrow or k/K).
     */
    public static boolean isUp(Event event) {
        if (!(event instanceof KeyEvent k)) {
            return false;
        }
        return k.code() == KeyCode.UP || k.isChar('k') || k.isChar('K');
    }

    /**
     * Returns true if this is a down navigation event (Down arrow or j/J).
     */
    public static boolean isDown(Event event) {
        if (!(event instanceof KeyEvent k)) {
            return false;
        }
        return k.code() == KeyCode.DOWN || k.isChar('j') || k.isChar('J');
    }

    /**
     * Returns true if this is a left navigation event (Left arrow or h/H).
     */
    public static boolean isLeft(Event event) {
        if (!(event instanceof KeyEvent k)) {
            return false;
        }
        return k.code() == KeyCode.LEFT || k.isChar('h') || k.isChar('H');
    }

    /**
     * Returns true if this is a right navigation event (Right arrow or l/L).
     */
    public static boolean isRight(Event event) {
        if (!(event instanceof KeyEvent k)) {
            return false;
        }
        return k.code() == KeyCode.RIGHT || k.isChar('l') || k.isChar('L');
    }

    // ========== Arrow Keys Only ==========

    /**
     * Returns true if this is an Up arrow key event.
     */
    public static boolean isArrowUp(Event event) {
        return event instanceof KeyEvent k && k.code() == KeyCode.UP;
    }

    /**
     * Returns true if this is a Down arrow key event.
     */
    public static boolean isArrowDown(Event event) {
        return event instanceof KeyEvent k && k.code() == KeyCode.DOWN;
    }

    /**
     * Returns true if this is a Left arrow key event.
     */
    public static boolean isArrowLeft(Event event) {
        return event instanceof KeyEvent k && k.code() == KeyCode.LEFT;
    }

    /**
     * Returns true if this is a Right arrow key event.
     */
    public static boolean isArrowRight(Event event) {
        return event instanceof KeyEvent k && k.code() == KeyCode.RIGHT;
    }

    // ========== Page Navigation ==========

    /**
     * Returns true if this is a page up event (PageUp or Ctrl+U).
     */
    public static boolean isPageUp(Event event) {
        if (!(event instanceof KeyEvent k)) {
            return false;
        }
        return k.code() == KeyCode.PAGE_UP ||
               (k.hasCtrl() && k.isChar('u'));
    }

    /**
     * Returns true if this is a page down event (PageDown or Ctrl+D).
     */
    public static boolean isPageDown(Event event) {
        if (!(event instanceof KeyEvent k)) {
            return false;
        }
        return k.code() == KeyCode.PAGE_DOWN ||
               (k.hasCtrl() && k.isChar('d'));
    }

    /**
     * Returns true if this is a home event (Home or g or Ctrl+Home).
     */
    public static boolean isHome(Event event) {
        if (!(event instanceof KeyEvent k)) {
            return false;
        }
        return k.code() == KeyCode.HOME || k.isChar('g');
    }

    /**
     * Returns true if this is an end event (End or G or Ctrl+End).
     */
    public static boolean isEnd(Event event) {
        if (!(event instanceof KeyEvent k)) {
            return false;
        }
        return k.code() == KeyCode.END || k.isChar('G');
    }

    // ========== Selection / Action ==========

    /**
     * Returns true if this is a select/confirm event (Enter or Space).
     */
    public static boolean isSelect(Event event) {
        if (!(event instanceof KeyEvent k)) {
            return false;
        }
        return k.code() == KeyCode.ENTER || k.isChar(' ');
    }

    /**
     * Returns true if this is an Enter key event.
     */
    public static boolean isEnter(Event event) {
        return event instanceof KeyEvent k && k.code() == KeyCode.ENTER;
    }

    /**
     * Returns true if this is an Escape key event.
     */
    public static boolean isEscape(Event event) {
        return event instanceof KeyEvent k && k.code() == KeyCode.ESCAPE;
    }

    /**
     * Returns true if this is a Tab key event.
     */
    public static boolean isTab(Event event) {
        return event instanceof KeyEvent k && k.code() == KeyCode.TAB;
    }

    /**
     * Returns true if this is a Shift+Tab (back tab) event.
     */
    public static boolean isBackTab(Event event) {
        return event instanceof KeyEvent k &&
               k.code() == KeyCode.TAB && k.hasShift();
    }

    /**
     * Returns true if this is a Backspace key event.
     */
    public static boolean isBackspace(Event event) {
        return event instanceof KeyEvent k && k.code() == KeyCode.BACKSPACE;
    }

    /**
     * Returns true if this is a Delete key event.
     */
    public static boolean isDelete(Event event) {
        return event instanceof KeyEvent k && k.code() == KeyCode.DELETE;
    }

    // ========== Character Matching ==========

    /**
     * Returns true if this is a key event for the given character.
     */
    public static boolean isChar(Event event, char c) {
        return event instanceof KeyEvent k && k.isChar(c);
    }

    /**
     * Returns true if this is a key event for any of the given characters.
     */
    public static boolean isAnyChar(Event event, char... chars) {
        if (!(event instanceof KeyEvent k) || k.code() != KeyCode.CHAR) {
            return false;
        }
        for (char c : chars) {
            if (k.character() == c) {
                return true;
            }
        }
        return false;
    }

    // ========== Function Keys ==========

    /**
     * Returns true if this is a function key event (F1-F12).
     */
    public static boolean isFunctionKey(Event event) {
        if (!(event instanceof KeyEvent k)) {
            return false;
        }
        return switch (k.code()) {
            case F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12 -> true;
            default -> false;
        };
    }

    /**
     * Returns the function key number (1-12) if this is a function key, or -1 otherwise.
     */
    public static int functionKeyNumber(Event event) {
        if (!(event instanceof KeyEvent k)) {
            return -1;
        }
        return switch (k.code()) {
            case F1 -> 1;
            case F2 -> 2;
            case F3 -> 3;
            case F4 -> 4;
            case F5 -> 5;
            case F6 -> 6;
            case F7 -> 7;
            case F8 -> 8;
            case F9 -> 9;
            case F10 -> 10;
            case F11 -> 11;
            case F12 -> 12;
            default -> -1;
        };
    }
}
