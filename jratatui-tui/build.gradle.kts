plugins {
    id("io.github.jratatui.java-library")
}

description = "High-level TUI application framework for JRatatui"

dependencies {
    api(project(":jratatui-core"))
    api(project(":jratatui-widgets"))
    api(project(":jratatui-jline"))
}
