plugins {
    id("dev.tamboui.java-base")
    `maven-publish`
    signing
}

publishing {
    repositories {
        maven {
            name = "build"
            url = uri("${rootProject.layout.buildDirectory.get().asFile}/repo")
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            version = rootProject.version.toString()
            pom {
                name.set(project.name)
                description.set(provider { project.description })
                url.set("https://github.com/tamboui/tamboui")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("melix")
                        name.set("Cédric Champeau")
                        email.set("cedric.champeau@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git@github.com:tamboui/tamboui.git")
                    developerConnection.set("<scm:git@github.com:tamboui/tamboui.git")
                    url.set("scm:git@github.com:tamboui/tamboui.git")
                }
            }
        }
    }
}

val signingRequired = !version.toString().endsWith("-SNAPSHOT")

if (signingRequired) {
    signing {
        setRequired {
            gradle.taskGraph.allTasks.any {
                it.name.startsWith("publish")
            }
        }

        val signingKey = providers.environmentVariable("GPG_SIGNING_KEY")
        val signingPassword = providers.environmentVariable("GPG_SIGNING_PASSWORD")

        if (signingKey.isPresent && signingPassword.isPresent) {
            useInMemoryPgpKeys(signingKey.get(), signingPassword.get())
        } else {
            useGpgCmd()  // Fallback for local development
        }

        publishing.publications.configureEach {
            sign(this)
        }
    }
}
