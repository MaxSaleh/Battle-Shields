package me.maxish0t.battleshields.common.init;

import me.maxish0t.battleshields.common.config.Config;
import me.maxish0t.battleshields.common.item.BattleShieldItem;
import me.maxish0t.battleshields.utilities.ModReference;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModReference.MOD_ID);

    /**
     * Original Shields
     */
    public static final RegistryObject<Item> IRON_SHIELD = ITEMS.register("iron_shield",
            () -> new BattleShieldItem(Config.ironDamageReduction, "forge:ingots/iron", Config.ironDurability.get(), false));
    public static final RegistryObject<Item> GOLD_SHIELD = ITEMS.register("gold_shield",
            () -> new BattleShieldItem(Config.goldDamageReduction, "forge:ingots/gold", Config.goldDurability.get(), false));
    public static final RegistryObject<Item> DIAMOND_SHIELD = ITEMS.register("diamond_shield",
            () -> new BattleShieldItem(Config.diamondDamageReduction, "forge:gems/diamond", Config.diamondDurability.get(), false));
    public static final RegistryObject<Item> NETHERITE_SHIELD = ITEMS.register("netherite_shield",
            () -> new BattleShieldItem(Config.netheriteDamageReduction, "forge:ingots/netherite", Config.netheriteDurability.get(), true));

}
