plugins {
    id("dev.tamboui.java-base")
    id("dev.tamboui.publishing-base")
}

publishing {
    publications {
        named<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
