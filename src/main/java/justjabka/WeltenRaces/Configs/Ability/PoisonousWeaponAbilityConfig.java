package justjabka.WeltenRaces.Configs.Ability;

import org.bukkit.configuration.file.FileConfiguration;

public class PoisonousWeaponAbilityConfig {
    public final long cooldown;
    public final int activationAmount;

    public PoisonousWeaponAbilityConfig(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown", 1) * 20;
        this.activationAmount = config.getInt("activation-amount", 64);
    }
}
