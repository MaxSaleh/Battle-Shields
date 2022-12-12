package me.maxish0t.battleshields.client;

import java.util.List;

import me.maxish0t.battleshields.common.item.BattleShieldItem;
import me.maxish0t.battleshields.common.config.Config;

import me.maxish0t.battleshields.utilities.ModReference;
import me.maxish0t.battleshields.utilities.ModUtilities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT, modid = ModReference.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public class TooltipHandler {

	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent e) {
		if (e.getItemStack().getItem() instanceof ShieldItem) {
			if (!Config.enableDamageReduction.get()) {
				return;
			}

			Item shield = e.getItemStack().getItem();
			List<Component> tooltip = e.getToolTip();
			tooltip.add(Component.literal(""));
			tooltip.add(ModUtilities.getBlockingTextComponent());

			if (shield == Items.SHIELD) {
				tooltip.add(ModUtilities.getDamageReductionTextComponent(Config.defaultDamageReduction.get()));
			} else if (shield instanceof BattleShieldItem) {
				tooltip.add(ModUtilities.getDamageReductionTextComponent(((BattleShieldItem) shield).getDamageReduction()));
			} else {
				tooltip.add(ModUtilities.getDamageReductionTextComponent(Config.customShieldMaxReduction.get() ? 100 : Config.defaultDamageReduction.get()));
			}
		}
	}

}