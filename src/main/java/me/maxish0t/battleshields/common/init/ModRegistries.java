package me.maxish0t.battleshields.common.init;

import me.maxish0t.battleshields.common.config.Config;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModRegistries {

    /**
     * Registers all the mods important things.
     */
    public static void register(IEventBus iEventBus) {
        ModBlocks.BLOCKS.register(iEventBus);
        ModItems.ITEMS.register(iEventBus);
        ModRecipes.RECIPE_SERIALIZER.register(iEventBus);
        new Config();
    }

}
