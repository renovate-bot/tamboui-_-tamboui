/*
 * Copyright (c) 2025 TamboUI Contributors
 * SPDX-License-Identifier: MIT
 */
package dev.tamboui.capability;

import dev.tamboui.util.SafeServiceLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Loads {@link CapabilityProvider}s via {@link java.util.ServiceLoader} and builds an aggregated report.
 */
public final class CapabilityRegistry {

    private final List<CapabilityProvider> providers;
    private final List<String> providerLoadErrors;

    private CapabilityRegistry(List<CapabilityProvider> providers, List<String> providerLoadErrors) {
        this.providers = Collections.unmodifiableList(providers);
        this.providerLoadErrors = Collections.unmodifiableList(providerLoadErrors);
    }

    public static CapabilityRegistry load() {
        List<CapabilityProvider> providers = new ArrayList<>();
        List<String> providerLoadErrors = new ArrayList<>();

        providers.addAll(SafeServiceLoader.load(CapabilityProvider.class, err ->
                providerLoadErrors.add(String.valueOf(err.getMessage()))));
        providers.sort(Comparator.comparing(CapabilityProvider::source));
        return new CapabilityRegistry(providers, providerLoadErrors);
    }

    public List<CapabilityProvider> providers() {
        return providers;
    }

    public CapabilityReport buildReport() {
        CapabilityReportBuilder builder = new CapabilityReportBuilder();

        for (int i = 0; i < providerLoadErrors.size(); i++) {
            builder.feature("tamboui-core", "capabilityProvider.loadError." + i, providerLoadErrors.get(i));
        }

        for (CapabilityProvider provider : providers) {
            try {
                provider.contribute(builder);
            } catch (Exception e) {
                builder.feature(provider.source(), "error", e.getClass().getName() + ": " + e.getMessage());
            } catch (LinkageError e) {
                builder.feature(provider.source(), "error", e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return builder.build();
    }
}


