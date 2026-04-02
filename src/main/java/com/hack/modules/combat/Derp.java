package com.hack.modules.combat;

import com.hack.modules.HackModule;
import net.minecraft.client.MinecraftClient;

/**
 * Derp — looks straight up or down alternating every tick.
 * Looks hilarious. Confuses some combat anticheats.
 */
public class Derp extends HackModule {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    private int tick = 0;

    public Derp() { super("Derp", "Combat"); }

    @Override
    public void onTick() {
        if (!isEnabled() || mc.player == null) return;
        mc.player.setPitch(tick % 2 == 0 ? 90.0f : -90.0f);
        tick++;
    }
}
