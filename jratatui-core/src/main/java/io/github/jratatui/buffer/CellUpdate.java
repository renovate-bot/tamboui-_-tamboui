/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.buffer;

/**
 * Represents a cell update to be sent to the terminal backend.
 */
public record CellUpdate(int x, int y, Cell cell) {
}
