package me.maxish0t.battleshields.common.init;

import me.maxish0t.battleshields.utilities.ModReference;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class ModCreativeTabs {

    /**
     * Mods Main Creative Menu Tab.
     */
    public static final CreativeModeTab MAIN_TAB = (new CreativeModeTab(ModReference.MOD_ID + ".main_tab") {
        @Override
        public void fillItemList(@NotNull NonNullList<ItemStack> itemStacks) {
            itemStacks.add(Items.SHIELD.getDefaultInstance());
            super.fillItemList(itemStacks);
        }

        @Override
        public @NotNull Component getDisplayName() {
            return Component.literal(ChatFormatting.BLUE + super.getDisplayName().getString());
        }

        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModItems.DIAMOND_SHIELD.get());
        }
    });

}
