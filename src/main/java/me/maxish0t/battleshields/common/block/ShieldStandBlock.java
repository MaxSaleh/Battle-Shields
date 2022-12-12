package me.maxish0t.battleshields.common.block;

import me.maxish0t.battleshields.common.entity.blockentity.ShieldStandBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShieldStandBlock extends BaseEntityBlock {

    public static DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public ShieldStandBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD)
                .requiresCorrectToolForDrops()
                .strength(40.0F)
                .noOcclusion()
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
            if (blockEntity instanceof ShieldStandBlockEntity shieldStandBlockEntity) {
                player.openMenu(shieldStandBlockEntity);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ShieldStandBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(HORIZONTAL_FACING,
                blockPlaceContext.getHorizontalDirection().getOpposite());
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
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        list.add(Component.translatable("block.battleshields.shield_stand.info").withStyle(ChatFormatting.RED));
    }
}
