/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.terminal;

import io.github.jratatui.buffer.Buffer;
import io.github.jratatui.layout.Rect;

/**
 * Represents a completed frame after rendering.
 */
public record CompletedFrame(Buffer buffer, Rect area) {
}
