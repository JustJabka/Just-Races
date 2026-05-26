plugins {
    id("java-library")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:24.1.0")

    compileOnly("net.dmulloy2:ProtocolLib:5.4.0")
    compileOnly("com.jeff-media:MorePersistentDataTypes:2.4.0")
    compileOnly("org.spongepowered:configurate-hocon:4.2.0")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}
