package justjabka.JustRaces.Registries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import justjabka.JustRaces.Instances.Deserializer.AbilityBindingDeserializer;
import justjabka.JustRaces.Instances.RaceInstance;
import justjabka.JustRaces.JustRacesAPI;
import justjabka.JustRaces.JustRacesRegistries;
import justjabka.JustRaces.Managers.ResourceManager;
import justjabka.JustRaces.Types.AbilityBinding;
import org.bukkit.NamespacedKey;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class RacesRegistry {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(AbilityBinding .class, new AbilityBindingDeserializer())
            .create();

    public static void loadAllRaces() {
        File rootRacesFolder = new File(JustRacesAPI.getInstance().getDataFolder(), "races");
        if (!rootRacesFolder.exists()) {
            rootRacesFolder.mkdirs();
        }

        ResourceManager.registerRacesFromPlugin(JustRacesAPI.getInstance());
        registerRaces(rootRacesFolder);

        JustRacesAPI.getLogger().info("Successfully registered {} races!", JustRacesRegistries.RACES.keys().size());
    }

    private static void registerRaces(File racesFolder) {
        Path rootPath = racesFolder.toPath();

        try (Stream<Path> stream = Files.walk(rootPath)) {
            stream.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> loadRaceFile(rootPath, path));
        } catch (IOException e) {
            JustRacesAPI.getLogger().error("Error while walking through races folder", e);
        }
    }

    private static void loadRaceFile(Path rootFolder, Path filePath) {
        File file = filePath.toFile();
        try (FileReader reader = new FileReader(file)) {
            RaceInstance race = gson.fromJson(reader, RaceInstance.class);

            Path relativePath = rootFolder.relativize(filePath);
            NamespacedKey key = parseNamespacedKey(relativePath.toString());

            race.setKey(key.toString());

            JustRacesRegistries.RACES.register(key, race);
            JustRacesAPI.getLogger().info("Registered race: {}", key);

        } catch (Exception e) {
            JustRacesAPI.getLogger().error("Error while registering race file: {}", filePath, e);
        }
    }

    private static NamespacedKey parseNamespacedKey(String relativePath) {
        String normalized = relativePath.replace('\\', '/');

        if (normalized.endsWith(".json")) {
            normalized = normalized.substring(0, normalized.length() - 5);
        }

        String[] parts = normalized.split("/");

        if (parts.length == 1) {
            return new NamespacedKey(JustRacesAPI.NAMESPACE, parts[0].toLowerCase());
        }
        String namespace = parts[0].toLowerCase();

        StringBuilder keyBuilder = new StringBuilder();
        for (int i = 1; i < parts.length; i++) {
            if (i > 1) keyBuilder.append("/");
            keyBuilder.append(parts[i]);
        }

        String keyPath = keyBuilder.toString().toLowerCase();

        return new NamespacedKey(namespace, keyPath);
    }
}