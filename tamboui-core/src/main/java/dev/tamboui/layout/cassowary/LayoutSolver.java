/*
 * Copyright (c) 2025 TamboUI Contributors
 * SPDX-License-Identifier: MIT
 */
package dev.tamboui.layout.cassowary;

import dev.tamboui.layout.Constraint;
import dev.tamboui.layout.Flex;

import java.util.List;

/**
 * Bridge between TamboUI layout constraints and the Cassowary solver.
 *
 * <p>This class translates TamboUI's constraint types and Flex modes into
 * Cassowary constraints and solves them using the simplex method.
 *
 * <p>The solver creates variables for each segment's position and size,
 * then adds constraints based on the TamboUI constraint types and the
 * selected Flex distribution mode.
 *
 * @see Solver
 * @see dev.tamboui.layout.Layout
 */
public final class LayoutSolver {

    private final Solver solver;

    /**
     * Creates a new layout solver.
     */
    public LayoutSolver() {
        this.solver = new Solver();
    }

    /**
     * Solves layout constraints and returns the computed sizes.
     *
     * @param constraints TamboUI constraints for each segment
     * @param available   total available space
     * @param spacing     space between elements
     * @param flex        flex distribution mode
     * @return array of computed sizes for each constraint
     */
    public int[] solve(List<Constraint> constraints, int available, int spacing, Flex flex) {
        solver.reset();

        int n = constraints.size();
        if (n == 0) {
            return new int[0];
        }

        Variable[] sizes = new Variable[n];
        Variable[] positions = new Variable[n + 1];

        // Create variables
        for (int i = 0; i < n; i++) {
            sizes[i] = new Variable("size_" + i);
            positions[i] = new Variable("pos_" + i);
        }
        positions[n] = new Variable("pos_end");

        // Add position and size relationship constraints (always required)
        addStructuralConstraints(positions, sizes, n, spacing, available, flex);

        // Convert TamboUI constraints to Cassowary constraints
        int totalFillWeight = computeTotalFillWeight(constraints);
        for (int i = 0; i < n; i++) {
            addConstraintFor(constraints.get(i), sizes[i], available, totalFillWeight);
        }

        // Solve and extract results
        solver.updateVariables();

        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = Math.max(0, (int) Math.round(solver.valueOf(sizes[i])));
        }

        // Apply flex adjustments for positioning
        return applyFlexPositioning(result, available, spacing, flex);
    }

    /**
     * Adds structural constraints that define the relationship between positions and sizes.
     */
    private void addStructuralConstraints(Variable[] positions, Variable[] sizes,
                                          int n, int spacing, int available, Flex flex) {
        // All sizes must be non-negative
        for (int i = 0; i < n; i++) {
            solver.addConstraint(
                    Expression.variable(sizes[i])
                            .greaterThanOrEqual(0, Strength.REQUIRED));
        }

        solver.addConstraint(
                Expression.variable(positions[0])
                        .equalTo(0, Strength.REQUIRED));

        // Position relationships: pos[i+1] = pos[i] + size[i] + spacing
        for (int i = 0; i < n; i++) {
            int gap = (i < n - 1) ? spacing : 0;
            solver.addConstraint(
                    Expression.variable(positions[i + 1])
                            .equalTo(Expression.variable(positions[i])
                                            .plus(Expression.variable(sizes[i]))
                                            .plus(gap),
                                    Strength.REQUIRED));
        }

        // Total space constraint: last position <= available
        solver.addConstraint(
                Expression.variable(positions[n])
                        .lessThanOrEqual(available, Strength.REQUIRED));
    }

    /**
     * Computes the total fill weight from all Fill constraints.
     */
    private int computeTotalFillWeight(List<Constraint> constraints) {
        int total = 0;
        for (Constraint c : constraints) {
            if (c instanceof Constraint.Fill) {
                total += ((Constraint.Fill) c).weight();
            } else if (c instanceof Constraint.Min || c instanceof Constraint.Max) {
                total += 1; // Min and Max behave like Fill(1)
            }
        }
        return total;
    }

    /**
     * Converts a TamboUI constraint to Cassowary constraints.
     */
    private void addConstraintFor(Constraint c, Variable size, int available,
                                  int totalFillWeight) {
        if (c instanceof Constraint.Length) {
            // Fixed size: size == value (strong)
            int value = ((Constraint.Length) c).value();
            solver.addConstraint(
                    Expression.variable(size)
                            .equalTo(value, Strength.STRONG));
        } else if (c instanceof Constraint.Percentage) {
            // Percentage: size == available * percent / 100 (strong)
            // Use integer division to match original behavior
            int percent = ((Constraint.Percentage) c).value();
            int target = available * percent / 100;
            solver.addConstraint(
                    Expression.variable(size)
                            .equalTo(target, Strength.STRONG));
        } else if (c instanceof Constraint.Ratio) {
            // Ratio: size == available * num / denom (strong)
            // Use integer division to match original behavior
            Constraint.Ratio ratio = (Constraint.Ratio) c;
            int target = available * ratio.numerator() / ratio.denominator();
            solver.addConstraint(
                    Expression.variable(size)
                            .equalTo(target, Strength.STRONG));
        } else if (c instanceof Constraint.Min) {
            // Minimum: size >= value (required), try to fill remaining (weak)
            int value = ((Constraint.Min) c).value();
            solver.addConstraint(
                    Expression.variable(size)
                            .greaterThanOrEqual(value, Strength.REQUIRED));
            // Weak preference to take share of remaining space
            if (totalFillWeight > 0) {
                double share = available / (double) totalFillWeight;
                solver.addConstraint(
                        Expression.variable(size)
                                .equalTo(Math.max(value, share), Strength.WEAK));
            }
        } else if (c instanceof Constraint.Max) {
            // Maximum: size <= value (required), try to fill remaining (weak)
            int value = ((Constraint.Max) c).value();
            solver.addConstraint(
                    Expression.variable(size)
                            .lessThanOrEqual(value, Strength.REQUIRED));
            // Weak preference to take share of remaining space
            if (totalFillWeight > 0) {
                double share = available / (double) totalFillWeight;
                solver.addConstraint(
                        Expression.variable(size)
                                .equalTo(Math.min(value, share), Strength.WEAK));
            }
        } else if (c instanceof Constraint.Fill) {
            // Fill: try to take weighted share of remaining space
            int weight = ((Constraint.Fill) c).weight();
            if (totalFillWeight > 0) {
                double share = available * weight / (double) totalFillWeight;
                // Use custom strength based on weight
                Strength fillStrength = Strength.create(0, 0, weight);
                solver.addConstraint(
                        Expression.variable(size)
                                .equalTo(share, fillStrength));
            }
        }
    }

    /**
     * Returns the computed sizes. Flex positioning is handled by {@link dev.tamboui.layout.Layout}.
     */
    private int[] applyFlexPositioning(int[] sizes, int available, int spacing, Flex flex) {
        return sizes;
    }
}
