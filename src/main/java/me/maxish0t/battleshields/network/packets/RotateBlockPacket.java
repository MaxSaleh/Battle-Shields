package me.maxish0t.battleshields.network.packets;

import me.maxish0t.battleshields.common.block.ShieldStandBlock;
import me.maxish0t.battleshields.common.entity.blockentity.ShieldStandBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
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
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer != null) {
                Level level = serverPlayer.level;
                if (level.getBlockEntity(msg.pos) instanceof ShieldStandBlockEntity shieldStandBlockEntity) {
                    BlockState oldState = shieldStandBlockEntity.getBlockState();
                    BlockState newState = oldState.setValue(ShieldStandBlock.HORIZONTAL_FACING,
                            Rotation.CLOCKWISE_90.rotate(oldState.getValue(ShieldStandBlock.HORIZONTAL_FACING)));
                    shieldStandBlockEntity.setDropItems(false);
                    level.setBlock(shieldStandBlockEntity.getBlockPos(), newState, 3);
                    rotateShieldStandBlock(shieldStandBlockEntity);
                    shieldStandBlockEntity.setDropItems(true);
                }
            }
        });
        context.setPacketHandled(true);
    }

    private static void rotateShieldStandBlock(ShieldStandBlockEntity shieldStandBlockEntity) {
        if (shieldStandBlockEntity != null) {
            switch (shieldStandBlockEntity.getAngle()) {
                case SOUTH -> shieldStandBlockEntity.setAngle(Direction.WEST);
                case WEST -> shieldStandBlockEntity.setAngle(Direction.NORTH);
                case NORTH -> shieldStandBlockEntity.setAngle(Direction.EAST);
                case EAST -> shieldStandBlockEntity.setAngle(Direction.SOUTH);
            }
        }
    }

}
