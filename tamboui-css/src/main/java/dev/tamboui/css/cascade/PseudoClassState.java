/*
 * Copyright (c) 2025 TamboUI Contributors
 * SPDX-License-Identifier: MIT
 */
package dev.tamboui.css.cascade;

import java.util.Objects;

/**
 * Represents the current pseudo-class state of an element.
 * <p>
 * This is used during selector matching to determine if pseudo-class
 * selectors like :focus, :hover, :disabled match the element.
 */
public final class PseudoClassState {

    /**
     * Empty state with all flags set to false.
     */
    public static final PseudoClassState NONE = new PseudoClassState(
            false, false, false, false, false, false
    );

    private final boolean focused;
    private final boolean hovered;
    private final boolean disabled;
    private final boolean active;
    private final boolean firstChild;
    private final boolean lastChild;

    public PseudoClassState(boolean focused,
                            boolean hovered,
                            boolean disabled,
                            boolean active,
                            boolean firstChild,
                            boolean lastChild) {
        this.focused = focused;
        this.hovered = hovered;
        this.disabled = disabled;
        this.active = active;
        this.firstChild = firstChild;
        this.lastChild = lastChild;
    }

    /**
     * Creates a state with only the focused flag set.
     */
    public static PseudoClassState ofFocused() {
        return new PseudoClassState(true, false, false, false, false, false);
    }

    /**
     * Creates a state with only the hovered flag set.
     */
    public static PseudoClassState ofHovered() {
        return new PseudoClassState(false, true, false, false, false, false);
    }

    /**
     * Creates a state with only the disabled flag set.
     */
    public static PseudoClassState ofDisabled() {
        return new PseudoClassState(false, false, true, false, false, false);
    }

    public boolean isFocused() {
        return focused;
    }

    public boolean isHovered() {
        return hovered;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isFirstChild() {
        return firstChild;
    }

    public boolean isLastChild() {
        return lastChild;
    }

    /**
     * Returns a new state with the focused flag set.
     */
    public PseudoClassState withFocused(boolean focused) {
        return new PseudoClassState(focused, hovered, disabled, active, firstChild, lastChild);
    }

    /**
     * Returns a new state with the hovered flag set.
     */
    public PseudoClassState withHovered(boolean hovered) {
        return new PseudoClassState(focused, hovered, disabled, active, firstChild, lastChild);
    }

    /**
     * Returns a new state with the disabled flag set.
     */
    public PseudoClassState withDisabled(boolean disabled) {
        return new PseudoClassState(focused, hovered, disabled, active, firstChild, lastChild);
    }

    /**
     * Returns a new state with the active flag set.
     */
    public PseudoClassState withActive(boolean active) {
        return new PseudoClassState(focused, hovered, disabled, active, firstChild, lastChild);
    }

    /**
     * Returns a new state with the firstChild flag set.
     */
    public PseudoClassState withFirstChild(boolean firstChild) {
        return new PseudoClassState(focused, hovered, disabled, active, firstChild, lastChild);
    }

    /**
     * Returns a new state with the lastChild flag set.
     */
    public PseudoClassState withLastChild(boolean lastChild) {
        return new PseudoClassState(focused, hovered, disabled, active, firstChild, lastChild);
    }

    /**
     * Checks if the given pseudo-class is active.
     *
     * @param pseudoClass the pseudo-class name (without colon)
     * @return true if the pseudo-class is active
     */
    public boolean has(String pseudoClass) {
        switch (pseudoClass) {
            case "focus":
                return focused;
            case "hover":
                return hovered;
            case "disabled":
                return disabled;
            case "active":
                return active;
            case "first-child":
                return firstChild;
            case "last-child":
                return lastChild;
            default:
                return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PseudoClassState)) {
            return false;
        }
        PseudoClassState that = (PseudoClassState) o;
        return focused == that.focused &&
                hovered == that.hovered &&
                disabled == that.disabled &&
                active == that.active &&
                firstChild == that.firstChild &&
                lastChild == that.lastChild;
    }

    @Override
    public int hashCode() {
        return Objects.hash(focused, hovered, disabled, active, firstChild, lastChild);
    }

    @Override
    public String toString() {
        return "PseudoClassState{" +
                "focused=" + focused +
                ", hovered=" + hovered +
                ", disabled=" + disabled +
                ", active=" + active +
                ", firstChild=" + firstChild +
                ", lastChild=" + lastChild +
                '}';
    }
}
