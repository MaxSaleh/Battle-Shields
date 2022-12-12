package me.maxish0t.battleshields.common.init;

import me.maxish0t.battleshields.common.recipes.ShieldRecipes;
import me.maxish0t.battleshields.utilities.ModReference;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ModReference.MOD_ID);

    /**
     * Recipe Registration
     */
    public static final RegistryObject<RecipeSerializer<?>> SHIELD_DECORATION = RECIPE_SERIALIZER.register("shield_decoration",
            () -> ShieldRecipes.SERIALIZER);

}
