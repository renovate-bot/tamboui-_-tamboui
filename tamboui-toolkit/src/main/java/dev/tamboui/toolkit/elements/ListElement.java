/*
 * Copyright (c) 2025 TamboUI Contributors
 * SPDX-License-Identifier: MIT
 */
package dev.tamboui.toolkit.elements;

import dev.tamboui.toolkit.element.RenderContext;
import dev.tamboui.toolkit.element.StyledElement;
import dev.tamboui.layout.Rect;
import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.terminal.Frame;
import dev.tamboui.widgets.block.Block;
import dev.tamboui.widgets.block.BorderType;
import dev.tamboui.widgets.block.Borders;
import dev.tamboui.widgets.block.Title;
import dev.tamboui.widgets.list.ListItem;
import dev.tamboui.widgets.list.ListState;
import dev.tamboui.widgets.list.ListWidget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A DSL wrapper for the ListWidget.
 * <p>
 * Displays a selectable list of items.
 * <pre>{@code
 * list("Item 1", "Item 2", "Item 3")
 *     .state(listState)
 *     .highlightColor(Color.YELLOW)
 *     .title("My List")
 *     .rounded()
 * }</pre>
 */
public final class ListElement extends StyledElement<ListElement> {

    private final List<ListItem> items = new ArrayList<>();
    private ListState state;
    private Style highlightStyle = Style.EMPTY.reversed();
    private String highlightSymbol = ">> ";
    private String title;
    private BorderType borderType;
    private Color borderColor;

    public ListElement() {
    }

    public ListElement(String... items) {
        for (String item : items) {
            this.items.add(ListItem.from(item));
        }
    }

    public ListElement(List<String> items) {
        for (String item : items) {
            this.items.add(ListItem.from(item));
        }
    }

    /**
     * Sets the list items from strings.
     */
    public ListElement items(String... items) {
        this.items.clear();
        for (String item : items) {
            this.items.add(ListItem.from(item));
        }
        return this;
    }

    /**
     * Sets the list items from a collection.
     */
    public ListElement items(List<String> items) {
        this.items.clear();
        for (String item : items) {
            this.items.add(ListItem.from(item));
        }
        return this;
    }

    /**
     * Sets the list items from ListItem objects.
     */
    public ListElement listItems(ListItem... items) {
        this.items.clear();
        this.items.addAll(Arrays.asList(items));
        return this;
    }

    /**
     * Adds an item to the list.
     */
    public ListElement add(String item) {
        this.items.add(ListItem.from(item));
        return this;
    }

    /**
     * Adds an item to the list.
     */
    public ListElement add(ListItem item) {
        this.items.add(item);
        return this;
    }

    /**
     * Sets the list state for selection tracking.
     */
    public ListElement state(ListState state) {
        this.state = state;
        return this;
    }

    /**
     * Sets the highlight style for selected items.
     */
    public ListElement highlightStyle(Style style) {
        this.highlightStyle = style;
        return this;
    }

    /**
     * Sets the highlight color for selected items.
     */
    public ListElement highlightColor(Color color) {
        this.highlightStyle = Style.EMPTY.fg(color).bold();
        return this;
    }

    /**
     * Sets the symbol displayed before the selected item.
     */
    public ListElement highlightSymbol(String symbol) {
        this.highlightSymbol = symbol;
        return this;
    }

    /**
     * Sets the title.
     */
    public ListElement title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Uses rounded borders.
     */
    public ListElement rounded() {
        this.borderType = BorderType.ROUNDED;
        return this;
    }

    /**
     * Sets the border color.
     */
    public ListElement borderColor(Color color) {
        this.borderColor = color;
        return this;
    }

    @Override
    public void render(Frame frame, Rect area, RenderContext context) {
        if (area.isEmpty()) {
            return;
        }

        ListWidget.Builder builder = ListWidget.builder()
            .items(items)
            .style(style)
            .highlightStyle(highlightStyle)
            .highlightSymbol(highlightSymbol);

        if (title != null || borderType != null) {
            Block.Builder blockBuilder = Block.builder().borders(Borders.ALL);
            if (title != null) {
                blockBuilder.title(Title.from(title));
            }
            if (borderType != null) {
                blockBuilder.borderType(borderType);
            }
            if (borderColor != null) {
                blockBuilder.borderStyle(Style.EMPTY.fg(borderColor));
            }
            builder.block(blockBuilder.build());
        }

        ListWidget widget = builder.build();
        ListState effectiveState = state != null ? state : new ListState();
        frame.renderStatefulWidget(widget, area, effectiveState);
    }
}
