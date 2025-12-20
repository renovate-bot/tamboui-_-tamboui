import org.gradle.api.tasks.compile.JavaCompile

plugins {
    java
}

repositories {
    mavenCentral()
}

java {
    // Default to Java 8 for library modules; allow demos to stay on 21
    val isDemo = project.path.startsWith(":demos:")
    val version = if (isDemo) JavaVersion.VERSION_21 else JavaVersion.VERSION_1_8
    sourceCompatibility = version
    targetCompatibility = version
}

tasks.withType<JavaCompile>().configureEach {
    val isDemo = project.path.startsWith(":demos:")
    // Compile main code for Java 8 unless this is a demo module; allow tests to use 21 to keep newer syntax in test sources
    val releaseVersion = if (name.contains("Test") || isDemo) 21 else 8
    options.release.set(releaseVersion)
    options.compilerArgs.add("-Xlint:-options")
}

group = "io.github.jratatui"
