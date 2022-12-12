package me.maxish0t.battleshields.client.handlers;

import me.maxish0t.battleshields.client.renderer.ShieldStandRenderer;
import me.maxish0t.battleshields.common.init.ModBlockEntities;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetupHandler {

    /**
     * Does all the client registrations like block renders etc.
     */
    public static void clientRegistries(final FMLClientSetupEvent event) {
        // Shield Stand
        BlockEntityRenderers.register(ModBlockEntities.SHIELD_STAND.get(), ShieldStandRenderer::new);
    }

}
