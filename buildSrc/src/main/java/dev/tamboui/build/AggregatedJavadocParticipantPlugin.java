/*
 * Copyright 2003-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.tamboui.build;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.component.ProjectComponentIdentifier;
import org.gradle.api.attributes.Attribute;
import org.gradle.api.attributes.AttributeContainer;
import org.gradle.api.attributes.Usage;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.javadoc.Javadoc;

import java.util.HashSet;
import java.util.Set;

/**
 * A plugin which marks a project as a participant
 * in the aggregated javadoc generation. The aggregator
 * will need information about the javadoc classpath,
 * includes and excludes.
 */
public class AggregatedJavadocParticipantPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPluginManager().withPlugin("java", unused -> {
            JavaPluginExtension javaPluginExtension = project.getExtensions().getByType(JavaPluginExtension.class);
            Configuration internalJavadocElements = createFilteredJavadocSourcesElements(project);
            Configuration javadocElementClasspath = createJavadocElementClasspath(project);
            TaskProvider<PrepareJavadocAggregationTask> prepareTask = project.getTasks().register("prepareJavadocAggregation", PrepareJavadocAggregationTask.class, task -> {
                TaskProvider<Javadoc> javadoc = project.getTasks().named("javadoc", Javadoc.class);
                SourceSet sourceSet = javaPluginExtension.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME);
                task.getSources().from(sourceSet.getAllJava().getSourceDirectories());
                task.getIncludes().set(javadoc.map(Javadoc::getIncludes));
                task.getExcludes().set(javadoc.map(jd -> {
                    Set<String> allExcludes = new HashSet<>();
                    allExcludes.add("module-info.java"); // This is problematic with aggregated javadoc
                    allExcludes.addAll(jd.getExcludes());
                    return allExcludes;
                }));
                task.getOutputDir().set(project.getLayout().getBuildDirectory().dir("aggregation/javadoc"));
            });
            internalJavadocElements.getOutgoing().artifact(prepareTask);
        });
    }

    private Configuration createJavadocElementClasspath(Project project) {
        ConfigurationContainer configurations = project.getConfigurations();
        ProviderFactory providers = project.getProviders();
        return configurations.create("internalJavadocClasspathElements", conf -> {
            conf.setCanBeResolved(false);
            conf.setCanBeConsumed(true);
            Configuration classpath = configurations.getByName("runtimeClasspath");
            AttributeContainer compileClasspathAttrs = classpath.getAttributes();
            conf.getOutgoing().artifacts(classpath.getIncoming().artifactView(view -> {
                view.componentFilter(id -> !(id instanceof ProjectComponentIdentifier));
            }).getFiles().getElements());
            conf.attributes(attrs -> compileClasspathAttrs.keySet().forEach(key -> {
                //noinspection unchecked
                Attribute<Object> o = (Attribute<Object>) key;
                attrs.attributeProvider(o, providers.provider(() -> compileClasspathAttrs.getAttribute(o)));
                attrs.attribute(Usage.USAGE_ATTRIBUTE, project.getObjects().named(Usage.class, JavadocAggregationUtils.AGGREGATED_JAVADOC_PARTICIPANT_DEPS));
            }));
        });
    }

    private Configuration createFilteredJavadocSourcesElements(Project project) {
        return project.getConfigurations().create("internalJavadocElements", conf -> {
            conf.setCanBeConsumed(true);
            conf.setCanBeResolved(false);
            ObjectFactory objects = project.getObjects();
            JavadocAggregationUtils.configureJavadocSourcesAggregationAttributes(objects, conf);
        });
    }
}
