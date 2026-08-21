plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:24.1.0")

    implementation(project(":justraces-api"))
    compileOnly(project(":justraces-core"))

    compileOnly("net.dmulloy2:ProtocolLib:5.4.0")
    compileOnly("com.jeff-media:MorePersistentDataTypes:2.4.0")
    compileOnly("org.spongepowered:configurate-gson:4.2.0")
    compileOnly("org.spongepowered:configurate-extra-guice:4.2.0")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    runServer {
        minecraftVersion("1.21.11")
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
