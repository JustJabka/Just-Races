package justjabka.JustRacesShowcase.Configs.Modifier.Food;

import org.bukkit.configuration.file.FileConfiguration;

public class MossFoodModifierConfig {
    public final int nutritionAmount;
    public final float saturationAmount;
    public final double healAmount;
    public final float useCooldown;

    public MossFoodModifierConfig(FileConfiguration config) {
        this.nutritionAmount = config.getInt("nutrition-amount", 0);
        this.saturationAmount = (float) config.getDouble("saturation-amount", 0);
        this.healAmount = config.getDouble("heal-amount", 6);
        this.useCooldown = (float) config.getDouble("use-cooldown", 60);
    }
}
