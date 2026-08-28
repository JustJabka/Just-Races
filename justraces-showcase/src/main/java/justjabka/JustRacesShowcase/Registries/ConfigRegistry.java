package justjabka.JustRacesShowcase.Registries;

import justjabka.JustRacesShowcase.Configs.Ability.DamageInversionAbilityConfig;
import justjabka.JustRacesShowcase.Configs.Ability.EcdysisAbilityConfig;
import justjabka.JustRacesShowcase.Configs.Ability.FrogTongueAbilityConfig;
import justjabka.JustRacesShowcase.Configs.Ability.SlimeTrailAbilityConfig;
import justjabka.JustRacesShowcase.Configs.Modifier.Armor.*;
import org.bukkit.plugin.Plugin;

import static justjabka.JustRaces.Managers.ConfigManager.loadAbilityConfig;
import static justjabka.JustRaces.Managers.ConfigManager.loadModifierConfig;

public class ConfigRegistry {
    // Abilities
    public final DamageInversionAbilityConfig damageInversionAbilityConfig;
    public final EcdysisAbilityConfig ecdysisAbilityConfig;
    public final FrogTongueAbilityConfig frogTongueAbilityConfig;
    public final SlimeTrailAbilityConfig slimeTrailAbilityConfig;

    // Modifiers
    public final LeatherArmorModifierConfig leatherArmorModifierConfig;
    public final CopperArmorModifierConfig copperArmorModifierConfig;
    public final ChainmailArmorModifierConfig chainmailArmorModifierConfig;
    public final IronArmorModifierConfig ironArmorModifierConfig;
    public final GoldenArmorModifierConfig goldenArmorModifierConfig;

    public ConfigRegistry(Plugin plugin) {
        // Abilities
        this.damageInversionAbilityConfig = new DamageInversionAbilityConfig(loadAbilityConfig(plugin, "damage-inversion"));
        this.ecdysisAbilityConfig = new EcdysisAbilityConfig(loadAbilityConfig(plugin, "ecdysis"));
        this.frogTongueAbilityConfig = new FrogTongueAbilityConfig(loadAbilityConfig(plugin, "frog-tongue"));
        this.slimeTrailAbilityConfig = new SlimeTrailAbilityConfig(loadAbilityConfig(plugin, "slime-trail"));

        // Modifiers
        this.leatherArmorModifierConfig = new LeatherArmorModifierConfig(loadModifierConfig(plugin, "armor/leather"));
        this.copperArmorModifierConfig = new CopperArmorModifierConfig(loadModifierConfig(plugin, "armor/copper"));
        this.chainmailArmorModifierConfig = new ChainmailArmorModifierConfig(loadModifierConfig(plugin, "armor/chainmail"));
        this.ironArmorModifierConfig = new IronArmorModifierConfig(loadModifierConfig(plugin, "armor/iron"));
        this.goldenArmorModifierConfig = new GoldenArmorModifierConfig(loadModifierConfig(plugin, "armor/golden"));
    }
}
