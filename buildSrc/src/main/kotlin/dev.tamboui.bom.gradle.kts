import gradle.kotlin.dsl.accessors._7ad08c4249ccee6ca7a996e0b8e3c15c.publishing
import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.signing

plugins {
    `java-platform`
    id("dev.tamboui.publishing-base")
}

publishing {
    publications {
        named<MavenPublication>("mavenJava") {
            from(components["javaPlatform"])
        }
    }
}

group = "dev.tamboui"

dependencies {
    constraints {
        rootProject.allprojects.forEach { p ->
            p.pluginManager.withPlugin("dev.tamboui.publishing") {
                api(project(p.path))
            }
        }
    }
}