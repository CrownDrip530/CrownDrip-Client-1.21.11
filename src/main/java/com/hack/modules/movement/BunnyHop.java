package com.hack.modules.movement;

import com.hack.modules.HackModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

/**
 * BunnyHop — auto-jumps at perfect timing to maintain sprint speed.
 * Makes you very hard to catch in PvP.
 */
public class BunnyHop extends HackModule {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public BunnyHop() { super("BunnyHop", "Movement"); }

    @Override
    public void onTick() {
        if (!isEnabled()) return;
        ClientPlayerEntity p = mc.player;
        if (p == null) return;
        if (!mc.options.forwardKey.isPressed() && !mc.options.backKey.isPressed()
            && !mc.options.leftKey.isPressed() && !mc.options.rightKey.isPressed()) return;
        if (p.isOnGround()) p.jump();
    }
}
