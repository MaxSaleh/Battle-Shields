package me.maxish0t.battleshields;

import me.maxish0t.battleshields.common.init.ModRegistries;
import me.maxish0t.battleshields.utilities.ModReference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModReference.MOD_ID)
public class BattleShields {
	public BattleShields() {
		ModRegistries.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
