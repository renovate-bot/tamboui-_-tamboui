plugins {
    id("dev.tamboui.demo-project")
}

description = "Demo showcasing TextArea multi-line text input"

dependencies {
    implementation(projects.tambouiToolkit)
}

application {
    mainClass.set("dev.tamboui.demo.TextAreaDemo")
}
