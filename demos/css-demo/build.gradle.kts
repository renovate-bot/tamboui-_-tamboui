plugins {
    id("dev.tamboui.demo-project")
}

description = "Demo showcasing CSS styling with live theme switching"

dependencies {
    implementation(projects.tambouiToolkit)
}

application {
    mainClass.set("dev.tamboui.demo.CssDemo")
}
