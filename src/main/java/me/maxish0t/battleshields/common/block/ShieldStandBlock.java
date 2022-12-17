package me.maxish0t.battleshields.common.block;

import me.maxish0t.battleshields.common.entity.blockentity.ShieldStandBlockEntity;
import me.maxish0t.battleshields.common.item.BattleShieldItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShieldStandBlock extends BaseEntityBlock {

    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final VoxelShape voxelShape = Block.box(0, 0, 0, 16, 32, 16);

    public ShieldStandBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD)
                .strength(3.0F, 5.0F)
                .noOcclusion()
                .lightLevel((level) -> 8)
                .sound(SoundType.ANVIL));
        this.registerDefaultState(this.getStateDefinition().any().getBlock().defaultBlockState()
                .setValue(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);

            if (blockEntity instanceof ShieldStandBlockEntity shieldStandBlockEntity)
                if (!player.isSecondaryUseActive())
                    NetworkHooks.openScreen((ServerPlayer) player, shieldStandBlockEntity, blockPos);

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @Nullable LivingEntity livingEntity, @NotNull ItemStack itemStack) {
        BlockEntity tile = level.getBlockEntity(blockPos);
        if (tile instanceof ShieldStandBlockEntity shieldStandBlockEntity) {
            if (livingEntity != null) {
                int angle = Mth.floor(livingEntity.getYRot() * 4.0F / 360.0F + 0.5D) & 3;
                ++angle;
                angle %= 4;
                shieldStandBlockEntity.setAngle(getFacing(angle));
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ShieldStandBlockEntity(blockPos, blockState);
    }

    @Override
    public void onRemove(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState2, boolean p_60519_) {
        if (blockState.hasBlockEntity()) {
            if (level.getBlockEntity(blockPos) instanceof ShieldStandBlockEntity shieldStandBlockEntity) {
                if(shieldStandBlockEntity.getStackInSlot(0) != ItemStack.EMPTY) {
                    if (shieldStandBlockEntity.dropItems()) {
                        Block.popResource(level, blockPos, shieldStandBlockEntity.getStackInSlot(0));
                    }
                }
            }
        }
        super.onRemove(blockState, level, blockPos, blockState2, p_60519_);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(HORIZONTAL_FACING, direction.rotate(state.getValue(HORIZONTAL_FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState blockState, @NotNull Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(HORIZONTAL_FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HORIZONTAL_FACING);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext context) {
        return voxelShape;
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return voxelShape;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext context) {
        return voxelShape;
    }

    @Override
    public @NotNull VoxelShape getVisualShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext context) {
        return voxelShape;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        list.add(Component.empty());
        list.add(Component.translatable("block.battleshields.shield_stand.info").withStyle(ChatFormatting.RED));
    }

    public Direction getFacing(int angle) {
        Direction face = Direction.WEST;
        switch (angle) {
            case 0 -> face = Direction.SOUTH;
            case 2 -> face = Direction.NORTH;
            case 3 -> face = Direction.EAST;
        }
        return face;
    }
}
