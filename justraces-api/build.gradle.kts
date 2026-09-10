plugins {
    id("java-library")
    id("maven-publish")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
    compileOnly("org.jetbrains:annotations:24.1.0")

    compileOnly("net.dmulloy2:ProtocolLib:5.4.0")
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