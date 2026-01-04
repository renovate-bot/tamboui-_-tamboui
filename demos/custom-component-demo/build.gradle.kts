plugins {
    id("dev.tamboui.demo-project")
}

description = "Demo showcasing custom CSS-styled components"

dependencies {
    implementation(projects.tambouiToolkit)
}

application {
    mainClass.set("dev.tamboui.demo.CustomComponentDemo")
}
