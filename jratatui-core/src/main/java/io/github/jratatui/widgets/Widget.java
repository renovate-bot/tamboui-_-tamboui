/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.widgets;

import io.github.jratatui.buffer.Buffer;
import io.github.jratatui.layout.Rect;

/**
 * A widget that can render itself to a buffer.
 */
@FunctionalInterface
public interface Widget {

    /**
     * Renders this widget to the given buffer area.
     */
    void render(Rect area, Buffer buffer);
}
