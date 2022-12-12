package me.maxish0t.battleshields;

import me.maxish0t.battleshields.client.handlers.ClientSetupHandler;
import me.maxish0t.battleshields.common.init.ModRegistries;
import me.maxish0t.battleshields.utilities.ModReference;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModReference.MOD_ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class BattleShields {

	public BattleShields() {
		final IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		iEventBus.addListener(ClientSetupHandler::clientRegistries);
		ModRegistries.register(iEventBus);
	}

}
