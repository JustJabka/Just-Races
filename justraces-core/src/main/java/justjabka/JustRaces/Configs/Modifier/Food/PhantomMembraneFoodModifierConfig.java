package justjabka.JustRaces.Configs.Modifier.Food;

import org.bukkit.configuration.file.FileConfiguration;

public class PhantomMembraneFoodModifierConfig {
    public final int nutritionAmount;
    public final float saturationAmount;
    public final double healAmount;
    public final float consumeSeconds;

    public PhantomMembraneFoodModifierConfig(FileConfiguration config) {
        this.nutritionAmount = config.getInt("nutrition-amount", 0);
        this.saturationAmount = (float) config.getDouble("saturation-amount", 0);
        this.healAmount = config.getDouble("heal-amount", 1);
        this.consumeSeconds = (float) config.getDouble("consume-seconds", 0.8);
    }
}
