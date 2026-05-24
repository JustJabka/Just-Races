# JustRaces

**JustRaces** is an origin-like framework originally created for the Just server. 
The main architectural goal of this plugin was to build a flexible, code-driven framework instead of a bloated, monolithic system.

---

## 💡 Why JustRaces?

What makes **JustRaces** different from **Origins** or **Origins-Reborn**?

**JustRaces** does not restrict its toolkit entirely to Data-Driven configurations. 
Data-Driven mechanics should remain a useful utility for configuration, not a complex scripting language where every minor detail requires its own custom JSON handler. 

Instead, this plugin offers a powerful developer API combined with a lightweight Data-Driven layer — giving you the best of both worlds.

---

## 🧬 What is a Race?

Races in this framework are defined by five core components:

1. **Name** — The display name of the race supporting [components](https://minecraft.wiki/w/Text_component_format).
2. **Description** - The description of the race. Very similar to the [lore item component](https://minecraft.wiki/w/Data_component_format#lore)  
3. **Abilities** — Active abilities bound to the race.
4. **Item Modifiers** — Special properties applied to items (utilizing modern [item components](https://minecraft.wiki/w/Item_components) and [event listeners](https://docs.papermc.io/paper/dev/event-listeners/)) that trigger only when wielded by a specific race.
5. **Attributes** — Vanilla base [attributes](https://minecraft.wiki/w/Attribute) automatically applied to the player (e.g., `minecraft:max_health` or `minecraft:movement_speed`).
6. **Config** — Custom constants and parameters powered by [Sponge Configurate](https://github.com/SpongePowered/Configurate), accessible directly within the race logic for seamless balancing.

### 📝 Configuration Example (`epiphyte.json`)

```json
{
  "name": {
    "translate": "race.epiphyte.name",
    "fallback": "Epiphyte"
  },
  "description": [],
  "abilities": [
    "weltenraces:azalea_camouflage",
    "weltenraces:poisonous_area",
    "weltenraces:poisonous_weapon"
  ],
  "attributes": {
    "minecraft:max_health": 18,
    "minecraft:scale": 0.95
  },
  "item_modifiers": {
    "weltenraces:glow_berries": "minecraft:glow_berries",
    "weltenraces:moss": "minecraft:moss_block"
  },
  "config": {
    "moss_movement_speed_bonus": 0.1
  }
}
```

---

## 🎭 Showcase Races

The plugin includes **7 stock races** built into its core to showcase the framework's capabilities and serve as development examples:

* 🛡️ `Armat`
* 🌿 `Epiphyte`
* 🐐 `Fetr`
* 👤 `Human`
* 🦎 `Lizard`
* 👻 `Phantom`
* ☁️ `Skyzern`