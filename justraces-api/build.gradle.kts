plugins {
    id("java-library")
    id("maven-publish")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-releases/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
    compileOnly("org.jetbrains:annotations:24.1.0")

    compileOnly("com.github.retrooper:packetevents-spigot:2.13.0")
    api("com.jeff-media:MorePersistentDataTypes:2.4.0")
    api("org.spongepowered:configurate-gson:4.2.0")
    api("org.spongepowered:configurate-extra-guice:4.2.0")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}