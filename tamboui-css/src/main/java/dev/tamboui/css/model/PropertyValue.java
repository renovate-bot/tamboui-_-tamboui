/*
 * Copyright (c) 2025 TamboUI Contributors
 * SPDX-License-Identifier: MIT
 */
package dev.tamboui.css.model;

import java.util.Objects;

/**
 * Represents a CSS property value as parsed from the stylesheet.
 * <p>
 * The raw value is stored as a string and converted to the appropriate
 * type when applied to elements.
 */
public final class PropertyValue {

    /**
     * The keyword used when a child wants to explicitly inherit a value from its parent.
     */
    public static final String INHERIT_KEYWORD = "inherit";

    /**
     * The keyword used when a parent wants to mark a value as inheritable to children.
     */
    public static final String INHERITABLE_KEYWORD = "inheritable";

    private final String raw;
    private final boolean important;
    private final boolean inheritable;

    /**
     * Creates a property value with all flags.
     *
     * @param raw         the raw value string
     * @param important   whether the value has !important
     * @param inheritable whether the value is marked as inheritable
     */
    public PropertyValue(String raw, boolean important, boolean inheritable) {
        this.raw = Objects.requireNonNull(raw);
        this.important = important;
        this.inheritable = inheritable;
    }

    /**
     * Backward-compatible constructor.
     *
     * @param raw       the raw value string
     * @param important whether the value has !important
     */
    public PropertyValue(String raw, boolean important) {
        this(raw, important, false);
    }

    /**
     * Creates a regular (non-important) property value.
     *
     * @param raw the raw value string
     * @return the property value
     */
    public static PropertyValue of(String raw) {
        return new PropertyValue(raw, false, false);
    }

    /**
     * Creates an important (!important) property value.
     *
     * @param raw the raw value string
     * @return the property value
     */
    public static PropertyValue important(String raw) {
        return new PropertyValue(raw, true, false);
    }

    /**
     * Creates an inheritable property value.
     *
     * @param raw the raw value string
     * @return the property value
     */
    public static PropertyValue inheritable(String raw) {
        return new PropertyValue(raw, false, true);
    }

    /**
     * Creates a property value with the specified flags.
     *
     * @param raw         the raw value string
     * @param important   whether the value has !important
     * @param inheritable whether the value is marked as inheritable
     * @return the property value
     */
    public static PropertyValue create(String raw, boolean important, boolean inheritable) {
        return new PropertyValue(raw, important, inheritable);
    }

    public String raw() {
        return raw;
    }

    public boolean important() {
        return important;
    }

    /**
     * Returns true if this value is marked as inheritable by the parent.
     *
     * @return true if marked inheritable
     */
    public boolean inheritable() {
        return inheritable;
    }

    /**
     * Returns true if this value is the special "inherit" keyword,
     * indicating the child wants to explicitly inherit from parent.
     *
     * @return true if the raw value is "inherit"
     */
    public boolean isInherit() {
        return INHERIT_KEYWORD.equalsIgnoreCase(raw);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PropertyValue)) {
            return false;
        }
        PropertyValue that = (PropertyValue) o;
        return important == that.important
                && inheritable == that.inheritable
                && raw.equals(that.raw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(raw, important, inheritable);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(raw);
        if (inheritable) {
            sb.append(" inheritable");
        }
        if (important) {
            sb.append(" !important");
        }
        return sb.toString();
    }
}
