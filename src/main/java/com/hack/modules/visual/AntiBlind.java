package com.hack.modules.visual;

import com.hack.modules.HackModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

/**
 * AntiBlind — removes visual blindness and darkness effects.
 *
 * Removes: blindness, darkness, nausea client-side every tick.
 * Also keeps gamma high to counteract darkness effect's light reduction.
 */
public class AntiBlind extends HackModule {

    private final MinecraftClient mc = Minecraft.getInstance();

    private static final String[] VISUAL_EFFECTS = {
        "blindness", "darkness", "nausea"
    };

    public AntiBlind() {
        super("AntiBlind", "Visual");
    }

    @Override
    public void onTick() {
        if (!isEnabled()) return;
        LocalPlayer p = mc.player;
        if (p == null) return;

        for (String effectId : VISUAL_EFFECTS) {
            try {
                RegistryEntry<StatusEffect> entry = Registries.STATUS_EFFECT
                    .getEntry(Identifier.of("minecraft", effectId))
                    .orElse(null);
                if (entry != null && p.hasStatusEffect(entry)) {
                    p.removeStatusEffect(entry);
                }
            } catch (Exception ignored) {}
        }

        // Also boost gamma to fight darkness lighting reduction
        try {
            double current = (Double) mc.options.getGamma().getValue();
            if (current < 5.0) mc.options.getGamma().setValue(10.0);
        } catch (Exception ignored) {}
    }
}
