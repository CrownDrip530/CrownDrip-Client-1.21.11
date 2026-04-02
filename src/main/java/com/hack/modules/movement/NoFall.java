package com.hack.modules.movement;

import com.hack.modules.HackModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

/**
 * NoFall - Never take fall damage.
 *
 * HOW IT WORKS:
 * MovementMixin intercepts sendMovementPackets() and sets onGround=true
 * in the packet every tick. The server never accumulates fall distance.
 *
 * onTick() also resets fallDistance client-side to prevent the
 * client-side damage flash when landing from high up.
 */
public class NoFall extends HackModule {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public NoFall() {
        super("NoFall", "Movement");
    }

    @Override
    public void onTick() {
        if (!isEnabled()) return;
        ClientPlayerEntity p = mc.player;
        if (p == null) return;

        // Reset client-side fall distance to prevent damage flash
        p.fallDistance = 0.0;
    }
}
