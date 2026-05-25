plugins {
    id("java-library")
    id("com.gradleup.shadow")
    id("xyz.jpenilla.run-paper")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":justraces-api"))

    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:24.1.0")

    compileOnly("net.dmulloy2:ProtocolLib:5.4.0")
    implementation("com.jeff-media:MorePersistentDataTypes:2.4.0")
    implementation("org.spongepowered:configurate-hocon:4.2.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveVersion.set(rootProject.version.toString())

        relocate("com.jeff_media.morepersistentdatatypes", "justjabka.libs.morepersistentdatatypes")
        relocate("org.spongepowered.configurate", "justjabka.libs.configurate")

        relocate("io.leangen.geantyref", "justjabka.libs.geantyref")
        relocate("net.kyori.option", "justjabka.libs.option")
    }

    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.11")
        jvmArgs("-Xms2G", "-Xmx2G")
    }


    processResources {
        val props = mapOf("version" to rootProject.version)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }

    build {
        dependsOn(shadowJar)
    }
}