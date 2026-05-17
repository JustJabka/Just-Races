package justjabka.WeltenRaces.Registries;

import com.google.gson.Gson;
import justjabka.WeltenRaces.Instances.RaceInstance;
import justjabka.WeltenRaces.WeltenRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class RaceRegistry {
    private static final Map<NamespacedKey, RaceInstance> registeredRaces = new HashMap<>();
    private static final Gson gson = new Gson();

    public static void register(Plugin plugin) {
        File racesFolder = new File(plugin.getDataFolder(), "races");

        if (!racesFolder.exists()) racesFolder.mkdirs();

        File[] files = racesFolder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) return;

        registeredRaces.clear();

        for (File file : files) {
            try (FileReader reader = new FileReader(file)) {
                RaceInstance race = gson.fromJson(reader, RaceInstance.class);

                String fileNameWithNoExtension = file.getName().replaceFirst("[.][^.]+$", "");
                race.setKey(fileNameWithNoExtension.toLowerCase());

                registeredRaces.put(race.getKey(), race);

                WeltenRaces.LOGGER.info("Race successfully registered: {}", race.getKey());
            } catch (Exception e) {
                WeltenRaces.LOGGER.error("Error while registering race: {}", file.getName(), e);
            }
        }
    }

    public static Map<NamespacedKey, RaceInstance> getRegisteredRaces() {
        return registeredRaces;
    }
}