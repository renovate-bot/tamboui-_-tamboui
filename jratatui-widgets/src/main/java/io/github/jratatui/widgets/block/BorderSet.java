/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.widgets.block;

/**
 * Characters used to draw a border.
 */
public record BorderSet(
    String horizontal,
    String vertical,
    String topLeft,
    String topRight,
    String bottomLeft,
    String bottomRight
) {
}
