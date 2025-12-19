plugins {
    id("io.github.jratatui.demo-project")
}

description = "Demo showcasing the TuiRunner framework"

dependencies {
    implementation(project(":jratatui-tui"))
}

application {
    mainClass.set("io.github.jratatui.demo.TuiDemo")
}
