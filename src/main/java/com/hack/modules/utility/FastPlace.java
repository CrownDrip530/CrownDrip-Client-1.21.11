package com.hack.modules.utility;

import com.hack.mixins.MinecraftClientAccessor;
import com.hack.modules.HackModule;
import net.minecraft.client.MinecraftClient;

/**
 * FastPlace — removes the delay between placing blocks.
 * Uses MinecraftClientAccessor to access private itemUseCooldown field.
 */
public class FastPlace extends HackModule {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public FastPlace() { super("FastPlace", "Utility"); }

    @Override
    public void onTick() {
        if (!isEnabled()) return;
        ((MinecraftClientAccessor)(Object) mc).setItemUseCooldown(0);
    }
}
