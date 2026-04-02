package com.hack.modules.movement;

import com.hack.modules.HackModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

/**
 * Phase — slip through walls by temporarily enabling noClip
 * when the player is colliding horizontally with a block.
 *
 * Smarter than NoClip: only phases when actually stuck against a wall,
 * normal physics otherwise. Harder to detect than full NoClip.
 */
public class Phase extends HackModule {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public Phase() {
        super("Phase", "Movement");
    }

    @Override
    public void onTick() {
        if (!isEnabled()) return;
        ClientPlayerEntity p = mc.player;
        if (p == null) return;

        // Enable noClip only when horizontally colliding (stuck against wall)
        if (p.horizontalCollision) {
            p.noClip = true;
            // Push through the wall slightly
            float yaw = (float) Math.toRadians(p.getYaw());
            p.setVelocity(
                p.getVelocity().x - Math.sin(yaw) * 0.1,
                p.getVelocity().y,
                p.getVelocity().z + Math.cos(yaw) * 0.1
            );
        } else {
            p.noClip = false;
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null) mc.player.noClip = false;
    }
}
