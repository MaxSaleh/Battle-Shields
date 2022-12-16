package me.maxish0t.battleshields.common.handlers;

import me.maxish0t.battleshields.common.entity.blockentity.ShieldStandBlockEntity;
import me.maxish0t.battleshields.common.item.BattleShieldItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.ClipContext;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonSetupHandler {

    /**
     * This method allows for a shield to be inserted into a shield's stand's menu
     * by holding shift and right-clicking without opening the menu and inserting it from there.
     */
    @SubscribeEvent
    public static void insetItemIntoShieldStand(LivingEntityUseItemEvent.Start event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player player) {
            if (!player.level.isClientSide) {
                if (event.getItem().getItem() instanceof BattleShieldItem || event.getItem().getItem() instanceof ShieldItem) {
                    BlockPos blockPos = player.level.clip(new ClipContext(player.getEyePosition(1f),
                            player.getEyePosition(1f).add(player.getViewVector(1f).scale(30)),
                            ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos();
                    double distanceAway = blockPos.distToCenterSqr(player.position());
                    if (distanceAway < 20) {
                        if (player.level.getBlockEntity(blockPos) instanceof ShieldStandBlockEntity shieldStandBlockEntity) {
                            if (shieldStandBlockEntity.getStackInSlot(0) == ItemStack.EMPTY) {
                                shieldStandBlockEntity.setItem(0, event.getItem());
                                if (player.getUsedItemHand() == InteractionHand.MAIN_HAND) {
                                    player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                                } else if (player.getUsedItemHand() == InteractionHand.OFF_HAND) {
                                    player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
