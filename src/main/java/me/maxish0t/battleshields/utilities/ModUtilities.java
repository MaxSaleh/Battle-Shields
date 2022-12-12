package me.maxish0t.battleshields.utilities;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ModUtilities {

    /**
     * Creates a {@link MutableComponent} for the when blocking tooltip.
     *
     * @return the new text component.
     */
    public static Component getBlockingTextComponent() {
        return Component.translatable("battleshields.shield_blocking").withStyle(ChatFormatting.GRAY);
    }

    /**
     * Creates a {@link MutableComponent} for the damage reduction tooltip.
     *
     * @param reduction the damage reduction of the shield for which the text
     *                  component will be used.
     * @return the new text component.
     */
    public static Component getDamageReductionTextComponent(int reduction) {
        return Component.translatable("battleshields.shield_damage_reduction", reduction)
                .withStyle(ChatFormatting.DARK_GREEN);
    }

}
