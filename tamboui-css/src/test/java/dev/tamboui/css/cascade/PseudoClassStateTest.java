/*
 * Copyright (c) 2025 TamboUI Contributors
 * SPDX-License-Identifier: MIT
 */
package dev.tamboui.css.cascade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PseudoClassStateTest {

    @Nested
    @DisplayName("NONE constant")
    class NoneConstant {

        @Test
        @DisplayName("NONE has all flags set to false")
        void noneHasAllFlagsFalse() {
            assertThat(PseudoClassState.NONE.isFocused()).isFalse();
            assertThat(PseudoClassState.NONE.isHovered()).isFalse();
            assertThat(PseudoClassState.NONE.isDisabled()).isFalse();
            assertThat(PseudoClassState.NONE.isActive()).isFalse();
            assertThat(PseudoClassState.NONE.isFirstChild()).isFalse();
            assertThat(PseudoClassState.NONE.isLastChild()).isFalse();
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("ofFocused() creates state with only focused flag set")
        void ofFocused() {
            PseudoClassState state = PseudoClassState.ofFocused();

            assertThat(state.isFocused()).isTrue();
            assertThat(state.isHovered()).isFalse();
            assertThat(state.isDisabled()).isFalse();
            assertThat(state.isActive()).isFalse();
            assertThat(state.isFirstChild()).isFalse();
            assertThat(state.isLastChild()).isFalse();
        }

        @Test
        @DisplayName("ofHovered() creates state with only hovered flag set")
        void ofHovered() {
            PseudoClassState state = PseudoClassState.ofHovered();

            assertThat(state.isFocused()).isFalse();
            assertThat(state.isHovered()).isTrue();
            assertThat(state.isDisabled()).isFalse();
            assertThat(state.isActive()).isFalse();
            assertThat(state.isFirstChild()).isFalse();
            assertThat(state.isLastChild()).isFalse();
        }

        @Test
        @DisplayName("ofDisabled() creates state with only disabled flag set")
        void ofDisabled() {
            PseudoClassState state = PseudoClassState.ofDisabled();

            assertThat(state.isFocused()).isFalse();
            assertThat(state.isHovered()).isFalse();
            assertThat(state.isDisabled()).isTrue();
            assertThat(state.isActive()).isFalse();
            assertThat(state.isFirstChild()).isFalse();
            assertThat(state.isLastChild()).isFalse();
        }
    }

    @Nested
    @DisplayName("Constructor")
    class Constructor {

        @Test
        @DisplayName("Constructor sets all flags correctly")
        void constructorSetsAllFlags() {
            PseudoClassState state = new PseudoClassState(
                    true, true, true, true, true, true
            );

            assertThat(state.isFocused()).isTrue();
            assertThat(state.isHovered()).isTrue();
            assertThat(state.isDisabled()).isTrue();
            assertThat(state.isActive()).isTrue();
            assertThat(state.isFirstChild()).isTrue();
            assertThat(state.isLastChild()).isTrue();
        }

        @Test
        @DisplayName("Constructor with mixed flags")
        void constructorWithMixedFlags() {
            PseudoClassState state = new PseudoClassState(
                    true, false, true, false, true, false
            );

            assertThat(state.isFocused()).isTrue();
            assertThat(state.isHovered()).isFalse();
            assertThat(state.isDisabled()).isTrue();
            assertThat(state.isActive()).isFalse();
            assertThat(state.isFirstChild()).isTrue();
            assertThat(state.isLastChild()).isFalse();
        }
    }

    @Nested
    @DisplayName("with* methods")
    class WithMethods {

        @Test
        @DisplayName("withFocused() returns new state with focused flag changed")
        void withFocused() {
            PseudoClassState original = PseudoClassState.NONE;
            PseudoClassState modified = original.withFocused(true);

            assertThat(modified.isFocused()).isTrue();
            assertThat(original.isFocused()).isFalse(); // Original unchanged
        }

        @Test
        @DisplayName("withHovered() returns new state with hovered flag changed")
        void withHovered() {
            PseudoClassState original = PseudoClassState.NONE;
            PseudoClassState modified = original.withHovered(true);

            assertThat(modified.isHovered()).isTrue();
            assertThat(original.isHovered()).isFalse();
        }

        @Test
        @DisplayName("withDisabled() returns new state with disabled flag changed")
        void withDisabled() {
            PseudoClassState original = PseudoClassState.NONE;
            PseudoClassState modified = original.withDisabled(true);

            assertThat(modified.isDisabled()).isTrue();
            assertThat(original.isDisabled()).isFalse();
        }

        @Test
        @DisplayName("withActive() returns new state with active flag changed")
        void withActive() {
            PseudoClassState original = PseudoClassState.NONE;
            PseudoClassState modified = original.withActive(true);

            assertThat(modified.isActive()).isTrue();
            assertThat(original.isActive()).isFalse();
        }

        @Test
        @DisplayName("withFirstChild() returns new state with firstChild flag changed")
        void withFirstChild() {
            PseudoClassState original = PseudoClassState.NONE;
            PseudoClassState modified = original.withFirstChild(true);

            assertThat(modified.isFirstChild()).isTrue();
            assertThat(original.isFirstChild()).isFalse();
        }

        @Test
        @DisplayName("withLastChild() returns new state with lastChild flag changed")
        void withLastChild() {
            PseudoClassState original = PseudoClassState.NONE;
            PseudoClassState modified = original.withLastChild(true);

            assertThat(modified.isLastChild()).isTrue();
            assertThat(original.isLastChild()).isFalse();
        }

        @Test
        @DisplayName("with* methods preserve other flags")
        void withMethodsPreserveOtherFlags() {
            PseudoClassState state = new PseudoClassState(
                    true, true, false, false, true, false
            );

            PseudoClassState modified = state.withDisabled(true);

            assertThat(modified.isFocused()).isTrue();
            assertThat(modified.isHovered()).isTrue();
            assertThat(modified.isDisabled()).isTrue();
            assertThat(modified.isActive()).isFalse();
            assertThat(modified.isFirstChild()).isTrue();
            assertThat(modified.isLastChild()).isFalse();
        }

        @Test
        @DisplayName("with* methods can be chained")
        void withMethodsCanBeChained() {
            PseudoClassState state = PseudoClassState.NONE
                    .withFocused(true)
                    .withHovered(true)
                    .withFirstChild(true);

            assertThat(state.isFocused()).isTrue();
            assertThat(state.isHovered()).isTrue();
            assertThat(state.isDisabled()).isFalse();
            assertThat(state.isActive()).isFalse();
            assertThat(state.isFirstChild()).isTrue();
            assertThat(state.isLastChild()).isFalse();
        }
    }

    @Nested
    @DisplayName("has() method")
    class HasMethod {

        @Test
        @DisplayName("has() returns true for focus when focused")
        void hasFocus() {
            PseudoClassState state = PseudoClassState.ofFocused();

            assertThat(state.has("focus")).isTrue();
        }

        @Test
        @DisplayName("has() returns true for hover when hovered")
        void hasHover() {
            PseudoClassState state = PseudoClassState.ofHovered();

            assertThat(state.has("hover")).isTrue();
        }

        @Test
        @DisplayName("has() returns true for disabled when disabled")
        void hasDisabled() {
            PseudoClassState state = PseudoClassState.ofDisabled();

            assertThat(state.has("disabled")).isTrue();
        }

        @Test
        @DisplayName("has() returns true for active when active")
        void hasActive() {
            PseudoClassState state = PseudoClassState.NONE.withActive(true);

            assertThat(state.has("active")).isTrue();
        }

        @Test
        @DisplayName("has() returns true for first-child when firstChild")
        void hasFirstChild() {
            PseudoClassState state = PseudoClassState.NONE.withFirstChild(true);

            assertThat(state.has("first-child")).isTrue();
        }

        @Test
        @DisplayName("has() returns true for last-child when lastChild")
        void hasLastChild() {
            PseudoClassState state = PseudoClassState.NONE.withLastChild(true);

            assertThat(state.has("last-child")).isTrue();
        }

        @Test
        @DisplayName("has() returns false for unknown pseudo-class")
        void hasUnknown() {
            PseudoClassState state = new PseudoClassState(
                    true, true, true, true, true, true
            );

            assertThat(state.has("unknown")).isFalse();
            assertThat(state.has("visited")).isFalse();
            assertThat(state.has("")).isFalse();
        }

        @Test
        @DisplayName("has() returns false when flag is not set")
        void hasFalseWhenNotSet() {
            PseudoClassState state = PseudoClassState.NONE;

            assertThat(state.has("focus")).isFalse();
            assertThat(state.has("hover")).isFalse();
            assertThat(state.has("disabled")).isFalse();
            assertThat(state.has("active")).isFalse();
            assertThat(state.has("first-child")).isFalse();
            assertThat(state.has("last-child")).isFalse();
        }
    }

    @Nested
    @DisplayName("equals() and hashCode()")
    class EqualsAndHashCode {

        @Test
        @DisplayName("equals() returns true for same state")
        void equalsForSameState() {
            PseudoClassState state1 = new PseudoClassState(
                    true, false, true, false, true, false
            );
            PseudoClassState state2 = new PseudoClassState(
                    true, false, true, false, true, false
            );

            assertThat(state1).isEqualTo(state2);
        }

        @Test
        @DisplayName("equals() returns false for different state")
        void notEqualsForDifferentState() {
            PseudoClassState state1 = PseudoClassState.ofFocused();
            PseudoClassState state2 = PseudoClassState.ofHovered();

            assertThat(state1).isNotEqualTo(state2);
        }

        @Test
        @DisplayName("equals() returns true for same instance")
        void equalsSameInstance() {
            PseudoClassState state = PseudoClassState.ofFocused();

            assertThat(state).isEqualTo(state);
        }

        @Test
        @DisplayName("equals() returns false for null")
        void notEqualsNull() {
            PseudoClassState state = PseudoClassState.ofFocused();

            assertThat(state).isNotEqualTo(null);
        }

        @Test
        @DisplayName("equals() returns false for different type")
        void notEqualsDifferentType() {
            PseudoClassState state = PseudoClassState.ofFocused();

            assertThat(state).isNotEqualTo("not a PseudoClassState");
        }

        @Test
        @DisplayName("hashCode() is same for equal states")
        void hashCodeSameForEqualStates() {
            PseudoClassState state1 = new PseudoClassState(
                    true, true, false, false, true, true
            );
            PseudoClassState state2 = new PseudoClassState(
                    true, true, false, false, true, true
            );

            assertThat(state1.hashCode()).isEqualTo(state2.hashCode());
        }

        @Test
        @DisplayName("hashCode() is different for different states")
        void hashCodeDifferentForDifferentStates() {
            PseudoClassState state1 = PseudoClassState.ofFocused();
            PseudoClassState state2 = PseudoClassState.ofHovered();

            assertThat(state1.hashCode()).isNotEqualTo(state2.hashCode());
        }
    }

    @Nested
    @DisplayName("toString()")
    class ToStringTest {

        @Test
        @DisplayName("toString() includes all flag values")
        void toStringIncludesAllFlags() {
            PseudoClassState state = new PseudoClassState(
                    true, false, true, false, true, false
            );

            String str = state.toString();

            assertThat(str).contains("focused=true");
            assertThat(str).contains("hovered=false");
            assertThat(str).contains("disabled=true");
            assertThat(str).contains("active=false");
            assertThat(str).contains("firstChild=true");
            assertThat(str).contains("lastChild=false");
        }

        @Test
        @DisplayName("toString() starts with class name")
        void toStringStartsWithClassName() {
            PseudoClassState state = PseudoClassState.NONE;

            assertThat(state.toString()).startsWith("PseudoClassState{");
        }
    }
}
