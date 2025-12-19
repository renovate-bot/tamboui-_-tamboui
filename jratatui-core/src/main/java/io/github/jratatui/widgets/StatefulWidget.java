/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.widgets;

import io.github.jratatui.buffer.Buffer;
import io.github.jratatui.layout.Rect;

/**
 * A widget that maintains state between renders.
 *
 * @param <S> the state type
 */
public interface StatefulWidget<S> {

    /**
     * Renders this widget to the given buffer area with the provided state.
     */
    void render(Rect area, Buffer buffer, S state);
}
