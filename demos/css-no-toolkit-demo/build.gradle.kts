plugins {
    id("dev.tamboui.demo-project")
}

description = "Demo showcasing CSS styling without the toolkit module, using the core and widgets APIs only"

dependencies {
    implementation(projects.tambouiWidgets)
    implementation(projects.tambouiCss)
}

application {
    mainClass.set("dev.tamboui.demo.CssNoToolkitDemo")
}
