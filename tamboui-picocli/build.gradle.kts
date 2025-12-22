plugins {
    id("dev.tamboui.java-library")
}

description = "PicoCLI integration for TamboUI TUI applications"

dependencies {
    api(projects.tambouiTui)
    api(libs.picocli)
}
