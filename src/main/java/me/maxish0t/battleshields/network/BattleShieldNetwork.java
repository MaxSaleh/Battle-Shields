package me.maxish0t.battleshields.network;

import me.maxish0t.battleshields.network.packets.RotateBlockPacket;
import me.maxish0t.battleshields.utilities.ModReference;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class BattleShieldNetwork {

    public static final String NETWORK_PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ModReference.MOD_ID, "main"),
            () -> NETWORK_PROTOCOL_VERSION,
            NETWORK_PROTOCOL_VERSION::equals,
            NETWORK_PROTOCOL_VERSION::equals
    );

    public static void init() {
        int networkId = 0;

        // Client -> Server
        CHANNEL.registerMessage(networkId++,
                RotateBlockPacket.class,
                RotateBlockPacket::encode,
                RotateBlockPacket::decode,
                RotateBlockPacket::handleServerSide
        );
    }

}
