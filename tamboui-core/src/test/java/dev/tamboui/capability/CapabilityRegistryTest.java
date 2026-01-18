/*
 * Copyright (c) 2025 TamboUI Contributors
 * SPDX-License-Identifier: MIT
 */
package dev.tamboui.capability;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CapabilityRegistryTest {

    @Test
    void loadsProvidersViaServiceLoader_and_buildsReport() {
        CapabilityRegistry registry = CapabilityRegistry.load();

        assertThat(registry.providers())
                .extracting(p -> p.getClass().getName())
                .contains(
                        "dev.tamboui.capability.core.CoreCapabilityProvider",
                        "dev.tamboui.capability.test.TestCapabilityProvider"
                );

        CapabilityReport report = registry.buildReport();
        assertThat(report.feature("tamboui-core", "capabilityProvider.loadError.0", String.class))
                .isPresent();
        assertThat(report.feature("tamboui-core", "backend.providers", String.class))
                .contains("test");
        assertThat(report.feature("tamboui-core", "backend.order", String.class))
                .hasValueSatisfying(v -> assertThat(v).contains("test").contains("missing"));
        assertThat(report.feature("tamboui-core", "backend.selected", String.class))
                .hasValue("test");
        assertThat(report.feature("tamboui-core", "backend.test.loadable", Boolean.class))
                .contains(true);
        assertThat(report.feature("tamboui-core", "backend.missing.loadable", Boolean.class))
                .contains(false);
        assertThat(report.feature("tamboui-core", "backend.missing.error", String.class))
                .isPresent();
        assertThat(report.feature("tamboui-test", "foo", String.class)).contains("bar");
        assertThat(report.feature("tamboui-test", "answer", Integer.class)).contains(42);
        assertThat(report.feature("tamboui-test", "feature.a", Boolean.class)).contains(true);
        assertThat(report.feature("tamboui-test", "feature.b", Boolean.class)).contains(false);
    }
}


