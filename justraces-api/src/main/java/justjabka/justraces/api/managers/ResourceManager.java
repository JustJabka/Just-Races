package justjabka.justraces.api.managers;

import justjabka.justraces.api.JustRacesAPI;
import org.bukkit.plugin.Plugin;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ResourceManager {

    public static void registerRacesFromPlugin(Plugin addonPlugin) {
        ResourceManager.registerRacesFromResources(addonPlugin, "races/");
    }

    public static void registerRacesFromResources(Plugin plugin, String resourceDir) {
        String namespace = plugin.getName().toLowerCase();

        File targetFolder = new File(JustRacesAPI.getInstance().getDataFolder(), "races/" + namespace);

        ResourceManager.extractResources(plugin, resourceDir, targetFolder, ".json");
    }

    public static ConfigurationNode loadJsonNode(Plugin plugin, File targetFile, String internalResourcePath) {
        if (!targetFile.exists()) createDefaultConfig(plugin, targetFile, internalResourcePath);

        GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
                .path(targetFile.toPath())
                .build();

        try {
            return loader.load();
        } catch (ConfigurateException e) {
            JustRacesAPI.getLogger().error("Failed to load JSON config at: {}", targetFile.getAbsolutePath(), e);
            return loader.createNode();
        }
    }

    private static void createDefaultConfig(Plugin plugin, File targetFile, String internalResourcePath) {
        try (InputStream in = plugin.getResource(internalResourcePath)) {
            if (in != null) {
                Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                JustRacesAPI.getLogger().info("Created default config for {} at {}", targetFile.getName(), targetFile.getPath());
            } else {
                throw new IOException("Config for %s not found in %s".formatted(targetFile.getName(), targetFile.getPath()));
            }
        } catch (IOException e) {
            JustRacesAPI.getLogger().error("Failed to save default config resource '{}' from {}", internalResourcePath, plugin.getName(), e);
        }
    }

    public static void extractResources(Plugin plugin, String resourceDir, File targetFolder, String extension) {
        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }

        URL jarUrl = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();

        String normalizedDir = resourceDir.replace('\\', '/');
        if (normalizedDir.startsWith("/")) {
            normalizedDir = normalizedDir.substring(1);
        }
        if (!normalizedDir.endsWith("/")) {
            normalizedDir += "/";
        }

        try (InputStream resStream = jarUrl.openStream();
             ZipInputStream zip = new ZipInputStream(resStream)) {

            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                String name = entry.getName();

                boolean isValidFile = name.startsWith(normalizedDir) && name.endsWith(extension) && !entry.isDirectory();
                if (!isValidFile) continue;

                String fileName = new File(name).getName();

                String relativePath = name.substring(normalizedDir.length());
                File destinationFile = new File(targetFolder, relativePath);

                if (destinationFile.getParentFile() != null) {
                    destinationFile.getParentFile().mkdirs();
                }

                try (InputStream in = plugin.getResource(name)) {
                    if (in == null) continue;
                    Files.copy(in, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    JustRacesAPI.getLogger().info("Extracted resource: {}", fileName);
                }
            }
        } catch (IOException e) {
            JustRacesAPI.getLogger().error("Failed to extract resources from plugin: {}", plugin.getName(), e);
        }
    }
}
