plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("com.gradleup.shadow") version "9.4.1"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("net.dmulloy2:ProtocolLib:5.4.0")
    implementation("com.jeff-media:MorePersistentDataTypes:2.4.0")
    implementation("org.spongepowered:configurate-hocon:4.2.0")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}


buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.9.1")
    }
}

tasks.register<proguard.gradle.ProGuardTask>("proguard") {
    description = "Obfuscates the JustRaces core while keeping the API and relocated libraries intact."
    verbose()
    notCompatibleWithConfigurationCache("ProGuard task is not compatible with Configuration Cache")

    // Take shadowjar
    injars(tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar").flatMap { it.archiveFile })
    dependsOn(tasks.named("shadowJar"))

    // Where to save final jar
    outjars("build/libs/${project.name}-${project.version}-obfuscated.jar")

    val javaHome = System.getProperty("java.home")
    if (System.getProperty("java.version").startsWith("1.")) {
        libraryjars("$javaHome/lib/rt.jar")
    } else {
        libraryjars(
            mapOf("jarfilter" to "!**.jar", "filter" to "!module-info.class"),
            "$javaHome/jmods/java.base.jmod"
        )
    }

    // Save mappings
    printmapping("build/proguard-mapping.txt")

    dontwarn()
    ignorewarnings()

    // Don't touch generics and signatures
    keepattributes("Signature,RuntimeVisibleAnnotations,AnnotationDefault,EnclosingMethod,InnerClasses,SourceFile,LineNumberTable")

    // Don't touch main class
    keep("""
        public class justjabka.JustRaces.JustRaces {
            public void onEnable();
            public void onDisable();
        }
    """)

    // Don't obfuscate API
    keep("""
        public class justjabka.JustRaces.Registries.** {
            public protected *;
        }
    """)
    keep("""
        public class justjabka.JustRaces.Abilities.Generic.** {
            public protected *;
        }
    """)
    keep("""
        public class justjabka.JustRaces.Modifiers.ItemModifier {
            public protected *;
        }
    """)

    // Don't obfuscate ANY libraries inside our shadow package (Guaranteed safety for reflection)
    keep("class justjabka.libs.** { *; }")

    // Suppress warnings from internal dependencies and compileOnly APIs
    dontwarn("justjabka.libs.**")
    dontwarn("org.spongepowered.configurate.**")
    dontwarn("com.jeff_media.morepersistentdatatypes.**")
    dontwarn("net.dmulloy2.protocol.**")
    dontwarn("io.papermc.paper.**")
    dontwarn("org.bukkit.**")
    dontwarn("org.apache.lang.**")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveVersion.set(version.toString())

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
        val props = mapOf("version" to version)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}
