package com.hack.modules.utility;

import com.hack.modules.HackModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * Scaffold — places blocks under feet automatically.
 *
 * 1.21.11 FIX:
 * Use getSelectedSlot() and setSelectedSlot() which were added in 1.21.x
 * instead of directly accessing the private selectedSlot field.
 * Also use createSlotSetPacket() to send the correct packet to the server.
 */
public class Scaffold extends HackModule {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    private int savedSlot = -1;

    public Scaffold() {
        super("Scaffold", "Utility");
    }

    @Override
    public void onDisable() {
        if (savedSlot != -1 && mc.player != null) {
            mc.player.getInventory().setSelectedSlot(savedSlot);
            mc.player.networkHandler.sendPacket(
                mc.player.getInventory().createSlotSetPacket(savedSlot));
            savedSlot = -1;
        }
    }

    @Override
    public void onTick() {
        if (!isEnabled()) return;
        ClientPlayerEntity p = mc.player;
        if (p == null || mc.world == null || mc.interactionManager == null) return;

        BlockPos below = BlockPos.ofFloored(p.getX(), p.getY() - 0.1, p.getZ());
        if (!mc.world.getBlockState(below).isAir()) return;

        // Find block in hotbar
        int blockSlot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = p.getInventory().getStack(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                blockSlot = i;
                break;
            }
        }
        if (blockSlot == -1) return;

        // Save and switch slot
        savedSlot = p.getInventory().getSelectedSlot();
        p.getInventory().setSelectedSlot(blockSlot);
        p.networkHandler.sendPacket(p.getInventory().createSlotSetPacket(blockSlot));

        // Place block below
        BlockPos placeOn = below.down();
        Vec3d hitVec = new Vec3d(below.getX() + 0.5, below.getY(), below.getZ() + 0.5);
        BlockHitResult hitResult = new BlockHitResult(hitVec, Direction.UP, placeOn, false);
        mc.interactionManager.interactBlock(p, Hand.MAIN_HAND, hitResult);
        p.swingHand(Hand.MAIN_HAND);

        // Restore slot immediately
        p.getInventory().setSelectedSlot(savedSlot);
        p.networkHandler.sendPacket(p.getInventory().createSlotSetPacket(savedSlot));
        savedSlot = -1;
    }
}
