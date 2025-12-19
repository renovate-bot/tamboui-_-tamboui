/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.widgets.paragraph;

/**
 * Text wrapping mode for paragraphs.
 */
public enum Wrap {
    /**
     * No wrapping - text extends beyond bounds.
     */
    NONE,

    /**
     * Wrap at word boundaries.
     */
    WORD,

    /**
     * Wrap at character boundaries.
     */
    CHARACTER
}
