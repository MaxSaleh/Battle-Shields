package me.maxish0t.battleshields.client.handlers;

import me.maxish0t.battleshields.client.renderer.ShieldStandRenderer;
import me.maxish0t.battleshields.client.screen.ShieldStandScreen;
import me.maxish0t.battleshields.common.init.ModBlockEntities;
import me.maxish0t.battleshields.common.init.ModContainers;
import me.maxish0t.battleshields.utilities.ModReference;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetupHandler {

    /**
     * Does all the client registrations like block renders etc.
     */
    public static void clientRegistries(final FMLClientSetupEvent event) {
        // Shield Stand
        BlockEntityRenderers.register(ModBlockEntities.SHIELD_STAND.get(), ShieldStandRenderer::new);
    }

    /**
     * Registers the menus to the correct screen.
     */
    public static void registerScreens(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModContainers.SHIELD_STAND.get(), ShieldStandScreen::new);
        });
    }

}
