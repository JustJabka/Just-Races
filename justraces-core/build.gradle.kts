plugins {
    id("java-library")
    id("com.gradleup.shadow")
    id("xyz.jpenilla.run-paper")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-releases/")
}

dependencies {
    implementation(project(":justraces-api"))

    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
    compileOnly("org.jetbrains:annotations:24.1.0")

    compileOnly("com.github.retrooper:packetevents-spigot:2.13.0")
    implementation("com.jeff-media:MorePersistentDataTypes:2.4.0")
    implementation("org.spongepowered:configurate-gson:4.2.0")
    implementation("org.spongepowered:configurate-extra-guice:4.2.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
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
        minecraftVersion("26.2")
        jvmArgs("-Xms2G", "-Xmx2G")
        runDirectory.set(rootProject.file("run"))
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