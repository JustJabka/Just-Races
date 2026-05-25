package justjabka.JustRaces.Registries;

import com.google.gson.Gson;
import justjabka.JustRaces.Instances.RaceInstance;
import justjabka.JustRaces.JustRaces;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class RacesRegistry {
    private static final Map<NamespacedKey, RaceInstance> RACES = new HashMap<>();
    private static final Gson gson = new Gson();

    public static Map<NamespacedKey, RaceInstance> getRaces() {
        return Collections.unmodifiableMap(RACES);
    }

    public static void register(Plugin plugin) {
        File racesFolder = new File(plugin.getDataFolder(), "races");

        if (!racesFolder.exists()) {
            racesFolder.mkdirs();
        }

        saveDefaultRaces(plugin, racesFolder);

        RACES.clear();
        registerRaces(racesFolder);

        JustRaces.LOGGER.info("Successfully registered {} races!", RACES.size());
    }

    private static void registerRaces(File racesFolder) {
        File[] files = racesFolder.listFiles((dir, name) -> isValidJson(name));
        if (files == null) return;

        for (File file : files) {
            try (FileReader reader = new FileReader(file)) {
                RaceInstance race = gson.fromJson(reader, RaceInstance.class);

                String fileNameWithNoExtension = file.getName().replaceFirst("[.][^.]+$", "");
                race.setKey(fileNameWithNoExtension.toLowerCase());

                RACES.put(race.getKey(), race);
            } catch (Exception e) {
                JustRaces.LOGGER.error("Error while registering race: {}", file.getName(), e);
            }
        }
    }

    private static void saveDefaultRaces(Plugin plugin, File racesFolder) {
        URL jarUrl = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();

        try (InputStream resStream = jarUrl.openStream(); ZipInputStream zip = new ZipInputStream(resStream)) {
            ZipEntry entry;

            while ((entry = zip.getNextEntry()) != null) {
                String name = entry.getName();

                boolean isValidDirectory = name.startsWith("races/") && isValidJson(name) && !entry.isDirectory();

                if (!isValidDirectory) continue;

                String fileName = name.substring("races/".length());
                File targetFile = new File(racesFolder, fileName);

                // Ignore created files
                if (targetFile.exists()) continue;

                try (InputStream in = plugin.getResource(name)) {
                    if (in == null) continue;

                    Files.copy(in, targetFile.toPath());
                }
            }
        } catch (IOException e) {
            plugin.getSLF4JLogger().error("Error while extracting default races", e);
        }
    }

    private static boolean isValidJson(String name) {
        return name.endsWith(".json");
    }
}