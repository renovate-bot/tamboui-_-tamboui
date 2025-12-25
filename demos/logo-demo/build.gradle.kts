plugins {
    id("dev.tamboui.demo-project")
}

description = "Demo showcasing the Tamboui Logo widget"

dependencies {
    implementation(projects.tambouiTui)
}

application {
    mainClass.set("dev.tamboui.demo.LogoDemo")
}

