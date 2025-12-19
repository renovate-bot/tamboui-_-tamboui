rootProject.name = "jratatui-parent"

include(
    "jratatui-core",
    "jratatui-widgets",
    "jratatui-jline"
)

File("demos").listFiles()?.forEach {
    if (it.isDirectory) {
        include("demos:${it.name}")
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")