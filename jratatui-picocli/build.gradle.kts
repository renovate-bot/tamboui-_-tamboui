plugins {
    id("io.github.jratatui.java-library")
}

description = "PicoCLI integration for JRatatui TUI applications"

dependencies {
    api(project(":jratatui-tui"))
    api(libs.picocli)
}
