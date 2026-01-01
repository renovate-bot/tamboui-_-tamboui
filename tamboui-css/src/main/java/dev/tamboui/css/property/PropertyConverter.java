/*
 * Copyright (c) 2025 TamboUI Contributors
 * SPDX-License-Identifier: MIT
 */
package dev.tamboui.css.property;

import java.util.Map;
import java.util.Optional;

/**
 * Converts CSS property values to typed objects.
 *
 * @param <T> the target type
 */
public interface PropertyConverter<T> {

    /**
     * Converts a CSS value string to the target type.
     *
     * @param value     the CSS value string
     * @param variables the CSS variables for resolving $references
     * @return the converted value, or empty if conversion fails
     */
    Optional<T> convert(String value, Map<String, String> variables);

    /**
     * Resolves variable references in a value.
     *
     * @param value     the value that may contain $variable references
     * @param variables the variables map
     * @return the resolved value
     */
    default String resolveVariables(String value, Map<String, String> variables) {
        if (value == null || !value.contains("$")) {
            return value;
        }

        String result = value;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("$" + entry.getKey(), entry.getValue());
        }
        return result;
    }
}
