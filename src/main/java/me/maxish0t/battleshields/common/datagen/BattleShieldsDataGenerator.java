package me.maxish0t.battleshields.common.datagen;

import me.maxish0t.battleshields.utilities.ModReference;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = ModReference.MOD_ID, bus = Bus.MOD)
public class BattleShieldsDataGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		generator.addProvider(event.includeServer(), new BattleShieldsRecipeProvider(generator));
	}

}
