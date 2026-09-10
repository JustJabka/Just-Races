# JustRaces

[![](https://jitpack.io/v/JustJabka/Just-Races.svg)](https://jitpack.io/#JustJabka/Just-Races)

Fast, lightweight, data-driven Minecraft races framework

---

# Creating a Project

> [!TIP]
> If you have not set up a Paper plugin project yet, please refer to the official [Paper Documentation on Project Setup](https://docs.papermc.io/paper/dev/project-setup) first.

## Adding JustRaces as a Dependency

To use the JustRaces API in your project, you need to add the repository and the dependency to your build configuration file (`build.gradle.kts`).

### Configure `build.gradle.kts`

Add the JitPack repository to your `repositories` block and specify `Just-Races` under your `dependencies`:

```kts
// ...

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.JustJabka:Just-Races:v1.1.0")
}

// ...
```

> [!NOTE]
> Thanks to transitive dependencies included in the JustRaces API, you do not need to manually add [Configurate](https://github.com/spongepowered/configurate) or [MorePersistentDataTypes](https://github.com/mfnalex/MorePersistentDataTypes) to your build script - Gradle will resolve them automatically.

### Configure `paper-plugin.yml`

Next, register JustRaces as a dependency in your plugin's manifest file (`src/main/resources/paper-plugin.yml`). This ensures Paper loads JustRaces before your addon and joins their classloaders.

```yaml
name: ExampleAddon
version: '1.0.0'
api-version: '26.2'

# ...

dependencies:
  server:
    JustRaces:
      load: BEFORE
      required: true
      join-classpath: true
```

> [!IMPORTANT]
> Setting `join-classpath: true` is recommended so your addon can seamlessly interact with the shared API types during runtime.