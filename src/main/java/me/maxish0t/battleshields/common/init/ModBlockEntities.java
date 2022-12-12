package me.maxish0t.battleshields.common.init;

import me.maxish0t.battleshields.common.entity.blockentity.ShieldStandBlockEntity;
import me.maxish0t.battleshields.utilities.ModReference;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ModReference.MOD_ID);

    /**
     * Shield Stand
     */
    public static final RegistryObject<BlockEntityType<ShieldStandBlockEntity>> SHIELD_STAND = BLOCK_ENTITIES.register("shield_stand",
            () -> BlockEntityType.Builder.of(ShieldStandBlockEntity::new, ModBlocks.SHIELD_STAND.get()).build(null));

}
