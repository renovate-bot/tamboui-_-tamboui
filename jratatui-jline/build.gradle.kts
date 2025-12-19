plugins {
    id("io.github.jratatui.java-library")
}

description = "JLine 3 backend for JRatatui TUI library"

dependencies {
    api(projects.jratatuiCore)
    api(libs.jline)
}
