/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A layout defines how to split a rectangular area into smaller areas
 * based on constraints.
 */
public final class Layout {

    private final Direction direction;
    private final List<Constraint> constraints;
    private final Margin margin;
    private final int spacing;
    private final Flex flex;

    private Layout(Direction direction, List<Constraint> constraints,
                   Margin margin, int spacing, Flex flex) {
        this.direction = direction;
        this.constraints = List.copyOf(constraints);
        this.margin = margin;
        this.spacing = spacing;
        this.flex = flex;
    }

    public static Layout vertical() {
        return new Layout(Direction.VERTICAL, List.of(), Margin.NONE, 0, Flex.START);
    }

    public static Layout horizontal() {
        return new Layout(Direction.HORIZONTAL, List.of(), Margin.NONE, 0, Flex.START);
    }

    public Layout constraints(Constraint... constraints) {
        return new Layout(direction, Arrays.asList(constraints), margin, spacing, flex);
    }

    public Layout constraints(List<Constraint> constraints) {
        return new Layout(direction, constraints, margin, spacing, flex);
    }

    public Layout margin(Margin margin) {
        return new Layout(direction, constraints, margin, spacing, flex);
    }

    public Layout margin(int value) {
        return new Layout(direction, constraints, Margin.uniform(value), spacing, flex);
    }

    public Layout spacing(int spacing) {
        return new Layout(direction, constraints, margin, spacing, flex);
    }

    public Layout flex(Flex flex) {
        return new Layout(direction, constraints, margin, spacing, flex);
    }

    public Direction direction() {
        return direction;
    }

    public List<Constraint> constraints() {
        return constraints;
    }

    public Margin margin() {
        return margin;
    }

    public int spacing() {
        return spacing;
    }

    public Flex flex() {
        return flex;
    }

    /**
     * Split the given area according to this layout's constraints.
     */
    public List<Rect> split(Rect area) {
        if (constraints.isEmpty()) {
            return List.of();
        }

        // Apply margin first
        Rect inner = area.inner(margin);

        int available = direction == Direction.HORIZONTAL ? inner.width() : inner.height();
        int totalSpacing = spacing * (constraints.size() - 1);
        int distributable = Math.max(0, available - totalSpacing);

        int[] sizes = new int[constraints.size()];
        int[] mins = new int[constraints.size()];
        int[] maxs = new int[constraints.size()];
        boolean[] isFill = new boolean[constraints.size()];
        Arrays.fill(maxs, Integer.MAX_VALUE);

        int remaining = distributable;
        int fillWeight = 0;

        // First pass: calculate fixed sizes and collect fill weights
        for (int i = 0; i < constraints.size(); i++) {
            Constraint c = constraints.get(i);
            switch (c) {
                case Constraint.Length(int v) -> {
                    sizes[i] = Math.min(v, distributable);
                    remaining -= sizes[i];
                }
                case Constraint.Percentage(int p) -> {
                    sizes[i] = distributable * p / 100;
                    remaining -= sizes[i];
                }
                case Constraint.Ratio(int num, int den) -> {
                    sizes[i] = distributable * num / den;
                    remaining -= sizes[i];
                }
                case Constraint.Min(int v) -> {
                    mins[i] = v;
                    isFill[i] = true;
                    fillWeight += 1;
                }
                case Constraint.Max(int v) -> {
                    maxs[i] = v;
                    isFill[i] = true;
                    fillWeight += 1;
                }
                case Constraint.Fill(int w) -> {
                    isFill[i] = true;
                    fillWeight += w;
                }
            }
        }

        // Second pass: distribute remaining space to Fill/Min/Max constraints
        if (fillWeight > 0 && remaining > 0) {
            int weightIndex = 0;
            for (int i = 0; i < constraints.size(); i++) {
                if (isFill[i]) {
                    int weight = switch (constraints.get(i)) {
                        case Constraint.Fill(int w) -> w;
                        case Constraint.Min ignored -> 1;
                        case Constraint.Max ignored -> 1;
                        default -> 0;
                    };
                    sizes[i] = remaining * weight / fillWeight;
                    weightIndex++;
                }
            }
        }

        // Third pass: apply min/max bounds
        for (int i = 0; i < constraints.size(); i++) {
            sizes[i] = Math.max(mins[i], Math.min(maxs[i], sizes[i]));
        }

        // Build rectangles
        List<Rect> result = new ArrayList<>(constraints.size());
        int pos = direction == Direction.HORIZONTAL ? inner.x() : inner.y();

        for (int i = 0; i < sizes.length; i++) {
            Rect rect = direction == Direction.HORIZONTAL
                ? new Rect(pos, inner.y(), sizes[i], inner.height())
                : new Rect(inner.x(), pos, inner.width(), sizes[i]);
            result.add(rect);
            pos += sizes[i] + spacing;
        }

        return result;
    }
}
