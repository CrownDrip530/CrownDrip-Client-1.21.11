package com.hack.modules.combat;

import com.hack.modules.HackModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

/**
 * AntiPotion — removes negative status effects every tick.
 */
public class AntiPotion extends HackModule {

    private final Minecraft mc = Minecraft.getInstance();

    private static final String[] NEGATIVE_EFFECTS = {
        "slowness", "weakness", "blindness", "nausea", "poison",
        "wither", "mining_fatigue", "hunger", "levitation",
        "darkness", "bad_omen", "unluck"
    };

    public AntiPotion() {
        super("AntiPotion", "Combat");
    }

    @Override
    public void onTick() {
        if (!isEnabled()) return;
        LocalPlayer p = mc.player;
        if (p == null) return;

        for (String effectId : NEGATIVE_EFFECTS) {
            try {
                // Use getEntry(Identifier) which exists in 1.21.11
                RegistryEntry<StatusEffect> entry = Registries.STATUS_EFFECT
                    .getEntry(Identifier.of("minecraft", effectId))
                    .orElse(null);

                if (entry != null && p.hasStatusEffect(entry)) {
                    p.removeStatusEffect(entry);
                }
            } catch (Exception ignored) {}
        }
    }
}
