/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.layout;

/**
 * Constraints for layout space allocation.
 */
public sealed interface Constraint permits
    Constraint.Length,
    Constraint.Percentage,
    Constraint.Ratio,
    Constraint.Min,
    Constraint.Max,
    Constraint.Fill {

    /**
     * Fixed size in cells.
     */
    record Length(int value) implements Constraint {
        public Length {
            if (value < 0) {
                throw new IllegalArgumentException("Length cannot be negative: " + value);
            }
        }
    }

    /**
     * Percentage of available space (0-100).
     */
    record Percentage(int value) implements Constraint {
        public Percentage {
            if (value < 0 || value > 100) {
                throw new IllegalArgumentException("Percentage must be between 0 and 100: " + value);
            }
        }
    }

    /**
     * Ratio of available space (numerator/denominator).
     */
    record Ratio(int numerator, int denominator) implements Constraint {
        public Ratio {
            if (denominator <= 0) {
                throw new IllegalArgumentException("Denominator must be positive: " + denominator);
            }
            if (numerator < 0) {
                throw new IllegalArgumentException("Numerator cannot be negative: " + numerator);
            }
        }
    }

    /**
     * Minimum size in cells.
     */
    record Min(int value) implements Constraint {
        public Min {
            if (value < 0) {
                throw new IllegalArgumentException("Min cannot be negative: " + value);
            }
        }
    }

    /**
     * Maximum size in cells.
     */
    record Max(int value) implements Constraint {
        public Max {
            if (value < 0) {
                throw new IllegalArgumentException("Max cannot be negative: " + value);
            }
        }
    }

    /**
     * Fill remaining space with given weight.
     */
    record Fill(int weight) implements Constraint {
        public Fill {
            if (weight < 1) {
                throw new IllegalArgumentException("Fill weight must be at least 1: " + weight);
            }
        }

        public Fill() {
            this(1);
        }
    }

    // Convenience factory methods
    static Constraint length(int value) {
        return new Length(value);
    }

    static Constraint percentage(int value) {
        return new Percentage(value);
    }

    static Constraint ratio(int numerator, int denominator) {
        return new Ratio(numerator, denominator);
    }

    static Constraint min(int value) {
        return new Min(value);
    }

    static Constraint max(int value) {
        return new Max(value);
    }

    static Constraint fill(int weight) {
        return new Fill(weight);
    }

    static Constraint fill() {
        return new Fill(1);
    }
}
