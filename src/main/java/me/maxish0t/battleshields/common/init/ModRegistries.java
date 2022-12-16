package me.maxish0t.battleshields.common.init;

import me.maxish0t.battleshields.client.handlers.ClientSetupHandler;
import me.maxish0t.battleshields.common.config.Config;
import me.maxish0t.battleshields.common.handlers.CommonSetupHandler;
import me.maxish0t.battleshields.network.BattleShieldNetwork;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ModRegistries {

    /**
     * Registers all the mods important things.
     */
    public static void register(IEventBus iEventBus) {
        // Mod Stuff
        ModBlocks.BLOCKS.register(iEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(iEventBus);
        ModItems.ITEMS.register(iEventBus);
        ModEntities.ENTITIES.register(iEventBus);
        ModContainers.CONTAINERS.register(iEventBus);
        ModRecipes.RECIPE_SERIALIZER.register(iEventBus);

        // Forge Bus Events
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> iEventBus.addListener(ClientSetupHandler::registerScreens));
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> iEventBus.addListener(ClientSetupHandler::clientRegistries));

        // Event Handlers
        MinecraftForge.EVENT_BUS.register(CommonSetupHandler.class);

        // Config
        new Config();

        // Network
        BattleShieldNetwork.init();
    }

}
