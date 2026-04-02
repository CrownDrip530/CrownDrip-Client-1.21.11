package com.hack.modules.utility;

import com.hack.modules.HackModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Nuker — breaks all blocks in a radius automatically.
 */
public class Nuker extends HackModule {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    public final Setting rangeSetting = new Setting("Range", 4.0f, 1.0f, 6.0f);

    public Nuker() {
        super("Nuker", "Utility");
        settings.add(rangeSetting);
        alwaysShowSettings = true;
    }

    @Override
    public void onTick() {
        if (!isEnabled()) return;
        ClientPlayerEntity p = mc.player;
        if (p == null || mc.world == null || mc.interactionManager == null) return;

        int range = (int) rangeSetting.value;
        BlockPos center = p.getBlockPos();

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos pos = center.add(x, y, z);
                    if (mc.world.getBlockState(pos).isAir()) continue;
                    if (mc.world.getBlockState(pos).getHardness(mc.world, pos) < 0) continue; // unbreakable
                    mc.interactionManager.updateBlockBreakingProgress(pos,
                        net.minecraft.util.math.Direction.UP);
                    return; // one block per tick
                }
            }
        }
    }
}
