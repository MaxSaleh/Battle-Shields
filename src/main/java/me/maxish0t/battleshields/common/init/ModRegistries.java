package me.maxish0t.battleshields.common.init;

import me.maxish0t.battleshields.client.handlers.ClientSetupHandler;
import me.maxish0t.battleshields.common.config.Config;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;

public class ModRegistries {

    /**
     * Registers all the mods important things.
     */
    public static void register(IEventBus iEventBus) {
        ModBlocks.BLOCKS.register(iEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(iEventBus);
        ModItems.ITEMS.register(iEventBus);
        ModEntities.ENTITIES.register(iEventBus);
        ModContainers.CONTAINERS.register(iEventBus);
        ModRecipes.RECIPE_SERIALIZER.register(iEventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> iEventBus.addListener(ClientSetupHandler::registerScreens));
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> iEventBus.addListener(ClientSetupHandler::clientRegistries));

        new Config();
    }

}
