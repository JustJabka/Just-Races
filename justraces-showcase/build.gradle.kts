plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(project(":justraces-api"))
    compileOnly(project(":justraces-core"))

    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
    compileOnly("org.jetbrains:annotations:24.1.0")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks {
    runServer {
        minecraftVersion("26.2")
        jvmArgs("-Xms2G", "-Xmx2G")
        runDirectory.set(rootProject.file("run"))

        downloadPlugins {
            val coreShadowJar = project(":justraces-core").tasks.named("shadowJar").flatMap {
                (it as AbstractArchiveTask).archiveFile
            }

            pluginJars(coreShadowJar)
        }
    }
}
