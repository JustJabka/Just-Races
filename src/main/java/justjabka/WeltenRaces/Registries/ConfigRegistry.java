package justjabka.WeltenRaces.Registries;

import justjabka.WeltenRaces.Configs.Ability.*;
import justjabka.WeltenRaces.Configs.Modifier.Armor.*;
import justjabka.WeltenRaces.Configs.Modifier.Food.GlowBerriesFoodModifierConfig;
import justjabka.WeltenRaces.Configs.Modifier.Food.MossFoodModifierConfig;
import justjabka.WeltenRaces.Configs.Modifier.Food.PhantomMembraneFoodModifierConfig;
import justjabka.WeltenRaces.Configs.Race.*;
import org.bukkit.plugin.Plugin;

import static justjabka.WeltenRaces.Managers.ConfigManager.*;

public class ConfigRegistry {
    // Races
    public final ArmatRaceConfig armatRaceConfig;
    public final PhantomRaceConfig phantomRaceConfig;
    public final SkyzernRaceConfig skyzernRaceConfig;
    public final EpiphyteRaceConfig epiphyteRaceConfig;
    public final LizardRaceConfig lizardRaceConfig;

    // Abilities
    public final DamageInversionAbilityConfig damageInversionAbilityConfig;
    public final EcdysisAbilityConfig ecdysisAbilityConfig;
    public final PredatorVisionAbilityConfig predatorVisionAbilityConfig;
    public final UnfoldWingsAbilityConfig unfoldWingsAbilityConfig;
    public final WildHuntAbilityConfig wildHuntAbilityConfig;
    public final WeightlessWillowSwayAbilityConfig weightlessWillowSwayAbilityConfig;
    public final CompressedSkyShardAbilityConfig compressedSkyShardAbilityConfig;
    public final PoisonousAreaAbilityConfig poisonousAreaAbilityConfig;
    public final AzaleaCamouflageAbilityConfig azaleaCamouflageAbilityConfig;
    public final PoisonousSplitAbilityConfig poisonousSplitAbilityConfig;
    public final SwiftSneakAbilityConfig swiftSneakAbilityConfig;
    public final TrueFormAbilityConfig trueFormAbilityConfig;

    // Modifiers
    public final LeatherArmorModifierConfig leatherArmorModifierConfig;
    public final CopperArmorModifierConfig copperArmorModifierConfig;
    public final ChainmailArmorModifierConfig chainmailArmorModifierConfig;
    public final IronArmorModifierConfig ironArmorModifierConfig;
    public final GoldenArmorModifierConfig goldenArmorModifierConfig;

    public final GlowBerriesFoodModifierConfig glowBerriesFoodModifierConfig;
    public final MossFoodModifierConfig mossFoodModifierConfig;
    public final PhantomMembraneFoodModifierConfig phantomMembraneFoodModifierConfig;

    public ConfigRegistry(Plugin plugin) {
        // Races
        this.armatRaceConfig = new ArmatRaceConfig(loadRaceConfig(plugin, "armat"));
        this.phantomRaceConfig = new PhantomRaceConfig(loadRaceConfig(plugin, "phantom"));
        this.skyzernRaceConfig = new SkyzernRaceConfig(loadRaceConfig(plugin, "skyzern"));
        this.epiphyteRaceConfig = new EpiphyteRaceConfig(loadRaceConfig(plugin, "epiphyte"));
        this.lizardRaceConfig = new LizardRaceConfig(loadRaceConfig(plugin, "lizard"));

        // Abilities
        this.damageInversionAbilityConfig = new DamageInversionAbilityConfig(loadAbilityConfig(plugin, "damage-inversion"));
        this.ecdysisAbilityConfig = new EcdysisAbilityConfig(loadAbilityConfig(plugin, "ecdysis"));
        this.predatorVisionAbilityConfig = new PredatorVisionAbilityConfig(loadAbilityConfig(plugin, "predator-vision"));
        this.unfoldWingsAbilityConfig = new UnfoldWingsAbilityConfig(loadAbilityConfig(plugin, "unfold-wings"));
        this.wildHuntAbilityConfig = new WildHuntAbilityConfig(loadAbilityConfig(plugin, "wild-hunt"));
        this.weightlessWillowSwayAbilityConfig = new WeightlessWillowSwayAbilityConfig(loadAbilityConfig(plugin, "weightless-willow-sway"));
        this.compressedSkyShardAbilityConfig = new CompressedSkyShardAbilityConfig(loadAbilityConfig(plugin, "compressed-sky-shard"));
        this.poisonousAreaAbilityConfig = new PoisonousAreaAbilityConfig(loadAbilityConfig(plugin, "poisonous-area"));
        this.azaleaCamouflageAbilityConfig = new AzaleaCamouflageAbilityConfig(loadAbilityConfig(plugin, "azalea-camouflage"));
        this.poisonousSplitAbilityConfig = new PoisonousSplitAbilityConfig(loadAbilityConfig(plugin, "poisonous-split"));
        this.swiftSneakAbilityConfig = new SwiftSneakAbilityConfig(loadAbilityConfig(plugin, "swift-sneak"));
        this.trueFormAbilityConfig = new TrueFormAbilityConfig(loadAbilityConfig(plugin, "true-form"));

        // Modifiers
        this.leatherArmorModifierConfig = new LeatherArmorModifierConfig(loadModifierConfig(plugin, "armor/leather"));
        this.copperArmorModifierConfig = new CopperArmorModifierConfig(loadModifierConfig(plugin, "armor/copper"));
        this.chainmailArmorModifierConfig = new ChainmailArmorModifierConfig(loadModifierConfig(plugin, "armor/chainmail"));
        this.ironArmorModifierConfig = new IronArmorModifierConfig(loadModifierConfig(plugin, "armor/iron"));
        this.goldenArmorModifierConfig = new GoldenArmorModifierConfig(loadModifierConfig(plugin, "armor/golden"));

        this.glowBerriesFoodModifierConfig = new GlowBerriesFoodModifierConfig(loadModifierConfig(plugin, "food/glow-berries"));
        this.mossFoodModifierConfig = new MossFoodModifierConfig(loadModifierConfig(plugin, "food/moss"));
        this.phantomMembraneFoodModifierConfig = new PhantomMembraneFoodModifierConfig(loadModifierConfig(plugin, "food/phantom-membrane"));
    }
}
