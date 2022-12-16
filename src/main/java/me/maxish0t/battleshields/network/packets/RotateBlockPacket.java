package me.maxish0t.battleshields.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RotateBlockPacket {

    private final BlockPos pos;

    public RotateBlockPacket(final BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(final RotateBlockPacket msg, final FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBlockPos(msg.pos);
    }

    public static RotateBlockPacket decode(final FriendlyByteBuf packetBuffer) {
        return new RotateBlockPacket(packetBuffer.readBlockPos());
    }

    public static void handleServerSide(final RotateBlockPacket msg, final Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            System.out.println("Button Clicked");
        });
        context.setPacketHandled(true);
    }
    
}
