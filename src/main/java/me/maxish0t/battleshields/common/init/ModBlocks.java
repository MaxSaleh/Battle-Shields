package me.maxish0t.battleshields.common.init;

import me.maxish0t.battleshields.common.block.ShieldStandBlock;
import me.maxish0t.battleshields.utilities.ModReference;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModReference.MOD_ID);

    /**
     * Shield Stand
     */
    public static final RegistryObject<Block> SHIELD_STAND = registerBlock("shield_stand", ShieldStandBlock::new);

    /**
     * Block registration helper methods.
     */
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> blockSupplier) {
        RegistryObject<T> newBlock = BLOCKS.register(name, blockSupplier);
        registerBlockItem(name, newBlock);
        return newBlock;
    }
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(ModCreativeTabs.MAIN_TAB)));
    }

}
